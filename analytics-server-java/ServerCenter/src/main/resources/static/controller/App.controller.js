sap.ui.define([
    'sap/ui/core/mvc/Controller', 
    'sap/ui/model/json/JSONModel',
	'sap/m/TabContainerItem',
	"sap/ui/core/Fragment",
	"../js/Utility",
],  function (Controller, JSONModel, TabContainerItem, Fragment, Utility) {
		"use strict";

		return Controller.extend("sap.sme.ilab.shopAna.controller.App", {
			onInit: function () {
				var oModel = new JSONModel({
					notificationVisible: false,
					notifications: [
						{
							title: "VIP",
							description: "One of your VIP Gold Customers has just arrived. Say hi to Luis M.",
							datetime: "Just Now",
							priority: "High",
							unread: false
						},
						{
							title: "VIP",
							description: "One of your VIP Gold Customers has just arrived. Say hi to Luis M.",
							datetime: "1 Day",
							priority: "Medium",
							unread: false
						},
						{
							title: "VIP",
							description: "One of your VIP Gold Customers has just arrived. Say hi to Luis M.",
							datetime: "2 Days",
							priority: "High",
							unread: false
						}
					]
				});
				this.getView().setModel(oModel);

				var oMask = this.getView().byId("mask");
				oMask.attachBrowserEvent("click", this.onClickMask);
				
				var oEventBus = sap.ui.getCore().getEventBus();
				oEventBus.subscribe(utility.channel.PeopleView, utility.event.ToggleMask, this.toggleMask, this);

				var oImageModel = new JSONModel({
					path: jQuery.sap.getModulePath("sap.sme.ilab.shopAna")
				});
				this.getView().setModel(oImageModel, "imageModel");
			},
			
			onAfterRendering: function() {
				let oTab = this.getView().byId("myTabBar");
				this._tabKey =  oTab.getSelectedKey();
				let oTabController = this.getTabContentController(this._tabKey);
				oTabController.onShow();
			},

			onExit: function() {
				if (this._oNotification) {
					this._oNotification.destroy();
				}
			},

			onNotificationPress: function(oEvent) {
				var oModel = this.getView().getModel();

				var visible = oModel.getProperty("/notificationVisible");
				oModel.setProperty("/notificationVisible", !visible);
				
			},

			onTabSelect: function(oEvent) {
				if (this._tabKey) {
					this.getTabContentController(this._tabKey).onHide();
				}
				this._tabKey =  oEvent.getParameters().key;
				this.getTabContentController(this._tabKey).onShow();
			},

			getTabContentController: function(key) {
				let oTab = this.getView().byId("myTabBar");
				let items = oTab.getItems();
				var oCurrentTab;
				for (var i = 0; i < items.length; i++) {
					if (items[i].getId() === key) {
						oCurrentTab = items[i];
						break;
					}
				}

				if (oCurrentTab) {
					return oCurrentTab.getContent()[0].getController();
				} else {
					return null;
				}
			},

			toggleMask: function() {
				var oMask = this.getView().byId("mask");
				oMask.setVisible(! oMask.getVisible());
			},

			onClickMask: function() {
				var oEventBus = sap.ui.getCore().getEventBus();
				oEventBus.publish(utility.channel.AppView, utility.event.MaskClicked);
			}
		});
	});
