ijoomer-adv-joomla/master
---------
<h1>Welcome to IjoomerAdvance SDK </h1>

iJoomer Advance is an SDK which can be integrated into an existing project as well as be used as a stand-alone project. This SDK can be used as a CMS (Joomla CMS) as well as a Social Network (JomSocial component of Joomla). 

To avoid you the hassle of developing stuff from scratch, we provide everything readymade so that you can easily use / re-use and extend it as per your needs.

Let us examine how iJoomer Advance for Android Native app is structured:

Basically, it can be divided into three major parts as under: 
<ul>
<li>Core Library</li>
<li>Data Provider Library</li>
<li>Implementation </li>
</ul>

<h2>Core Library </h2>

As name suggests, this is the core of the mobile application. 

<h3>Server Communication</h3>

Server Communication consists that part of the code which is responsible for communicate from the server to fetch data. 
While fetching data from the server, server communication handles sessions, security settings, verifies the network availability and validates the returned data.

<h3>Intelligent Data Caching</h3>

Once data is received, appropriate caching inside the sqlLite database is done. And depending upon the lifecycle of the data, it is cached and recycled. 

In nutshell, the core library decides whether the data should be retrieved from the cached database or the live server.

What’s really interesting is that while the live data is being loaded from the server, cached data is automatically displayed on the screen so that your user doesn’t have to wait for a response and has something to read while data is loading.


<h3>Data Parsing</h3>

Once the data is received from the server, it has to be parsed in a way that your activities get it. So, the JSON data gets parsed and is further sent to the cache library for insertion process. On another thread, the parsed data is returned to the DataProvider Library which requested a response for a certain activity. 

<h2>DataProvider Libraries</h2> 

The Data Provider Library contains classes for web-service calls from the server. It contains multiple functions which are called by activities. Each function has certain arguments which need to be passed in order to process a request. 

The simplest example would be to get the list of featured articles in iCMS.

In this case, no argument is passed, but the function creates the requested argument, sends it to the core part for data, and receives an object with the values which is then returned to your activity. 

Some functions do have arguments present, like login/authentication; where your activity calls this function with username and password arguments and in return, receives full object of the user if authentication is through or an error message, if login credentials are wrong.

For each of your activity screens, there is a variety of readily available functions which are getting called; hence, you don’t have you write codes for making calls. These are read-only functions which cannot be modified so as to receive consistency. 

But, if you need any additional data from the server which iJoomer Advance doesn’t provide by default, you can get it inside the custom data array which you can parse as per your requirements.

Each Joomla Component you use, has its own data provider library, like iCMS (Joomla Content) has its own data provider and JomSocial has its own. Depending upon the component you want to use in your Android Mobile Application, you can decide which one to include.

<h2>Implementation</h2> 

This part consists of implementation of the java classes (Activities). As a developer, this is your playground and you can customize the app related to Branding, Color Theme, layout alterations, style edits, adding your own view/screen and attaching it to the already created classes. 

<b>Virtually, 80% of your customization part will be happening in this part and that’s the reason we have kept this part at the disposal of developer. </b>

