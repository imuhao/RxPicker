package com.caimuhao.rxpicker;

/**
 * @author Smile
 * @time 2017/4/19  下午12:41
 * @desc ${TODD}
 */
public class context {

  /**
   Understanding Context In Android Application
   What is Context?
   As the name suggests, it’s the context of the current state of the application/object. It lets newly-created objects understand what has been going on. Typically you call it to get information regarding another part of your program (activity and package/application).
   And also, Context is a handle to the system, it provides services like resolving resources, obtaining access to databases and preferences, and so on. An Android app has activities. It’s like a handle to the environment your application is currently running in. The activity object inherits the Context object. It allows access to application specific resources and class and information about the application environment.
   Context is almost everywhere in Android Development and it is the most important thing in the Android Development, so we must understand to use it correctly.
   Wrong use of Context can easily lead to memory leaks in an android application.
   There are many different types of context in android, so let’s understand what are those, how to use those and when to use which one.
   Application Context
   It is an instance which is the singleton and can be accessed in an activity via getApplicationContext(). This context is tied to the lifecycle of an application. The application context can be used where you need a context whose lifecycle is separate from the current context or when you are passing a context beyond the scope of an activity.
   Example Use: If you have to create a singleton object for your application and that object needs a context, always pass the application context.
   If you pass the activity context here, it will lead to the memory leak as it will keep the reference to the activity and activity will not be garbage collected.
   In case, when you have to initialize a library in an activity, always pass the application context, not the activity context.
   You only use getApplicationContext() when you know you need a Context for something that may live longer than any other likely Context you have at your disposal.
   Activity Context
   This context is available in an activity. This context is tied to the lifecycle of an activity. The activity context should be used when you are passing the context in the scope of an activity or you need the context whose lifecycle is attached to the current context.
   Example Use: If you have to create an object whose lifecycle is attached to an activity, you can use the activity context.
   getContext() in ContentProvider
   This context is the application context and can be used similar to the application context. This can be accessed via getContext() method.
   When not to use getApplicationContext() ?
   It’s not a complete Context, supporting everything that Activity does. Various things you will try to do with this Context will fail, mostly related to the GUI.
   It can create memory leaks if the Context from getApplicationContext() holds onto something created by your calls on it that you don't clean up. With an Activity, if it holds onto something, once the Activity gets garbage collected, everything else flushes out too. The Application object remains for the lifetime of your process.
   The Rule of Thumb
   In most cases, use the Context directly available to you from the enclosing component you’re working within. You can safely hold a reference to it as long as that reference does not extend beyond the lifecycle of that component. As soon as you need to save a reference to a Context from an object that lives beyond your Activity or Service, even temporarily, switch that reference you save over to the application context.
   Happy Coding :)

   */
}
