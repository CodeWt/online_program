>Ubuntu查看端口使用情况，使用netstat命令：

查看已经连接的服务端口（ESTABLISHED）

netstat -a

查看所有的服务端口（LISTEN，ESTABLISHED）

netstat -ap

查看指定端口，可以结合grep命令：

netstat -ap | grep 8080

>也可以使用lsof命令：

lsof -i:8888

若要关闭使用这个端口的程序，使用kill + 对应的pid

kill -9 PID号

ps：kill就是给某个进程id发送了一个信号。默认发送的信号是SIGTERM，而kill -9发送的信号是SIGKILL，即exit。exit信号不会被系统阻塞，所以kill -9能顺利杀掉进程。


>springboot部署运行
```aidl
1.mvn clean package -Dmaven.test.skip=true
2.java -jar x.jar --spring.profiles.active=xxx

```