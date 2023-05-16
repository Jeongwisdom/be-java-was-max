package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import request.HttpRequestUtils;
import response.HttpResponseUtils;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            HttpRequestUtils httpRequestUtils = new HttpRequestUtils(br);
            httpRequestUtils.debug(logger);
            httpRequestUtils.processRequestBody();

            HttpResponseUtils httpResponseUtils = new HttpResponseUtils(httpRequestUtils.getRequestLine());
            httpResponseUtils.response(out, logger);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
