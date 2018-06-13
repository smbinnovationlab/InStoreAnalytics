sap.ui.define([
    "./BaseTabContent",
    "sap/ui/model/json/JSONModel",
    "../js/Utility"
], function(BaseTabContent, JSONModel, Utility) {
    'use strict';
    
    return BaseTabContent.extend("sap.sme.ilab.shopAna.controller.Visits", {
        onInit: function() {
            //change duration donut chart properties
            var oDurationChart = this.getView().byId(sap.ui.core.Fragment.createId("durationChart", "donutChart"));
            oDurationChart.bindProperty("busy", {
                path: "/dataLoading"
            });
            oDurationChart.setHeight(utility.smallChartHeight);
            utility.initDonutChartProperties(oDurationChart);

            //change number bar chart properties
            var oVisitorVolumnColumnChart = this.getView().byId(sap.ui.core.Fragment.createId("visitorVolumn", "columnChart"));
            utility.initColumnChartProperties(oVisitorVolumnColumnChart, null, "Time", "Volume");
            oVisitorVolumnColumnChart.bindProperty("visible", {
                path: "/selectedKey",
                formatter: function(value) {
                    return value === "day";
                }
            });
            oVisitorVolumnColumnChart.bindProperty("busy", {
                path: "/dataLoading"
            });
            oVisitorVolumnColumnChart.setHeight(utility.smallChartHeight);

            //change number density chart properties
            var oDensityChart = this.getView().byId(sap.ui.core.Fragment.createId("numberDensityChart", "densityChart"));
            utility.initDensityChartProperties(oDensityChart);
            oDensityChart.setVizScales([{
                feed: "color",
                startColor: "#8CD3FF",
                endColor: "#5BBAE6",
                nullColor: "#e2e2e2"
            }]);
            oDensityChart.bindProperty("visible", {
                path: "/selectedKey",
                formatter: function(value) {
                    return value === "week";
                }
            });
            oDensityChart.bindProperty("busy", {
                path: "/dataLoading"
            });
            oDensityChart.setHeight(utility.smallChartHeight);

            //change number line chart properties
            var oNumberLineChart = this.getView().byId(sap.ui.core.Fragment.createId("numberLineChart", "lineChart"));
            utility.initLineChartProperties(oNumberLineChart, null, false, "Time", "Volume");
            oNumberLineChart.bindProperty("visible", {
                path: "/selectedKey",
                formatter: function(value) {
                    return value === "month" || value === "calendar";
                }
            });
            oNumberLineChart.bindProperty("busy", {
                path: "/dataLoading"
            });
            oNumberLineChart.setHeight(utility.smallChartHeight);

            //change bounce rate chart properties
            var oBounceRateChart = this.getView().byId(sap.ui.core.Fragment.createId("bounceRateChart", "pieChart"));
            utility.initPieChartProperties(oBounceRateChart);
            oBounceRateChart.setHeight(utility.smallChartHeight);
            oBounceRateChart.bindProperty("busy", {
                path: "/dataLoading"
            });

            //change visits revenue bar chart properties
            var oVisitRevenueBarChart = this.getView().byId("visitsRevenueBarChart");
            oVisitRevenueBarChart.setVizProperties({
                title: { 
                    visible: false
                },
                legend: {
                    label: {
                        style: {
                            fontSize: "14px"
                        }
                    }
                },
                categoryAxis: {
                    title: {
                        text: "Time",
                        style: {
                            fontWeight: "normal",
                            color: "#aaaaaa"
                        }
                    },
                    label: {
                        style: {
                            fontSize: "14px"
                        }
                    }
                },
                valueAxis: {
                    title: {
                        text: "Volume",
                        style: {
                            fontWeight: "normal",
                            color: "#aaaaaa"
                        }
                    },
                    axisLine: {
                        visible: true
                    },
                    label: {
                        style: {
                            fontSize: "14px"
                        }
                    }
                },
                valueAxis2: {
                    title: {
                        text: "Revenue",
                        style: {
                            fontWeight: "normal",
                            color: "#aaaaaa"
                        }
                    }
                },
                // plotArea: {
                //     colorPalette: [["#81D4FA"],, "#5899DA"]
                // },
                interaction: {
                    selectability: {
                        mode: "SINGLE"
                    }
                }
            });
            oVisitRevenueBarChart.setVizScales([
                {
                    feed: "color",
                    palette: [["#81D4FA"], ["#5899DA"]]
                }
                
            ]);
            oVisitRevenueBarChart.setHeight(utility.smallChartHeight);

            //change visits revenue line chart properties
            var oVisitRevenueLineChart = this.getView().byId("visitsRevenueLineChart");
            oVisitRevenueLineChart.setVizProperties({
                title: { 
                    visible: false
                },
                legend: {
                    label: {
                        style: {
                            fontSize: "14px"
                        }
                    }
                },
                categoryAxis: {
                    title: {
                        text: "Time",
                        style: {
                            fontWeight: "normal",
                            color: "#aaaaaa"
                        }
                    },
                    label: {
                        style: {
                            fontSize: "14px"
                        }
                    }
                },
                valueAxis: {
                    title: {
                        text: "Volume",
                        style: {
                            fontWeight: "normal",
                            color: "#aaaaaa"
                        }
                    },
                    label: {
                        style: {
                            fontSize: "14px"
                        }
                    }
                },
                valueAxis2: {
                    title: {
                        text: "Revenue",
                        style: {
                            fontWeight: "normal",
                            color: "#aaaaaa"
                        }
                    }
                },
                interaction: {
                    selectability: {
                        mode: "SINGLE"
                    }
                }
            });
            oVisitRevenueLineChart.setVizScales([
                {
                    feed: "color",
                    palette: [["#81D4FA"], ["#5899DA"]]
                }
                
            ]);
            oVisitRevenueLineChart.setHeight(utility.smallChartHeight);

            this._currentKey = "day";
            this._avatars = [];
            let oData = {
                selectValues: [
                    {
                        key: "day",
                        name: "Today"
                    },
                    {
                        key: "week",
                        name: "This Week"
                    },
                    {
                        key: "month",
                        name: "This Month"
                    }
                ],
                selectedKey: this._currentKey,
                totalVisit: 0,
                duration: [],
                number: [],
                numberDensity: [],
                dataLoading: true,
                bounceRate: [],
                visitsRevenue: [],
                avatarGroups: [],
                peopleCount: 0
            };

            let oModel = new JSONModel(oData);
            this.getView().setModel(oModel);
        },

        _bindAvatarGroups: function(avatars) {
            var num = 0;
            var items = [], avatarGroups = [];
            for (var i = 0; i < avatars.length; i++) {
                items.push(avatars[i]);
                if (i%20 === 0 && i !== 0) {
                    avatarGroups.push(items);
                    items = [];
                }
            }
            if (items.length > 0) {
                avatarGroups.push(items);
            }

            var oModel = this.getView().getModel();
            oModel.setProperty("/avatarGroups", avatarGroups);
            oModel.setProperty("/peopleCount", avatars.length);
        },

        onShow: function() {
            this.fetchData();
            let oController = this;
            this._timer = setInterval(function(){
                oController.fetchData();
            }, 3000);
        },

        onHide: function() {
            if (this._timer) {
                clearInterval(this._timer);
            }
        },

        fetchData: function() {
            
            let oController = this;
            let key = this.getView().getModel().getProperty("/selectedKey");
            let date = new Date();
            var dateRange = [date];
            if ( key === "week") {
                dateRange = utility.getWeekDays(date);
            } else if (key === "month") {
                dateRange = utility.getMonthDays(date);
            }

            var subPath;
            if (dateRange.length > 1) {
                subPath = "/range?begin=" + utility.dateToString(dateRange[0]) + "&end=" + utility.dateToString(dateRange[1]);
            } else {
                subPath = "/day?date=" + utility.dateToString(dateRange[0]);
            }

            jQuery.ajax({
                type: "GET",
                url:  utility.dataURL + "/app/ShopReport" + subPath,
                success: function(data) {
                    oController._bindData(data);
                },
                error: function(error) {
                    console.log(error);
                }
            });
            // jQuery.ajax({
            //     type: "GET",
            //     url: utility.dataURL +  "/app/ShopReport/customer/images/room",
            //     success: function(data) {
            //         var aCustomers = [];
            //         data.map(item => {
            //             aCustomers.push({
            //                 img: utility.imageURL + "/images/"+item.picId+".jpg"
            //             });
            //         })
            //         oController._bindAvatarGroups(aCustomers);
            //     },
            //     error: function(error) {
            //         console.log(error);
            //     }
            // });
        },

        _bindData: function(data) {
            let oModel = this.getView().getModel();
            let key = oModel.getProperty("/selectedKey");

            oModel.setProperty("/totalVisit", utility.formatInteger(data.general.totalNumber));
            oModel.setProperty("/duration", [{
                label: "<1min",
                value: data.general.duration1Min
            },{
                label: "<5mins",
                value: data.general.duration5Min
            },{
                label: "<15mins",
                value: data.general.duration15Min
            },{
                label: ">15mins",
                value: data.general.durationMore
            }]);

            var numberData = [];
            
            let aVisitData = data.timeVisit;
            let aRevenueData = data.revenueInfor;
            let aVisitRevenueData = [];
            aVisitData.map((item, index) => {
                var sLabel = item.time + ":00";
                if (key !== "day") {
                    sLabel = utility.strToMDStr(item.daytime);
                }
                // item.weekday = day[new Date(item.daytime).getDay()];
                numberData.push({
                    label: sLabel,
                    value: item.number
                });

                if (index < aVisitData.length) {
                    var oRevenueData = aRevenueData[index];
                    var visitLength = aVisitRevenueData.length;
                    if (visitLength > 0 && aVisitRevenueData[visitLength-1].label == sLabel)
                    {
                    	var pre = aVisitRevenueData[visitLength-1];
                    	pre.visit = pre.visit+item.number;
                    	pre.revenue = pre.revenue+oRevenueData.value/10;
                    }
                    else
                    {
	                    aVisitRevenueData.push({
	                        label: sLabel,
	                        visit: item.number,
	                        revenue: oRevenueData.value/10
	                    });
                    }
                }
            })
            oModel.setProperty("/number", numberData);
            if (key === "week") {
                var oDensityData = [];
                let days = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
                aVisitData.map(item => {
                    oDensityData.push({
                        yAxis: item.time + ":00",
                        xAxis: days[new Date(item.daytime).getDay()],
                        number: item.number
                    })
                });
                oModel.setProperty("/numberDensity", oDensityData);
            }

            oModel.setProperty("/visitsRevenue", aVisitRevenueData);
            
            if (data.bounceRate.length > 0) {
                var oBounceData = data.bounceRate[0];
                oModel.setProperty("/bounceRate", [
                    {
                        label: "Return",
                        value: oBounceData.total - oBounceData.bounce
                    }, 
                    {
                        label: "First time",
                        value: oBounceData.bounce
                    }
                ]);
            };
            
            if (key === this._currentKey) {
                oModel.setProperty("/dataLoading", false);
            }
        },

        onTimeChange: function(oEvent) {
            let oItem = oEvent.getParameters().selectedItem;
            if (oItem.getKey() !== this._currentKey) {
                this.getView().getModel().setProperty("/dataLoading", true);
            }
            this._currentKey = oItem.getKey();
        },

        onOpenCalendar: function(oEvent) {
            let oSource = oEvent.getSource();
            if (! this._calendarPopover) {
                this._calendarPopover = sap.ui.xmlfragment("sap.sme.ilab.shopAna.view.CalendarPopover", this);
                this.getView().addDependent(this._calendarPopover);
            }

            let periods = utility.getDataAvailablePeriods();
            this._calendarPopover.setModel(new JSONModel({
                minDate: periods[0],
                maxDate: periods[1]
            }));
            this._calendarPopover.openBy(oSource);
        },

        handleCalendarSelect: function(oEvent) {
            var oCalendar = oEvent.getSource();
            var aSelectedDates = oCalendar.getSelectedDates();
            var oDate;
			if (aSelectedDates.length > 0 ) {
                oDate = aSelectedDates[0].getStartDate();
                console.log(oDate);
				oDate = aSelectedDates[0].getEndDate();
				console.log(oDate);
			}
        }
    });
});