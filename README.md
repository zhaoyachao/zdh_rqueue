
# 轻量级队列
   此项目专为zdh系列项目研发,zdh系列是一个可视化数据处理平台,具体可参见 [zdh_web](https://github.com/zhaoyachao/zdh_web)
   本项目的初衷是,实现一个分布式优先级队列,对性能要求不高,如果你是做高性能的消息队列,那么此项目将不在你的考虑范围之内,实现此项目
   的目的是为了解决调度任务中,的优先级处理,此项目的管理界面在zdh_web项目
       
# FAQ
   当前项目底层基于redis, 依赖工具包redisson, 不保证强稳定性, 不支持大数据量(单队列建议不超过10w)
    
# 系统技术栈 
    开发语言：java (jdk1.8)
    使用开源组件：redisson
    
# 快速开始
   + 编译
   
         windows: build.bat
         linux: sh build.sh
         
   + 配置
          
         无
       
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
                  Object o = rQueueClient.poll();

# 个人联系方式
    邮件：1209687056@qq.com
    代码所有都以开源,敢兴趣的码友可以下载自行研究,个人也可有偿进行定制化开发

# 权益
    因代码全部开源,谨防一些不法分子利用项目做违法等事情,均和本人无关,本人不承担任何责任,当他人因任何利益等和本人产生冲突时，最终解释权归本人所有
              