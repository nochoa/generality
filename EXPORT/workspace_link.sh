workspacePath=../../workspace
echo ================
echo Generating links
echo ================
for i in $( ls | grep generality ); do
  if [ -f $i/workspace ]; then
    echo "$i/workspace EXISTS SKIPPING..."
  else
    ln -s $workspacePath $i/workspace
  fi
done
