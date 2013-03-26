ijoomer-adv-joomla
==================
Git Setup in Eclipse Steps:

1) Git install in eclipse
Indigo : EGit P2 Repository - http://download.eclipse.org/egit/updates
Juno   : http://download.eclipse.org/releases/juno

2) Generate SSH2 key -> RSA Key
In Eclipse: Windows-> Preferences -> General -> Network Connections -> SSH2 -> Key Management (2nd Tab ) -> Generate RSA key

Save Keys: Path for ubunutu: /home/tasol/.ssh

3) Github
  a ) Create a github account if doesn't exist.
  b ) Fork main iJoomer repo : https://github.com/ijoomer-advance/ijoomer-adv-joomla
  c ) Now, go to settings ( very last in right side ) -> Deploy Keys -> Add generated RSA SSH2 key from Eclipse in step 2.

4) Clone a repository into Eclipse
Import -> GIT 