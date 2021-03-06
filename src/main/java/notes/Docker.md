# Docker

- CentOS6.5及以上
[TOC]
## 概念&安装

- 镜像 一个只读的模板,创建Docker容器
- 容器 利用镜像创建的实例
- 仓库 集中存放镜像文件的场所


- 一般下载

```shell script
$ sudo yum install -y yum-utils

$ sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo
```
```shell script
$ sudo yum-config-manager --enable docker-ce-nightly
```
```shell script
$ sudo yum-config-manager --enable docker-ce-test

```
```shell script
$ sudo yum-config-manager --disable docker-ce-nightly
```
```shell script
$ sudo yum install docker-ce docker-ce-cli containerd.io
```
- 启动
```shell script
$ sudo systemctl start docker
```
- HelloWorld
```shell script
$ sudo docker run hello-world
```

### 改源
```shell script
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://xxxxxxx.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker

```
### 优缺点

| | Docker容器 | 虚拟机(VM) |
| --- | --- | --- |
|操作系统 | 与宿主机共享OS| 宿主机OS上运行虚拟机OS|   
|存储大小 | 镜像小,便于存储与传输 | 镜像庞大(vmdk,vdi等) |
|运行性能|几乎无额外性能损失|操作系统额外的CPU,内存消耗|
|移植性|灵活,轻便,适应于Linux|面向硬件运维者|
|部署速度|快速,秒级|较慢,10s以上|
[TOC]
## 常用命令
### 帮助命令
```shell script
docker help
docker info
docker version
```
### 镜像命令
```shell script
docker images
```
- 当前主机上的镜像 
- `-a`全部镜像(含中间镜像层)
- `-q`imageId
- `--digests` 详细信息
- `--no-trunc` 显示完整的镜像信息

```shell script
docker search xxx
```
- 查询镜像
- `-s [nums]`点赞数大于nums的
```shell script
docker pull xxx:[titile:默认不加]
docker rmi xxx
```
- `pull`  拉取镜像
- `rmi` 
    - 删除镜像,要保证镜像以关闭
    - `-f`强制删除
    -  可多个删除
    - 删除全部 `docker rmi -f $(docker images -qa`
    
### 容器命令

以windows的vmare中的centos中的centos为例

#### 新建并启动容器
```shell script
docker run[OPTIONS] IMAGE [COMMAND]{ARG...}
```
- `--name` 指定NAME
- `-d` 后台运行容器,并返回容器ID,也即启动守护式容器
- `-i`以交互模式运行容器,通常与-t同时使用
- `-t`为容器重新分配一个伪输入终端
#### 列出当前所有正住运行的容器
```shell script
docker ps [OPTIONS]
```
- `-q` 静默模式,只显示容器编号
- `-l` 显示最近创建过的容器
- `-a` 列出当前所有正在运行的容器+历史运行过的
- `-n` 显示最近n个创建的容器
- `--no-trunc` 不截断输出
#### 操作容器
- 退出容器
    1. `exit`关闭容器并退出
    2. `Ctrl`+`p`+`q` 退出不关闭
- 启动容器
    - `docker start xxx` id或容器名
- 重启容器
    - `docker restart xxx`id或容器名
- 停止
    - `docker stop xxx` 停止容器
    - `docker kill xxx` 强制停止容器
    
- 删除容器
`docker rm xxx`没有i
    - `-f` 强制删除
    - 删除多个容器
        - `docker rm -f $(docker ps -a -q)`
        - `docker ps -a -q|xargs docker rm`
        
### 重要命令
1. 启动
    - `docker run -d centos` 后台启动centos,但会被瞬间退出
        - docker 容器后台运行,必须要有前台进程
        - 容器运行的命令如果不是那些一直挂起的命令(top,tail),会自动退出
2. 查看容器日志
    - `docker logs -f -t --tail n xxx`
        - `-t`是加入时间戳
        - `-f` 跟随最新的日志打印
        - `--tail` 数字显示最后多少条
3. 查看容器内运行的进程
    - `docker top xxx`相当于linux的top
4. 查看容器内部细节
    - `docker inspect 容器ID`
    
5. 进入正在运行的容器并以命令行交互
    - `docker exec -it 容器ID bashShell`
        - 是在容器中打开新的终端,并且可以启动新的进程
    - 重新进入`docker attach 容器ID`
        - 直接进入容器启动命令的终端,不会启动新的进程 
6. 从容器内拷贝文件到宿主机
    - `docker cp 容器ID:路径 宿主机路径:`

## 镜像原理

- 是什么: 
    1.UnionFS(联合文件系统) bootfs rootfs
    2. Docker镜像加载原理
    3. 分层的镜像
- 特点
    1. Docker镜像都是只读的
    2. 当容器启动时,一个新的可写层被加载到镜像的顶部.这一层通常被称作"容器层","容器层"之下的都叫镜像层

- tomcat
    - `docker run -it -p 8888:8080 tomcat ` 端口映射

- 提交commit
    - `docker commit -m="提交的信息" -a="作者" 容器ID 要创建的镜像名:[标签名]`

## Docker容器数据卷

- 是什么
    - 保存数据 类似docker cp
- 特点
    1. 数据卷可在容器之间共享或重用数据
    2. 卷中的更改可以直接生效
    3. 数据卷中的更改不会包含在镜像的更新中
    4. 数据卷的生命周期一直持续到没有容器继续使用它为止
- 数据卷 容器内添加
    - 直接命令添加
        - `docker run -it -v /宿主机绝对路径目录:/容器目录 镜像名` 如果目录不存在,会创建目录
        - `docker inspect` 查看详细信息,看是否挂载成功
        - `docker run -it -v /宿主机绝对路径目录:/容器内目录:ro 镜像名` 只读
         
    - dockerFile添加
    备注:
    

## DockerFile

### 直接命令添加

### DockerFile添加

1. 根目录下新建mydocker并进入
2. 可在Dockerfile中使用VOLUME指令来给镜像添加一个或多个数据卷
3. File构建
4. build后生成镜像
5. run容器

### 数据卷容器

命名的容器挂载数据卷,其他容器通过挂载这个(父容器)实现数据共享,挂在数据卷的容器,称之为数据卷容器

- 容器间传递共享(--volumes-from)
    - 先启动一个父容器dk01
    - dk02/dk03 (集成)共享dk01 
dk01
 ```json
 "Mounts": [
            {
                "Type": "volume",
                "Name": "bcae3861c5c98efb330484995446b084bab5e63ce69cb8c0d3d0a4d650caa7d4",
                "Source": "/var/lib/docker/volumes/bcae3861c5c98efb330484995446b084bab5e63ce69cb8c0d3d0a4d650caa7d4/_data",
                "Destination": "/dataVolumeContainer2",
                "Driver": "local",
                "Mode": "",
                "RW": true,
                "Propagation": ""
            },
            {
                "Type": "volume",
                "Name": "e072f0984f04bc3342bcf500549f2533c5b8b6a90f41fd60bd049048b46e84cb",
                "Source": "/var/lib/docker/volumes/e072f0984f04bc3342bcf500549f2533c5b8b6a90f41fd60bd049048b46e84cb/_data",
                "Destination": "/dataVolumeContainer1",
                "Driver": "local",
                "Mode": "",
                "RW": true,
                "Propagation": ""
            }
        ],

```
dk02
```json
 "Mounts": [
            {
                "Type": "volume",
                "Name": "e072f0984f04bc3342bcf500549f2533c5b8b6a90f41fd60bd049048b46e84cb",
                "Source": "/var/lib/docker/volumes/e072f0984f04bc3342bcf500549f2533c5b8b6a90f41fd60bd049048b46e84cb/_data",
                "Destination": "/dataVolumeContainer1",
                "Driver": "local",
                "Mode": "",
                "RW": true,
                "Propagation": ""
            },
            {
                "Type": "volume",
                "Name": "bcae3861c5c98efb330484995446b084bab5e63ce69cb8c0d3d0a4d650caa7d4",
                "Source": "/var/lib/docker/volumes/bcae3861c5c98efb330484995446b084bab5e63ce69cb8c0d3d0a4d650caa7d4/_data",
                "Destination": "/dataVolumeContainer2",
                "Driver": "local",
                "Mode": "",
                "RW": true,
                "Propagation": ""
            }
        ],

```

挂载对应一致,实现共享数据

### DockerFile 规范
1. 每条保留字指令都必须为大写字母且后面至少跟随一个参数
2. 指令按照从上到下,顺序执行
3. `#`表示注释
4. 每条指令都会创建一个新的镜像层,并对镜像进行提交