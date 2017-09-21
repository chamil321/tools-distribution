import ballerina.net.http;
import ballerina.net.http.response;

@http:configuration {
    basePath:"/echo",
    port:9094
}
service<http> echo {

    @http:resourceConfig {
        methods:["POST"],
        path:"/"
    }
    resource echo (http:Request req, http:Response res) {
        response:setStringPayload(res, "hello world");
        response:send(res);
    }
    
}

@http:configuration {
    basePath:"/echoOne",
    port:9094
}
service<http> echoOne {

    @http:resourceConfig {
        methods:["POST"],
        path:"/abc"
    }
    resource echoAbc (http:Request req, http:Response res) {
        response:setStringPayload(res, "hello world");
        response:send(res);

    }
}

@http:configuration {
    basePath:"/echoDummy"
}
service<http> echoDummy {

    @http:resourceConfig {
        methods:["POST"],
        path:"/"
    }
    resource echoDummy (http:Request req, http:Response res) {
        response:setStringPayload(res, "hello world");
        response:send(res);
    }

}
