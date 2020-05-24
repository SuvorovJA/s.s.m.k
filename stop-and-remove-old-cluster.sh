minishift delete
mkdir $HOME/.minishift-bak
mv $HOME/.minishift/cache $HOME/.minishift-bak 
rm -fr $HOME/.minishift
mkdir $HOME/.minishift
mv $HOME/.minishift-bak/cache $HOME/.minishift
