
# 轻量级队列
   此项目专为zdh系列项目研发,zdh系列是一个可视化数据处理平台,具体可参见 [zdh_web](https://github.com/zhaoyachao/zdh_web)
   本项目的初衷是,实现一个分布式优先级队列,对性能要求不高,如果你是做高性能的消息队列,那么此项目将不在你的考虑范围之内,实现此项目
   的目的是为了解决调度任务中,的优先级处理,此项目的管理界面在zdh_web项目
    
# 系统技术栈 
    开发语言：java (jdk1.8)
    使用开源组件：redisson, mysql
    
# 快速开始
   + 编译
   
         windows: build.bat
         linux: sh build.sh
         
   + 配置
          
         cd 1.0-SNAPSHOT
         配置conf目录下,queue.properties文件
         #服务端配置ip+port
         redis.host=127.0.0.1
         redis.passwd=9001
       
   + 运行
         
        直接把当前jar引入到项目中
         
         
   + 客户端调用
        
         拷贝zdh_rqueue.jar到个人项目,依赖redisson包
         
         生产者demo:
                  RQueueManager.buildDefault("127.0.0.1:6379", "");

                  RQueueClient rQueueClient = RQueueManager.getRQueueClient("tqueue");
                  rQueueClient.add(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
         
         消费者demo:
                  RQueueManager.buildDefault("127.0.0.1:6379", "");

                  RQueueClient rQueueClient = RQueueManager.getRQueueClient("tqueue");
                  rQueueClient
       