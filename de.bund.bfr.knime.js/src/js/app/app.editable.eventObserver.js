/**
 * The observer class maintains a list of its observers and notifies them automatically of any state changes,
 * by calling one of their methods.
 * version: 1.0.0
 * author: Ahmad Swaid 
 * date: 07.12.2020 
 */
class EventObserver {
    constructor() {
        let O = this;
        O.observers = []; //list of observed events (callback functions)
        O.registeredID = [];
    }

    subscribe(id, observer) {
        let O = this;
        //add new events
        console.log(id, observer);
        if(!O.registeredID.includes(id) ){
            O.observers.push({
                id: id,
                callback: observer
            }); //get list of observed events and push new item to array
            O.registeredID.push(id);
        }
    }

    unsubscribe(observer) {
        let O = this;
        O.observers = O.observers.filter(
            subscriber => subscriber !== observer
        ); //returns a new list with filtered entries
    }

    broadcast(event) {
        let O = this;
        _log("Sending event ", event);
        O.observers.forEach((subscriber) => {
            subscriber.callback(event)
        });
    }
}