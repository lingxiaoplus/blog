FROM nginx:latest
COPY nginx.conf /etc/nginx/
COPY cert/ /etc/nginx/cert/
COPY fronted/ /www/wwwroot/blog/
# 容器启动nginx后会立刻退出，所以需要使用nginx的前台运行模式
CMD [ "nginx", "-g", "daemon off;"]