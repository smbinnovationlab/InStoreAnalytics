sap.ui.define([
    'sap/ui/core/mvc/Controller',
], function(Controller) {
    'use strict';
    
    return Controller.extend("sap.sme.ilab.shopAna.controller.BaseTabContent", {

        onShow: function() {
            console.log("base show");
        },

        onHide: function() {
            console.log("base hide");
        }
    })
});