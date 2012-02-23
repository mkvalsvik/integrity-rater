# First remove old symlinks
rm target/quality-web-1.0/WEB-INF/freemarker
rm target/quality-web-1.0/css
rm target/quality-web-1.0/js
rm target/quality-web-1.0/img

# Then clean
mvn clean

