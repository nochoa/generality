targetDir=../workspace
echo GENERANDO RELEASE
for i in $( ls | grep generality ); do
  if [ -f archives/$i.zip ]; then
    echo archives/$i.zip already exist... skipping
  else
    cp -r $i archives/$i
    rm -rf archives/$i/workspace
    cp -r $targetDir archives/$i/workspace
    rm -rf `find archives/$i -type d -name .svn` 
    zip -r archives/$i.zip archives/$i
    rm -r archives/$i
  fi
done
