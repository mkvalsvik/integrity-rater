HOST=$1
export d=`eval date +%Y-%m-%d_%H%M`
FILENAME=$d.tar
WARDIR=target/quality-web-1.0

if [ "$1" == '' ]; then
echo Include destination ssh host as a command line parameter
exit 1
fi

pushd $WARDIR
echo Creating .tar file from $WARDIR ..
tar -cf $FILENAME *
echo Deploying $FILENAME to $HOST ..
scp $FILENAME $HOST:/tmp/
popd

