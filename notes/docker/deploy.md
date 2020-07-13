>阿里云docker-compose部署服务文件目录层级结构
```aidl
---|mnt
    ---|ide_docker
        ---|dockerfiles
            ---|nginx-Dockerfile
            ---|server-Dockerfile
        ---|esdata1
        ---|esdata2
        ---|logs
                ---|eslog1
                ---|eslog2
                ---|log1
                ---|log2
        ---|mysql
            ---|config
                ---|my.cnf
            ---|data   
            ---|init
                ---init.sql
        ---|nginx
            ---|logs
            ---|nginx.conf
        ---|online_program
            ---|log
            ---|src
                ---|main
                    ---|java
                    ---|resources
                        ---|mapper
                        ---|application.yaml
            ---|pom.xml
        ---|online_program2(同online_program)
        ---|static  //静态文件
        ---|sources.list //阿里云源
        ---|docker-compose.yaml
        

```