package thomastodon.io.sinkapp;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.*;
import org.springframework.restdocs.snippet.TemplatedSnippet;
import org.springframework.util.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestResponseSnippet extends TemplatedSnippet {

    private static final String MULTIPART_BOUNDARY = "6o2knFse3p53ty9dmcQvWAIx1zInP11uCfbm";

    protected RequestResponseSnippet() {
        this(null);
    }

    private RequestResponseSnippet(Map<String, Object> attributes) {
        super("request-response", attributes);
    }

    @Override
    protected Map<String, Object> createModel(Operation operation) {
        Map<String, Object> model = new HashMap<>();
        model.put("method", operation.getRequest().getMethod());
        model.put("path", getPath(operation.getRequest()));
        model.put("requestHeaders", getHeaders(operation.getRequest()));
        model.put("requestBody", getRequestBody(operation.getRequest()));

        OperationResponse response = operation.getResponse();
        HttpStatus status = response.getStatus();
        model.put("responseBody", responseBody(response));
        model.put("statusCode", status.value());
        model.put("statusReason", status.getReasonPhrase());
        model.put("responseHeaders", headers(response));
        return model;
    }

    private String getPath(OperationRequest request) {
        String path = request.getUri().getRawPath();
        String queryString = request.getUri().getRawQuery();
        Parameters uniqueParameters = request.getParameters()
            .getUniqueParameters(request.getUri());
        if (!uniqueParameters.isEmpty() && includeParametersInUri(request)) {
            if (StringUtils.hasText(queryString)) {
                queryString = queryString + "&" + uniqueParameters.toQueryString();
            }
            else {
                queryString = uniqueParameters.toQueryString();
            }
        }
        if (StringUtils.hasText(queryString)) {
            path = path + "?" + queryString;
        }
        return path;
    }

    private boolean includeParametersInUri(OperationRequest request) {
        return request.getMethod() == HttpMethod.GET || request.getContent().length > 0;
    }

    private List<Map<String, String>> getHeaders(OperationRequest request) {
        List<Map<String, String>> headers = new ArrayList<>();

        for (Map.Entry<String, List<String>> header : request.getHeaders().entrySet()) {
            for (String value : header.getValue()) {
                if (HttpHeaders.CONTENT_TYPE.equals(header.getKey())
                    && !request.getParts().isEmpty()) {
                    headers.add(header(header.getKey(),
                        String.format("%s; boundary=%s", value, MULTIPART_BOUNDARY)));
                }
                else {
                    headers.add(header(header.getKey(), value));
                }

            }
        }

        for (RequestCookie cookie : request.getCookies()) {
            headers.add(header(HttpHeaders.COOKIE,
                String.format("%s=%s", cookie.getName(), cookie.getValue())));
        }

        if (requiresFormEncodingContentTypeHeader(request)) {
            headers.add(header(HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        }
        return headers;
    }

    private String getRequestBody(OperationRequest request) {
        StringWriter httpRequest = new StringWriter();
        PrintWriter writer = new PrintWriter(httpRequest);
        String content = request.getContentAsString();
        if (StringUtils.hasText(content)) {
            writer.printf("%n%s", content);
        }
        else if (isPutOrPost(request)) {
            if (request.getParts().isEmpty()) {
                String queryString = request.getParameters().toQueryString();
                if (StringUtils.hasText(queryString)) {
                    writer.println();
                    writer.print(queryString);
                }
            }
            else {
                writeParts(request, writer);
            }
        }
        return httpRequest.toString();
    }


    private boolean isPutOrPost(OperationRequest request) {
        return HttpMethod.PUT.equals(request.getMethod())
            || HttpMethod.POST.equals(request.getMethod());
    }

    private void writeParts(OperationRequest request, PrintWriter writer) {
        writer.println();
        for (Map.Entry<String, List<String>> parameter : request.getParameters().entrySet()) {
            if (parameter.getValue().isEmpty()) {
                writePartBoundary(writer);
                writePart(parameter.getKey(), "", null, null, writer);
            }
            else {
                for (String value : parameter.getValue()) {
                    writePartBoundary(writer);
                    writePart(parameter.getKey(), value, null, null, writer);
                    writer.println();
                }
            }
        }
        for (OperationRequestPart part : request.getParts()) {
            writePartBoundary(writer);
            writePart(part, writer);
            writer.println();
        }
        writeMultipartEnd(writer);
    }

    private void writePartBoundary(PrintWriter writer) {
        writer.printf("--%s%n", MULTIPART_BOUNDARY);
    }

    private void writePart(OperationRequestPart part, PrintWriter writer) {
        writePart(part.getName(), part.getContentAsString(), part.getSubmittedFileName(),
            part.getHeaders().getContentType(), writer);
    }

    private void writePart(String name, String value, String filename,
                           MediaType contentType, PrintWriter writer) {
        writer.printf("Content-Disposition: form-data; name=%s", name);
        if (StringUtils.hasText(filename)) {
            writer.printf("; filename=%s", filename);
        }
        writer.printf("%n");
        if (contentType != null) {
            writer.printf("Content-Type: %s%n", contentType);
        }
        writer.println();
        writer.print(value);
    }

    private void writeMultipartEnd(PrintWriter writer) {
        writer.printf("--%s--", MULTIPART_BOUNDARY);
    }

    private boolean requiresFormEncodingContentTypeHeader(OperationRequest request) {
        return request.getHeaders().get(HttpHeaders.CONTENT_TYPE) == null
            && isPutOrPost(request) && (!request.getParameters().isEmpty()
            && !includeParametersInUri(request));
    }

    private Map<String, String> header(String name, String value) {
        Map<String, String> header = new HashMap<>();
        header.put("name", name);
        header.put("value", value);
        return header;
    }

    private String responseBody(OperationResponse response) {
        String content = response.getContentAsString();
        return content.isEmpty() ? content : String.format("%n%s", content);
    }

    private List<Map<String, String>> headers(OperationResponse response) {
        List<Map<String, String>> headers = new ArrayList<>();
        for (Map.Entry<String, List<String>> header : response.getHeaders().entrySet()) {
            List<String> values = header.getValue();
            for (String value : values) {
                headers.add(header(header.getKey(), value));
            }
        }
        return headers;
    }
}
