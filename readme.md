
1.采用技术：netty+spring+springboot

2.项目采用netty做为二维码扫描头的服务

3.项目类管理采用了spring

4.演示demo采用了springboot

5.项目启动顺序是是启动TcpServer，init_method ,startServer()方法。

6.demo 启动方式采用springboot 方式启动，运行com.kfit.App的main方法。

A.http://127.0.0.1:8080/hello 访问返回hello说明运行正常。
B.http://127.0.0.1:8080/list 查看连接上来项目列表。
C.http://127.0.0.1:8080/open 下发开门指令，需要提前配置设备sn在open方法。


