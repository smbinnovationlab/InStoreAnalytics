sap.ui.define([
    "./BaseTabContent",
    "sap/ui/model/json/JSONModel",
    "sap/m/Dialog",
    "sap/m/List",
    "sap/m/StandardListItem",
    "sap/m/Button",
    "../js/Utility",
    "../js/DI"
], function(BaseTabContent, JSONModel, Dialog, List, StandardListItem, Button, Utility, DI) {
    'use strict';
    
    return BaseTabContent.extend("sap.sme.ilab.shopAna.controller.People", {
        onInit: function() {
            let oData = {
                selectValues: [
                    {
                        key: "all",
                        name: "All"
                    },
                    {
                        key: "repeat",
                        name: "Frequent Visitors"
                    },
                    {
                        key: "vip",
                        name: "Members"
                    }
                ],
                selectedKey: "all",
                peopleCount: 3,
                customers: [
                    // {
                    //     src: jQuery.sap.getModulePath("sap.sme.ilab.shopAna", "/img/user_3.png"),
                    //     type: "anonymous",
                    //     name: "Anonymous visitor",
                    //     gender: "male",
                    //     age: 38,
                    //     mood: "happy",
                    //     info: [
                    //         {
                    //             label: "Gender",
                    //             value: "Male"
                    //         },
                    //         {
                    //             label: "Age",
                    //             // value: "🤐"
                    //             value: "38"
                    //         },
                    //         {
                    //             label: "Mood",
                    //             value: "Happy"
                    //         }
                    //     ],
                    //     visitCount: 0,
                    //     countTextVisible: false
                    // },
                    // {
                    //     src: jQuery.sap.getModulePath("sap.sme.ilab.shopAna", "/img/user_2.png"),
                    //     type: "repeat",
                    //     name: "Repeat visitor",
                    //     gender: "female",
                    //     age: 30,
                    //     mood: "neutral",
                    //     info: [
                    //         {
                    //             label: "Gender",
                    //             value: "Female"
                    //         },
                    //         {
                    //             label: "Age",
                    //             value: "🤐"
                    //         },
                    //         {
                    //             label: "Mood",
                    //             value: "Neutral"
                    //         }
                    //     ],
                    //     visitCount: 4,
                    //     countTextVisible: true,
                    //     previousVisits: [
                    //         {
                    //             time: "Dec 3rd, 12:00 (30mins), left ",
                    //             mood: "Happy ",
                    //             moodCount: "+3",
                    //             order: "Order #345 ($495)"
                    //         },
                    //         {
                    //             time: "Dec 1st, 12:00 (15mins), left ",
                    //             mood: "Very Happy ",
                    //             moodCount: "+3",
                    //             order: null
                    //         },
                    //         {
                    //             time: "Oct 3rd, 13:00 (30mins), left ",
                    //             mood: "Happy",
                    //             moodCount: "0",
                    //             order: "Order #345 ($495)"
                    //         }
                    //     ]
                    // },
                    // {
                    //     src: jQuery.sap.getModulePath("sap.sme.ilab.shopAna", "/img/user_1.png"),
                    //     type: "vip",
                    //     name: "Dan Potter",
                    //     gender: "male",
                    //     age: 50,
                    //     mood: "veryHappy",
                    //     info: [
                    //         {
                    //             label: "Level",
                    //             value: "Jeroboam VIP",
                    //             icon: jQuery.sap.getModulePath("sap.sme.ilab.shopAna", "/img/openwith.png"),
                    //         },
                    //         {
                    //             label: "Points",
                    //             value: "32,00"
                    //         },
                    //         {
                    //             label: "Anniversary",
                    //             value: "Dec 20th"
                    //         },
                    //         {
                    //             label: "Balance",
                    //             value: "$395,00"
                    //         }
                    //     ],
                    //     visitCount: 35,
                    //     countTextVisible: false,
                    //     previousVisits: [
                    //         {
                    //             time: "Dec 3rd, 12:00 (30mins), left ",
                    //             mood: "Happy ",
                    //             moodCount: "+3",
                    //             order: "Order #345 ($495)"
                    //         },
                    //         {
                    //             time: "Dec 1st, 12:00 (15mins), left ",
                    //             mood: "Very Happy ",
                    //             moodCount: "+3",
                    //             order: null
                    //         },
                    //         {
                    //             time: "Oct 3rd, 13:00 (30mins), left ",
                    //             mood: "Happy",
                    //             moodCount: "0",
                    //             order: "Order #345 ($495)"
                    //         }
                    //     ]
                    // }
                ],
                videoData: null,
                // videoData: jQuery.sap.getModulePath("sap.sme.ilab.shopAna", "/img/video.png"),
                selectedPeople: null
            };

            let oModel = new JSONModel(oData);
            this.getView().setModel(oModel);
        },

        _subscribeEvents: function() {
            var oEventBus = sap.ui.getCore().getEventBus();
            oEventBus.subscribe(utility.channel.AppView, utility.event.MaskClicked, this.onHidePeopleDialog, this);
        },

        _unsubscribeEvents: function() {
            var oEventBus = sap.ui.getCore().getEventBus();
            oEventBus.unsubscribe(utility.channel.AppView, utility.event.MaskClicked, this.onHidePeopleDialog, this);
        },

        onShow: function() {
            this._subscribeEvents();
            
            this.fetchData();
            let oController = this;
            this._timer = setInterval(function() {
                oController.fetchData();
            }, 3000);
            this._reconnect = true;
            this._attempt = 1;
            this.tryConnect();
        },
        tryConnect: function()
        {
            this._websocket = new WebSocket(utility.videoURL);
            this._websocket.onmessage = (eve) => {
                  this.getView().getModel().setProperty("/videoData", eve.data);
            }
            this._websocket.onclose = ()=> {
                
                if(this._reconnect){
                  let interv =  this.generateInterval(this._attempt);
                  this._attempt++;
                  let own = this;
                  setTimeout(()=>{own.tryConnect()},interv)
                }
            }
        },
        generateInterval: function(k)
        {
            return Math.min(30,(Math.pow(2,k)-1))*1000;
        },

        onHide: function() {
            if (this._timer) {
                clearInterval(this._timer);
            }
            if (this._websocket) {
                this._reconnect = false;
                this._websocket.close();
            }

            this._unsubscribeEvents();
        },

        fetchData: function() {
            var oController = this;
            jQuery.ajax({
                type: "GET",
                url: utility.dataURL +  "/app/ShopReport/customer/images/room",
                success: function(data) {
                    // if (oController.checkIfCusImagesChanged(data)) {
                    //     data.map(item => {
                    //         item.src = utility.imageURL + "/images/"+item.picId+".jpg";
                    //     });
                    //     var oModel = oController.getView().getModel();
                    //     oModel.setProperty("/customers",data);
                    //     oModel.setProperty("/peopleCount",data.length);
                    // }
                    var aCustomers = [];
                    data.map(item => {
                        aCustomers.push({
                            src: utility.imageURL + "/images/"+item.picId+".jpg",
                            type: item.vip ? "vip" : (item.visitCount > 1 ? "repeat" : "anonymous"),
                            countTextVisible: item.vip ? false : (item.visitCount > 3 ? true : false),
                            visitCount: item.visitCount,
                            faceId: item.faceId
                        });
                    })
                    var oModel = oController.getView().getModel();
                    oModel.setProperty("/customers",aCustomers);
                    oModel.setProperty("/peopleCount",aCustomers.length);
                },
                error: function(error) {
                    console.log(error);
                }
            });
            
            jQuery.ajax({
                type: "GET",
                url: utility.dataURL + "/app/Camera/FocusCustomer",
                success: function(faceId) {
                    if (!faceId) {
                        return;
                    }
                    var oModel = oController.getView().getModel();
                    // var oSelectedPeople = oModel.getProperty("/selectedPeople");
                    // if (!oSelectedPeople || oSelectedPeople.faceId !== faceId) {
                        // jQuery.ajax({
                        //     type: "GET",
                        //     url: utility.dataURL + "/app/Camera/CustomerSummaryInfor/"+faceId,
                        //     success: function(customerInfo) {
                        //         customerInfo.faceId = faceId;
                        //         oModel.setProperty("/selectedPeople", oController._handlePeopleData(customerInfo));
                        //         oController.showPeopleDialog();
                        //     },
                        //     error: function(err) {
                        //         console.log(err)
                        //     }
                        // });
                        oController._fetchCustomerSummaryInfo(faceId).then(function(info) {
                            if (info) {
                                oModel.setProperty("/selectedPeople", info);
                                oController.showPeopleDialog();
                            }
                        });
                    // }
                },
                error: function(error) {
                    console.log(error);
                }
            });
        },

        _fetchCustomerSummaryInfo: function(faceId) {
            var oController = this;
            return new Promise(function(resolve){
                jQuery.ajax({
                    type: "GET",
                    url: utility.dataURL + "/app/Camera/CustomerSummaryInfor/"+faceId,
                    success: function(customerInfo) {
                        customerInfo.faceId = faceId;
                        resolve(oController._handlePeopleData(customerInfo));
                    },
                    error: function(err) {
                        // console.log(err)
                        resolve(null);
                    }
                });
            });
        },

        _handlePeopleData: function(people) {
            people.src = utility.imageURL + "/images/"+ people.picId+".jpg";
            var previousVisitCount = people.visitRecord.length - 1;
            if (previousVisitCount < 0) {
                previousVisitCount = 0;
            }
            people.type = (people.isVip === 0) ? ( previousVisitCount > 0 ? "repeat" : "anonymous" ) : "vip";
            
            if (people.type === "vip") {
                var points = ["32", "56", "79", "128"];
                var balances = ["$500", "$3,500", "$17,800", "$39,500"];
                people.info = [
                    {
                        label: "Level",
                        value: "Gold VIP",
                        icon: jQuery.sap.getModulePath("sap.sme.ilab.shopAna", "/img/openwith.png"),
                    },
                    {
                        label: "Points",
                        value: points[Math.floor(Math.random() * points.length)]
                    },
                    {
                        label: "Anniversary",
                        value: "Dec 20th"
                    },
                    {
                        label: "Balance",
                        value: balances[Math.floor(Math.random() * balances.length)]
                    }
                ];
            } else {
                if (people.type === "anonymous") {
                    people.name = "Anonymous Visitor";
                } else if (people.type === "repeat") {
                    people.name = "Frequent Visitor";
                }
                people.info = [
                    {
                        label: "Gender",
                        value: people.gender === 0 ? "Female" : "Male"
                    }, 
                    {
                        label: "Age",
                        value: people.gender === 1 ? people.age : "🤐"
                    },
                    {
                        label: "Mood",
                        value: utility.getMood(people.recentEmotion)
                    }
                ];
            }
            people.visitCount = previousVisitCount;
            people.countTextVisible = (people.type !== "vip" && previousVisitCount >= 3);
            
            var aPreviousRecords = [];
            for (var i = 1; i < 4; i++) {
                if (i >= people.visitRecord.length) {
                    break;
                }
                var oVisitItem = people.visitRecord[i];
                var oNextVisitItem = people.visitRecord[i-1];
                var moodTrend = this._getMoodLevel(oNextVisitItem.emotion) - this._getMoodLevel(oVisitItem.emotion); 
                var duration = Math.floor((new Date(oVisitItem.leaveTime) - new Date(oVisitItem.enterTime))/60000);
                if (duration < 1) {
                    duration = 1; 
                }
                aPreviousRecords.push({
                    // time: "Dec 3rd, 12:00 (30mins), left ",
                    time: utility.strToDMHMStr(oVisitItem.enterTime) + " (" + duration +" mins). ",
                    mood: utility.getMood(oNextVisitItem.emotion) + " ",
                    moodCount: moodTrend > 0 ? ("+"+moodTrend) : moodTrend 
                });
            }
            people.previousVisits = aPreviousRecords;
            
            var aRecommends = di.getRecommendations(people.gender, people.age, people.recentEmotion, people.visitCount);
            var discount = (people.type === "vip") ? 0.15 : (people.countTextVisible ? 0.08 : 0);
            aRecommends.map(recom => {
                recom.oldPrice = discount === 0 ? null : recom.price;
                recom.price = recom.price * (1-discount);

                //format value
                if (recom.oldPrice) {
                    recom.oldPrice = (recom.oldPrice).toFixed(2);
                }
                recom.price = (recom.price).toFixed(2);
            });
            people.recommends = aRecommends;

            return people;
        },

        _getMoodLevel: function(mood) {
            if (mood === 3) {
                //neutral
                return 2;
            } else if (mood === 2) {
                //happy
                return 3;
            }
            return mood;
        },

        checkIfCusImagesChanged: function(data) {
            let currentImages = this.getView().getModel().getProperty("/customers");
            if (data.length !== currentImages.length) {
                return true;
            }
            let currentPicIds = [];
            currentImages.map(item => {
                currentPicIds.push(item.picId);
            });
            for (var i = 0; i < data.length; i++) {
                let picId = data[i].picId;
                if (!currentPicIds.includes(picId)) {
                    return true;
                }
            }
            return false;
        },

        showPeopleDialog: function() {
            let peopleDialog = this.getView().byId("peopleDialog");
            if (peopleDialog.getVisible()) {
                return;
            }

            //show mask
            var oEventBus = sap.ui.getCore().getEventBus();
            oEventBus.publish("PeopleView", "ToggleMask");

            peopleDialog.setVisible(true);
        },

        onShowPeopleDialog: function(oEvent) {
            var oController = this;
            let oSource = oEvent.getSource();
            let context = oSource.getBindingContext();
            var oData = context.getModel().getProperty(context.getPath());
            this._fetchCustomerSummaryInfo(oData.faceId).then(function(info){
                if (info) {
                    oController.getView().getModel().setProperty("/selectedPeople", info);
                    oController.showPeopleDialog();
                }
            });
            // let oSource = oEvent.getSource();
            // let context = oSource.getBindingContext();
            // var oData = context.getModel().getProperty(context.getPath());
            // // if (oData.vip !== true) {
            // //     //only show popover for vip
            // //     return;
            // // }

            // if (! this._peoplePopover) {
            //     this._peoplePopover = sap.ui.xmlfragment("sap.sme.ilab.shopAna.view.PeoplePopover", this);
            //     this.getView().addDependent(this._peoplePopover);
            // }

            // oData = {...oData, ...{
            //     bought: [
            //         {
            //             img: "./img/product.png",
            //             name: "High heel-red-1",
            //             price: "$78"
            //         },
            //         {
            //             img: "./img/product.png",
            //             name: "High heel-red-2",
            //             price: "$88"
            //         },
            //         {
            //             img: "./img/product.png",
            //             name: "High heel-red-3",
            //             price: "$98"
            //         }
            //     ],
            //     recommends: [
            //         {
            //             img: "./img/product.png",
            //             name: "High heel-red-1",
            //             price: "$78"
            //         },
            //         {
            //             img: "./img/product.png",
            //             name: "High heel-red-2",
            //             price: "$88"
            //         },
            //         {
            //             img: "../img/product.png",
            //             name: "High heel-red-3",
            //             price: "$98"
            //         }
            //     ],
            //     visits: [
            //         {
            //             date: "12-03",
            //             startTime: "13:00",
            //             endTime: "13:30",
            //             duration: "30 mins",
            //             arrivalMood: "Happy",
            //             departureMood: "Happy"
            //         }
            //     ]
            // }};
            // this._peoplePopover.setModel(new JSONModel(oData));
            // this._peoplePopover.openBy(oSource);
        },

        onHidePeopleDialog: function() {
            this.getView().getModel().setProperty("/selectedPeople", null);

            let peopleDialog = this.getView().byId("peopleDialog");
            peopleDialog.setVisible(false);

            //hide mask
            var oEventBus = sap.ui.getCore().getEventBus();
            oEventBus.publish(utility.channel.PeopleView, utility.event.ToggleMask);
        },

        onClickOpenImg: function(oEvent) {
            let oSource = oEvent.getSource();
            let context = oSource.getBindingContext();
            var oData = context.getModel().getProperty(context.getPath());
            var url = "/#thinclient-OITM&/Objects/OITM/Detail?id=OITM,"+oData.id;
            window.open(url, "_blank"); 
        }
    });
});