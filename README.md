Git Setup in Eclipse Steps:

1) Git install in eclipse
Indigo : EGit P2 Repository - http://download.eclipse.org/egit/updates
Juno : http://download.eclipse.org/releases/juno

2) Generate SSH2 key -> RSA Key
In Eclipse: Windows-> Preferences -> General -> Network Connections -> SSH2 -> Key Management (2nd Tab ) -> Generate RSA key

Save Keys: Path for ubunutu: /home/tasol/.ssh

3) Github
a ) Create a github account if doesn't exist.
b ) Fork main iJoomer repo : https://github.com/ijoomer-advance/ijoomer-adv-joomla
c ) Now, go to settings ( very last in right side ) -> Deploy Keys -> Add generated RSA SSH2 key from Eclipse in step 2.

4) Clone a repository into Eclipse
Import -> GIT

5) Install Git on your Ubuntu

sudo apt-get install git-core

############

Using Github Daily:

Commiting Your Code:

1) Make changes in your local repository and commit your changes into your forked repo.
2) Send Pull Request to your Master repo from where you fork your repo.

##########

Updating your Fork with latest code:

Go to your clone folder which you had created from eclipse.

1) git remote add ijoomerlive git@github.com:ijoomer-advance/ijoomer-adv-joomla.git
1st step is only for first time

2) git fetch ijoomerlive
3) git merge ijoomerlive/master