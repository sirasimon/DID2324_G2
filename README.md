#ShopDrop
This project was created as the final project for the course of **Digital Interaction Design** (DID) held by Prof. Giovanni **Malnati** for the master's degree course in **Cinema and Media Engineering** at **Politecnico di Torino** (PoliTO) in the academic year 2023/24.

###Disclaimer

This projects has to be intended as a Hi-Fi Prototype made in order to demostrate the interaction we designed for our scenario, therefore it can look incomplete in some of its parts.
Also, for security reasons, the project is no more able to connect to the database.

##Project
The final exam of the course of DID required a project that applied all the principles of _interaction design_ learnt during the term. This year the project was about the development of an interaction between a person and al smart-locker in every aspect, from the mobile app to the opening of a compartment in the locker. Every group was free to think a scenario and design with every means provided by the course – but not just that ones – the interaction.

###Scenario
We built our locker in the context of a shopping delivery, especially the one about stores willing to do home delivery. This delivery service, unlike traditional home delivery, eliminates inconveniences such as the need to be at home when the delivery person arrives, instead opting for delivery into a locker, preserving the ordered groceries for several hours. The advantages are manifold: the ability to order desired products from mobile devices and then pick them up from the most convenient locker at any time of day, even after store hours, without the stress of rushing to the supermarket before closing. All of this comes at reduced prices compared to home delivery.

From this starting point, we developed all the interactions between the users and the locker, via mobile app.

###Mobile App
From a single app, all the actors involved in the interaction could log into their specific version of the app. This solution helped us to reduce development time and reuse of code. Thus, depending on the credentials inserted it could be possible to land in one of the following versions:

* **CUSTOMER**: The app aims to be simple and endearing structured as follow:
	* The landing homepage where the customer could make an order
	* The cart screen where the customer could proceed with the checkout of the order
	* The profile screen where the customer could reach the orders history and control the status of the pendings one

	![Customer home](path "Customer homescreen")	![Customer home purchase dialog](path "Customer home purchase dialog")
	![Customer cart](path "Customer cart")
	![Customer checkout](path "Customer checkout")
	
* **CARRIER**: The app aims to be even simpler and thought to optimize the carrier work: an homepage where you can switch from the list of orders that have to be collected and one for that one that have to be deposited in the locker. There's also a profile screen that could help the carriers sets their status (online, available, etc)

	![Carrier home](path "Carrier home")	![Carrier detail](path "Carrier detail")	![Carrier scanner](path "Carrier scanner")
	
* **ADMIN**: A debug app used only to help us control the flow during the presentation and simulate the store behaviour.

	![Admin orders](path "Admin orders")
	![Admin order QR](path "Admin order QR")

> _The app was developed in Android Studio with **Kotlin** and **Jetpack Compose** as an exam request. Additionally, we also used [ZXing library](https://github.com/zxing/zxing) for QR scanning and rendering._

###Smart Locker
The locker was built with semi-transparent plastic panels connected by plastic joints. With the material provided, we've been able to assemble a sqared locker with two small compartments and a bigger one, and we automated only two of them – as requested by the assignment – with 4 servomotors: 2 for a locking mechanism and 2 as opening trigger.

In addition, to create the interaction we had in mind and that we are going to explain in the following paragraph, we needed a PIR sensor and a small OLED display.

We 3D printed some parts to complete the assembly such as cases for the central unit and extension components for the triggers.

> __

###Interaction
We developed the interaction with few keywords in mind:

* **SAFETY**: minimizing the phisical touch with the structure and with components, avoiding hygienic, electric and mechanical issues. This is obtained by letting the actors touch only the doors of the target compartments and the items ordered.
* **SECURITY**: minimizing the risk of errors from the collection of an order from the store by the carrier to the collection from the locker by the final customer. This is obtained by changing the QR periodically and switching it on and off depending on whether or not there is a person in front of the locker.
* **SUSTAINABILITY**: making the locker active only if it detects a person in front of it and save energy when there's no need of interaction.
* **SIMPLENESS**: minimizing the steps and the efforts required by the actors to have the worke done.

Let's now focus on the unlock procedure assuming we already sent a shopping order and the store accepted it:

1. The carrier sees he's got an order to collect from the store after it's been accepted and prepared. The carrier just have to scan the QR code on the box prepared to communicate to the system he has took charge of it.
2. The carrier gets to the locker and after he chose the right order in the app, he first scans the QR on the OLED display, than on the box. If it's all right, a compartment opens and he can deposit it. The deposit is done after closing the door.
3. The customer sees he has an order to collect, gets to the locker and after selecting the order on the app he can scan the QR on the display. If he's in front of the right locker, a door opens and the customer now has 3 minutes to collect his order because the collection procedure ends after he closes the door.

####Going deep...

Since it was not in the objectives of the course to implement anything on the server side except a database with Firebase Realtime Database, the interaction is totally managed between the Arduino and the app.

But it's all about the QR: the ESP shows a dynamic QR that contains a prefix – the reference of the locker – and a 4 digit password (OTP) – randomly generated by the ESP and communicated to the database – than keeps listening to Firebase in order to starts events. The QR is then used by the app to verify the presence of the user in front of the locker and start the interaction.

The steps described previously can be detailed as follow:

1. When the carrier's app scans the QR on the order, it changes the order state on the database. Now the other actors know the parcel has been carried by the carrier.
2. When the carrier gets to the locker and scans the QR code that is used to access the locker via Firebase and make it unavailable for other users if the carrier is supposed to deposit an order, otherwise nothing happens. Then the carrier is asked to scan the parcel's QR and a door opens. After the deposit is done, the locker is automatically freed and the user can see that the order is now available for collection.
3. When the customer scans the QR, the app works similarly as in the previous step: it uses it to connect to the locker via Firebase, checks if the target order matches the one on the database, and if so it writes on the database to open the door. While the door is open, the app il blocked in a collection mode screen made with a timer and some messages: this was made with the aim to encourage the customer to close correctly the door because only when it's closed the app is available again.

##About us
The group was made up by (in alphabetical order)

* Alberto Nicco – Social Researcher ([GitHub](https://github.com/albertonicco))
* Arianna Ferraris – Project Manager ([GitHub](https://github.com/ariannaferraris))
* Lorenzo Dell'Anna – Graphic Designer ([GitHub](https://github.com/Static65))
* Mattia Scagliola – Maker ([GitHub](https://github.com/JoJoJoJoestar))
* Simone Siragusa – Software Developer ([GitHub](https://github.com/srgsmn), [LinkedIn](www.linkedin.com/in/simone-siragusa))


Thank you for reading.
