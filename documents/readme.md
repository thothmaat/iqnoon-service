# zookeeper安装启动
## 1、安装
```
//查看zookeeper状态
zkCli
```

## 2、启动
```
Last login: Tue Jul  5 22:32:44 on ttys000
hexianjin@MacBook-Pro ~ % cd ..
hexianjin@MacBook-Pro /Users % cd ..
hexianjin@MacBook-Pro / % cd usr/local/bin
hexianjin@MacBook-Pro bin % zkServer status
ZooKeeper JMX enabled by default
Using config: /usr/local/etc/zookeeper/zoo.cfg
Client port found: 2181. Client address: localhost. Client SSL: false.
Error contacting service. It is probably not running.
hexianjin@MacBook-Pro bin % zkServer start
ZooKeeper JMX enabled by default
Using config: /usr/local/etc/zookeeper/zoo.cfg
Starting zookeeper ... STARTED
hexianjin@MacBook-Pro bin %
```