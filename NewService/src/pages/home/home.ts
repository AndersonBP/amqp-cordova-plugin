import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { Platform } from 'ionic-angular';

@Component({
    selector: 'page-home',
    templateUrl: 'home.html'
})
export class HomePage {

    constructor(public navCtrl: NavController, public platform: Platform) {

        platform.ready().then(() => {

            if (this.platform.is('cordova')) {

                (<any>window).Push.Start(() => {
                    this.nextNotification();
                }, (error) => { }, {});
            };
        });
    }

    nextNotification(){
        (<any>window).Push.Next((message) => {
            console.log("Message: " + message);

            this.nextNotification();
        }, (error) => {
            console.log("Error: " + error);

            this.nextNotification();
        }, {});
    }
}
