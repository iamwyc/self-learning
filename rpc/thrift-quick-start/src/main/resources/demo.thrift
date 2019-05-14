namespace java com.github.iamwyc.api.hello

enum RequestType {
  JustForFun = 1,
  CodeOnly = 2,
  HappyRun = 3
}

struct HelloResponse {
  1: string welcomeString
  2: i32 serviceId
  3: list<string> gifts
}

struct HelloRequest {
  1: string name
  2: i64 timestamp
}

service HelloService {
  HelloResponse sayHello(1: RequestType type,2: HelloRequest request)
}