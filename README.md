# InstantMessenger

**Design Decisions:** We kept the project organized in three files: a multi-threaded server file, a threaded socket file, and a client file.
We kept the user interface simple enough for the user to understand easily and be able to send files and photos.
We had the host start the server, and each client connected to the server, adding to the server's list of BufferedReaders and PrintWriters.
When clients send information, it gets sent to the server and then rerouted back to all clients from the server.
When a clients leaves the chat, the chat continues. When the host leaves the chat, it notifies all users and closes the server.

**Problems:** These aren't necessarily huge issues, more of just "quirks" in our code. These are the only two we noticed:
* When anyone receives a file, it prompts them for a save destination.
However, if they hit "Cancel" or don't put a valid destination, it outputs the file contents to the chat box.
* The server can send files to all the clients, but the client can only send files to the server.
Of course, the host can always just send the file it's received to all the clients, but we didn't have time to implement it automatically.

**Additional Features:** Aside from a fully working messenger capable of sharing files and photos,
we added these minor, yet extremely helpful, add-ons:
* Allowing the clients to input their names for all to see in the messenger.
* Notifications for the whole chatroom whenever someone enters or exits the chat.
* We tested sending audio files and we were able to send small ones!

We hope you enjoy it,

Timothy Shaker & Parker Wilf
