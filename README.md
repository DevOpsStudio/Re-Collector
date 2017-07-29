[TOC]

# 构建方式

```
mvn clean install package assembly:single -DskipTests
```

# 启动方式

```
java -jar ./target/graylog-collector-0.5.1-SNAPSHOT.jar run -f ./config/collector.conf

#检查内存泄漏的时候用
java -jar -Dio.netty.leakDetection.level=advanced ./target/graylog-collector-0.5.1-SNAPSHOT.jar run -f ./config/collector.conf
```

# 配置示例

```
message-buffer-size = 128

inputs {
  local-syslog {
    type = "file"
    path = "/var/log/syslog"
  }
  apache-access {
    type = "file"
    path = "/var/log/apache2/access.log"
    outputs = "gelf-tcp,console"
  }
  test-log {
    type = "file"
    path = "logs/file.log"
  }
}

outputs {
  gelf-tcp {
    type = "gelf"
    host = "127.0.0.1"
    port = 12201
    client-queue-size = 512
    client-connect-timeout = 5000
    client-reconnect-delay = 1000
    client-tcp-no-delay = true
    client-send-buffer-size = 32768
    inputs = "test-log"
  }
  console {
    type = "stdout"
  }
}
```
## 全局配置

| 参数                  | 说明                                       |
| ------------------- | ---------------------------------------- |
| message-buffer-size | 消息处理链的队列长度，默认是128。队列长度越长，输入的性能会越高，不过会带来Agent内存占用的问题 |
| enable-registration | 是否启用sidecar客户端管理，默认为true，请先使用false，没有实现客户端配置管理，只是上报了状态和度量 |
| server-url          | Graylog的SideCarUrl地址                     |
| collector-id        | 采集器唯一标志存放路径                              |
| host-name           | 主机名称，默认不填即可，可复写主机名                       |
|                     |                                          |

## 输入

输入配置在input块内

```
inputs {
....
}
```

| 参数                 | 说明                                       | 默认值     |
| ------------------ | ---------------------------------------- | ------- |
| type               | 输入的类型 必填，file、windows-eventlog           |         |
| path               | 文件采集的路径                                  |         |
| charset            | 字符集转换                                    | utf-8   |
| content-splitter   | 单行匹配还是多行匹配，newline 单行匹配，以\r为换行符，pattern，采用正则匹配，正则写在content-splitter-pattern配置里面 | newline |
| outputs            | 输出的路由，多输出的时候使用                           |         |
| path-glob-root     | 是否采用glob模式采集文件，path和path-glob-root仅能用一种  |         |
| path-glob-pattern  | glob模式的通配符，glob采集模式下才可使用                 |         |
| source-name        | WindowsEventlog的事件源，可用的有Application, System, Security |         |
| poll-interval      | WindowsEventlog的采集间隔                     |         |
| reader-interval    | 采集器的采集间隔                                 | 100毫秒   |
| reader-buffer-size | 采集器的读取缓冲大小                               | 102400  |

指定文件输入

```
local-syslog {
    type = "file"
    path = "/var/log/syslog"
    charset = "utf-8"
    content-splitter = "newline"
}
```

带路由指定文件输入

```
  apache-access {
    type = "file"
    path = "/var/log/apache2/access.log"
    outputs = "gelf-tcp,console"
  }
```

采集Windows Eventlog

```
win-application {
    type = "windows-eventlog"
    source-name = "Application"
    poll-interval = 1s
  }
```

## 输出

输出的配置都配在输出块中

```
outputs{

}
```

### GELF输出

| 参数                         | 说明                         | 值    |
| -------------------------- | -------------------------- | ---- |
| type                       | 输出类型，可选 gelf，stdout（控制台输出） |      |
| host                       | 输出的目标主机ip                  |      |
| port                       | 输出的目标主机端口                  |      |
| client-tls                 | 是否采用tls进行传输  false/true    |      |
| client-tls-cert-chain-file | tls认证文件路径                  |      |
| client-tls-verify-cert     | 是否校验证书  true/false         |      |
| client-queue-size          | gelf输出队列大小 512             |      |
| client-connect-timeout     | gelf连接的超时时间                |      |
| client-reconnect-delay     | gelf连接延时时间                 |      |
| client-tcp-no-delay        | 是否启用tcp no-delay模式         |      |
| client-send-buffer-size    | 发送的buffer长度                |      |
| protocol                   | 发送的协议，模式TCP，可选UDP          |      |
|                            |                            |      |

