package org.projw.blackserver.utils;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import org.projw.blackserver.controllers.exceptions.BusinessException;

import java.util.HashMap;
import java.util.Map;

public class Api {
    public static Result result() {
        return new Result();
    }

    public static Result error(BusinessException exception) {
        return result().param("error", exception.getErrorCode()).param("errorMessage", exception.getErrorMessage());
    }

    public static class Result {
        Map<String, Object> data = new HashMap<>();
        public Result param(String key, Object value) {
            data.put(key, value);
            return this;
        }

        @JsonAnyGetter
        public Map<String, Object> getData() {
            return data;
        }
    }

//    public static Result result(int returnCode) {
//        return new Result(returnCode);
//    }
//
//    @JsonFilter(RESULT_FILTER_NAME)
//    public static class Result1 {
//        @JsonProperty("error")
//        private int returnCode;
//        private Map<String, Object> params = new HashMap<>();
//
//        private Result1(int returnCode) {
//            this.returnCode = returnCode;
//        }
//
//        public ResultNeedValue param(String name) {
//            return new ResultNeedValue(this, name);
//        }
//
//        public int getReturnCode() {
//            return returnCode;
//        }
//
//        public void setReturnCode(int returnCode) {
//            this.returnCode = returnCode;
//        }
//
//        @JsonAnyGetter
//        public Map<String, Object> getParams() {
//            return params;
//        }
//
//        @JsonAnySetter
//        public void addParam(String key, Object value) {
//            params.put(key, value);
//        }
//
//        public static class ResultNeedValue {
//            private Result1 result;
//            private String lastParamName;
//
//            private ResultNeedValue(Result1 result, String paramName) {
//                this.result = result;
//                this.lastParamName = paramName;
//            }
//
//            public Result1 value(Object value) {
//                result.params.put(lastParamName, value);
//                return result;
//            }
//        }
//
//        public static class ResultFilter extends SimpleBeanPropertyFilter {
//            @Override
//            protected boolean include(BeanPropertyWriter writer) {
//                return true;
//            }
//
//            @Override
//            protected boolean include(PropertyWriter writer) {
//                return true;
//            }
//
//            @Override
//            public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {
//                if (include(writer)) {
//                    if (!writer.getName().equals("error")) {
//                        writer.serializeAsField(pojo, jgen, provider);
//                        return;
//                    }
//                    int intValue = ((Result1) pojo).getReturnCode();
//                    if (intValue > 0) {
//                        writer.serializeAsField(pojo, jgen, provider);
//                    }
//                } else if (!jgen.canOmitFields()) { // since 2.3
//                    writer.serializeAsOmittedField(pojo, jgen, provider);
//                }
//            }
//        }
//    }
//
//    public static class Result extends ResponseEntity<Object> {
//
//        private Map<String, Object> params = new HashMap<>();
//
//        private int errorCode;
//
//        private Object entity;
//
//        public Result (HttpStatus statusCode) {
//            super(statusCode);
//        }
//
//        public EntityResult entity(Object obj) {
//            return null;
//        }
//
//        public ResultNeedValue param(String name) {
//            return new ResultNeedValue(this, name);
//        }
//
//        public Result image(Image image) {
//            return this;
//        }
//
//        public Result error(int code) {
//
//        }
//
//        public static class ResultNeedValue {
//            private Result result;
//            private String lastParamName;
//
//            private ResultNeedValue(Result result, String paramName) {
//                this.result = result;
//                this.lastParamName = paramName;
//            }
//
//            public Result value(Object value) {
//                result.params.put(lastParamName, value);
//                return result;
//            }
//        }
//
//        public static class EntityResult {
//
//        }
//
//        public static class ImageResult {
//
//        }
//
//        public static class ErrorResult {
//
//        }
//
//
//    }
}
