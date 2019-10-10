package com.syphan.practice.commonservice.dubbo;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.internal.Platform;
import brave.propagation.Propagation;
import brave.propagation.TraceContext;
import brave.propagation.TraceContextOrSamplingFlags;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.protocol.dubbo.FutureAdapter;
import org.apache.dubbo.rpc.support.RpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.Future;

@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, value = {"tracing"})
public class TracingFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(TracingFilter.class);

    private Tracer tracer;
    private TraceContext.Extractor<Map<String, String>> extractor;
    private TraceContext.Injector<Map<String, String>> injector;

    public void setTracing(Tracing tracing) {
        logger.debug("called setTracing method");
        tracer = tracing.tracer();
        extractor = tracing.propagation().extractor(GETTER);
        injector = tracing.propagation().injector(SETTER);
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        logger.debug("called invoker method");
        if (tracer != null) {
            RpcContext rpcContext = RpcContext.getContext();
            Span.Kind kind = rpcContext.isProviderSide() ? Span.Kind.SERVER : Span.Kind.CLIENT;
            final Span span;
            if (kind == Span.Kind.CLIENT) {
                span = tracer.nextSpan();
                injector.inject(span.context(), invocation.getAttachments());
            } else {
                TraceContextOrSamplingFlags extracted = extractor.extract(invocation.getAttachments());
                span = extracted.context() != null
                        ? tracer.joinSpan(extracted.context())
                        : tracer.nextSpan(extracted);
            }

            if (!span.isNoop()) {
                span.kind(kind);
                String service = invoker.getInterface().getSimpleName();
                String method = RpcUtils.getMethodName(invocation);
                span.name(service + "/" + method);
                parseRemoteAddress(rpcContext, span);
                span.start();
            }

            boolean isOneWay = false, deferFinish = false;
            try {
                tracer.withSpanInScope(span);
                Result result = invoker.invoke(invocation);
                isOneWay = RpcUtils.isOneway(invoker.getUrl(), invocation);
                Future<Object> future = rpcContext.getFuture();
                if (future instanceof FutureAdapter) {
                    deferFinish = true;
                    ((FutureAdapter<Object>) future).whenComplete((o, exception) -> {
                        if (exception != null) {
                            onError(exception, span);
                        }
                        span.finish();
                    });
                }
                return result;
            } catch (Error | RuntimeException e) {
                onError(e, span);
                throw e;
            } finally {
                if (isOneWay) {
                    span.flush();
                } else if (!deferFinish) {
                    span.finish();
                }
            }
        } else return invoker.invoke(invocation);
    }

    private static void onError(Throwable error, Span span) {
        span.error(error);
        if (error instanceof RpcException) {
            span.tag("dubbo.error_code", Integer.toString(((RpcException) error).getCode()));
        }
    }

    private static void parseRemoteAddress(RpcContext rpcContext, Span span) {
        InetSocketAddress remoteAddress = rpcContext.getRemoteAddress();
        if (remoteAddress == null) return;
        span.remoteIpAndPort(Platform.get().getHostString(remoteAddress), remoteAddress.getPort());
    }

    private static final Propagation.Getter<Map<String, String>, String> GETTER =
            new Propagation.Getter<Map<String, String>, String>() {
                @Override
                public String get(Map<String, String> carrier, String key) {
                    return carrier.get(key);
                }

                @Override
                public String toString() {
                    return "Map::get";
                }
            };

    private static final Propagation.Setter<Map<String, String>, String> SETTER =
            new Propagation.Setter<Map<String, String>, String>() {
                @Override
                public void put(Map<String, String> carrier, String key, String value) {
                    carrier.put(key, value);
                }

                @Override
                public String toString() {
                    return "Map::set";
                }
            };
}
