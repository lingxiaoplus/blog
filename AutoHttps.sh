#!/usr/bin/env bash 
echo "开始安装快照";
sudo yum install snapd -y
sudo systemctl enable --now snapd.socket
sudo ln -s /var/lib/snapd/snap /snap
sudo snap install core
sudo snap refresh core
echo "开始安装certbot";
sudo snap install --classic certbot
echo "设置软链";
sudo ln -s /snap/bin/certbot /usr/bin/certbot
echo "获取证书，自动编辑nginx配置";
sudo certbot --nginx
echo "测试自动续订程序";
sudo certbot renew --dry-run