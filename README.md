# Collabora-android-app
An android application for the [Collabora](https://github.com/manuelperuzzi/Collabora) project.

## Features
- Provides an easy access to your Collabora world
    - sign-in to join the Collabora community immediately
    - log-in to have access to your personal data
- Insertion of personal and shared notes
    - textual content
    - location, to handle location-based notifications
    - expiration, to allow custom alarms
    - previous notes, to introduce a bond of precedence between notes
    - state, to promote the organization of work
- Organization in collaborations
    - private, unique collaboration for the personal user notes
    - groups, simple collaborations for everyday situations
    - projects, elaborate collaborations, structured in modules, for work and university related projects
- Management of an access rights policy in a collaboration
    - a user with a read access can only visualize notes and modules
    - a user with a write access can also create, update and delete notes and modules
    - an admin user, in addition to having a write access, can edit/remove the collaboration, add/remove members and change their access rights
- Keep the users aware about the state of the notes in their collaborations, by constantly send them custom notifications

## Contributing
This repository contains a university course project. Please, do not fork it or send us your ideas until this message is here (~ mid September 2017).

### Importing the project
The project has been developed using Androi Studio, and can be easily imported in such IDE.
#### Recommended configuration
- Download the latest [latest Android Studio version][anroid].
- Be sure that at least API level 21 is used.
#### IDE settings
- We use the default Android Studio code checker (which is quite good). Be sure to have its installed and active.
- (Only for Windows user) Set the line delimiter to LF.
- We use space instead of tabs, and tab of 4 spaces. Be sure you follow this rules before starting to code.
#### Import procedure
- Install git on your system, if you haven't yet.
- Fork this repository in github.
- Clone your forked project with `git clone https://github.com/<your-username>/Collabora-android-app`
- Open Android Studio, and go to File -> Open. In the window select the folder of the project, the import procedure should be smooth.
- For building the project use grade. Simply click the hammer icon in the top bar, or Build -> Make Project.
- For executing all the test right click on the package `org.gammf.collabora_android (test)` and click on Run tests in collabora_android
- For producing the APK go click on Build -> Build APK. This produce an unsigned APK in `app/build/outputs/apk`. Note: the APK is unsigned, you have to allow not trusted app in your device.

### Run the application
- First, run the collabora server. Follow instructions [here](https://github.com/manuelperuzzi/Collabora).
- **You have to manually set the IP address of the server before running the application**
  - Go to `app.util.AuthenticationUtils.java` class, and insert your server IP in `SERVER_IP`
  - Go to `app.util.RabbitMQConfig.java` class, and insert your server IP in `BROKER_ADDRESS`
- Open android studio, connect your device, and click Run -> Run app. Be sure the device has an API level >= 21
- If you have problem running the app, please, send us a email or open an issue [here](https://github.com/manuelperuzzi/Collabora-android-app).


[android]: https://developer.android.com/studio/index.html
