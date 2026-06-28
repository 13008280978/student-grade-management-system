#!/bin/bash
echo "=== 安装 Tomcat 8.5 ==="
TOMCAT_VERSION=8.5.100
if [ ! -d "/opt/apache-tomcat-${TOMCAT_VERSION}" ]; then
  wget -q https://archive.apache.org/dist/tomcat/tomcat-8/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz -O /tmp/tomcat.tar.gz
  tar -xzf /tmp/tomcat.tar.gz -C /opt/
  rm /tmp/tomcat.tar.gz
fi

export CATALINA_HOME=/opt/apache-tomcat-${TOMCAT_VERSION}
chmod +x $CATALINA_HOME/bin/*.sh

# 如果项目有 war 文件，自动部署
WAR_FILE=$(find . -name "*.war" -not -path "./.devcontainer/*" | head -1)
if [ -n "$WAR_FILE" ]; then
  echo "检测到 WAR 文件: $WAR_FILE"
  cp "$WAR_FILE" $CATALINA_HOME/webapps/
fi

# 如果有 webapp 目录结构，创建软链接
WEBAPP_DIR=$(find . -type d -name "webapp" -not -path "./.devcontainer/*" -not -path "./target/*" | head -1)
if [ -z "$WEBAPP_DIR" ]; then
  WEBAPP_DIR=$(find . -type d -name "webContent" -o -name "WebContent" -o -name "webapp" -not -path "./.devcontainer/*" | head -1)
fi

echo "=== 启动 Tomcat ==="
$CATALINA_HOME/bin/startup.sh

echo ""
echo "====================================="
echo "  环境就绪！"
echo "  JDK: $(java -version 2>&1 | head -1)"
echo "  Tomcat: ${TOMCAT_VERSION}"
echo "  访问地址: http://localhost:8080"
echo "====================================="
