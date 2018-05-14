sap.ui.define([
    "./BaseTabContent",
    'sap/ui/model/json/JSONModel',
    "../js/Utility"
], function(BaseTabContent, JSONModel, Utility) {
    'use strict';
    
    return BaseTabContent.extend("sap.sme.ilab.shopAna.controller.Persona", {
        onInit: function() {
            //change return pie chart properties
            var oReturnPieChart = this.getView().byId(sap.ui.core.Fragment.createId("returnRateChart", "pieChart"));
            utility.initPieChartProperties(oReturnPieChart);
            oReturnPieChart.setHeight(utility.smallChartHeight);
            oReturnPieChart.bindProperty("busy", {
                path: "/dataLoading"
            });

            //change return frequency column chart properties
            var oReturnFrequencyColumnChart = this.getView().byId(sap.ui.core.Fragment.createId("returnFrequencyColumn", "columnChart"));
            utility.initColumnChartProperties(oReturnFrequencyColumnChart);
            oReturnFrequencyColumnChart.setHeight(utility.smallChartHeight);
            oReturnFrequencyColumnChart.bindProperty("visible", {
                path: "/selectedKey",
                formatter: function(value) {
                    return value === "day" || value === "week";
                }
            });
            oReturnFrequencyColumnChart.bindProperty("busy", {
                path: "/dataLoading"
            });

            //change return frequency line chart properties
            var oReturnFrequencyLineChart = this.getView().byId(sap.ui.core.Fragment.createId("returnFrequencyLine", "lineChart"));
            utility.initLineChartProperties(oReturnFrequencyLineChart);
            oReturnFrequencyLineChart.setHeight(utility.smallChartHeight);
            oReturnFrequencyLineChart.bindProperty("visible", {
                path: "/selectedKey",
                formatter: function(value) {
                    return value === "month" || value === "calendar";
                }
            });
            oReturnFrequencyLineChart.bindProperty("busy", {
                path: "/dataLoading"
            });

            //change conversion column chart properties
            var oConversionColumnChart = this.getView().byId(sap.ui.core.Fragment.createId("conversionChart", "barChart"));
            utility.initColumnChartProperties(oConversionColumnChart);
            oConversionColumnChart.setVizScales([{
                feed: "color",
                palette: ["#81D4FA"]
            }]);
            oConversionColumnChart.setHeight(utility.smallChartHeight);
            oConversionColumnChart.bindProperty("busy", {
                path: "/dataLoading"
            });

            //change demographics line chart properties
            var oDemographicsLineChart = this.getView().byId(sap.ui.core.Fragment.createId("demographicsChart", "lineChart"));
            utility.initLineChartProperties(oDemographicsLineChart, null, true, "Age", "Volume");
            oDemographicsLineChart.setHeight(utility.smallChartHeight);
            oDemographicsLineChart.bindProperty("busy", {
                path: "/dataLoading"
            });

            //sentiment colot palette [frown, veryhappy, happy, neutral]
            var sentimentColorPalette = ["#2B5CC2", "#9ECDFE", "#E97680", "#ED1B24"];
            //change sentiment bubble chart properties
            var oSentimentChart = this.getView().byId("sentimentChart");
            oSentimentChart.setVizProperties({
                title: {
                    visible: false
                },
                legend: {
                    visible: false
                },
                sizeLegend: {
                    visible: false
                },
                valueAxis: {
                    visible: false
                },
                valueAxis2: {
                    visible: false
                },
                plotArea: {
                    dataLabel: {
                        visible: true,
                        type: "color",
                        renderer: function(obj) {
                            var labels = obj.ctx.Label.split(" ");
                            var node = document.createElementNS('http://www.w3.org/2000/svg', "g");
                            var indexY = 0;
                            labels.map(label => {
                                var text = document.createElementNS('http://www.w3.org/2000/svg', "text");
                                text.textContent = label;
                                // text.setAttribute('fill', 'white');
                                // text.setAttribute('font-size')
                                text.setAttribute('style', 'fill:black;font-size:14;');
                                text.setAttribute('y', indexY+=12);
                                node.appendChild(text);
                            });
                            return node;
                        }
                    },
                    gridline: {
                        visible: false
                    },
                    markerRenderer: function(obj) {
                        obj.graphic.width = obj.graphic.width * 3;
                        obj.graphic.height = obj.graphic.height * 3;
                    },
                    colorPalette: sentimentColorPalette
                },
                tooltip: {
                    visible: false
                }
            });
            oSentimentChart.setHeight(utility.smallChartHeight);

            //change sentiment trend line chart properties
            var oSentimentTrendLineChart = this.getView().byId("sentimentTrendLineChart");
            utility.initLineChartProperties(oSentimentTrendLineChart, null, true, "Time", "Volume");
            oSentimentTrendLineChart.setHeight(utility.smallChartHeight);
            oSentimentTrendLineChart.setVizProperties({
                plotArea: {
                    colorPalette: sentimentColorPalette
                }
            });

            //change gender female line chart properties
            var oGenderChart = this.getView().byId("genderChart");
            oGenderChart.setVizProperties({
                title: {
                    visible: false
                },
                legend: {
                    visible: false
                },
                interaction: {
                    selectability: {
                        mode: "NONE"
                    }
                },
                categoryAxis: {
                    title: {
                        text: "Age",
                        style: {
                            fontWeight: "normal",
                            color: "#aaaaaa"
                        }
                    },
                    axisLine: {
                        visible: false
                    },
                    axisTick: {
                        visible: false
                    },
                    label: {
                       alignment: "top",
                       style: {
                           fontSize: "14px"
                       }
                    },
                    labelRenderer: function(obj) {
                        let label = obj.ctx.Label;
                        if (label === 10 || label === 20 || label === 30 || label === 40 || label === 50 || label === 60) {
                            obj.text = label;
                        } else {
                            obj.text = "";
                        }
                    }
                },
                valueAxis: {
                    visible: false
                },
                valueAxis2: {
                    visible: false
                },
                plotArea: {
                    line: {
                        isSmoothed: true,
                        marker: {
                            visible: false
                        }
                    },
                    gridline: {
                        visible: false,
                        zeroLine: {
                            highlight: false
                        }
                    },
                    secondaryValuesColorPalette: ['rgba(237,27,36,1)', 'rgba(0,173,239,1)'],
                    referenceLine: {
                        line: {
                            valueAxis2: [{
                                value: 0,
                                color: "#000000",
                                type: "solid",
                                size: 0.5
                            }]
                        }
                    }
                },
                tooltip: {
                    visible: false
                }
            });

            this._currentKey = "day";
            let oData = {
                femalePercent: 0,
                malePercent: 0,
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
                sentimentData: [],
                genderData: [],
                // returnData: [
                //     {
                //         label: 0,
                //         value: 30
                //     },
                //     {
                //         label: 1,
                //         value: 32
                //     },
                //     {
                //         label: 2,
                //         value: 29
                //     },
                //     {
                //         label: 3,
                //         value: 34
                //     },
                //     {
                //         label: 4,
                //         value: 31
                //     },
                //     {
                //         label: 5,
                //         value: 32
                //     },
                //     {
                //         label: 6,
                //         value: 36
                //     }
                // ],
                dataLoading: true,
                returnVsNew: [],
                returnFrequency: [],
                conversionData: [],
                demographicsData: [],
                sentimentTrendData: [],
                genderData: []
            };

            let oModel = new JSONModel(oData);
            this.getView().setModel(oModel);
        },

        onShow: function() {
            this.fetchData();
            let oController = this;
            this._timer = setInterval(function() {
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
        },

        _bindData: function(data) {
            let oModel = this.getView().getModel();
            let key = oModel.getProperty("/selectedKey");

            var aReturnRate = data.returnRate;
            if (aReturnRate.length > 0) {
                oModel.setProperty("/returnVsNew", [
                    {
                        label: "Return",
                        value: aReturnRate[0].returnVisit
                    }, 
                    {
                        label: "New",
                        value: aReturnRate[0].newVisit
                    }
                ]);
            }

            var showTimeLabel = false;
            if (key === "day") {
                showTimeLabel = true;
            }
            var aReturnFrequency = [
                {
                    label: "0-24hrs",
                    value: 50
                },
                {
                    label: "1-7 Days",
                    value: 188
                },
                {
                    label: "1-4 Weeks",
                    value: 98
                },
                {
                    label: "4+ Weeks",
                    value: 38
                }
            ];
            // data.returnFrequency.map(item => {
            //     aReturnFrequency.push({
            //         label: showTimeLabel ? item.time + ":00" : utility.strToMDStr(item.daytime),
            //         value: item.returnVisit
            //     });
            // });
            oModel.setProperty("/returnFrequency", aReturnFrequency);

            var aConversionData = [
                {
                    label: "First time visitors",
                    value: 238
                },
                {
                    label: "Returning visitors",
                    value: 49
                },
                {
                    label: "Members",
                    value: 96
                }
            ];
            // data.conversionPipline.map(item => {
            //     aConversionData.push({
            //         label: showTimeLabel ? item.time + ":00" : utility.strToMDStr(item.daytime),
            //         value: item.value
            //     })
            // });
            oModel.setProperty("/conversionData", aConversionData);

            oModel.setProperty("/femalePercent", data.general.femalePercentage);
            oModel.setProperty("/malePercent", data.general.malePercentage);
            oModel.setProperty("/genderData", this._handleAgeData(data.ageRange));

            var aDemographicsData = [];
            data.ageRange.map(item => {
                if (item.age > 0) {
                    aDemographicsData.push({
                        label: item.age,
                        value: item.femaleNumber + item.maleNumber
                    });
                }
            });
            oModel.setProperty("/demographicsData", aDemographicsData);

            oModel.setProperty("/sentimentData", [
                {
                    label: "FROWN " + data.general.frown + "%",
                    value1: 0.5,
                    value2: 2.2,
                    value3: data.general.frown
                },
                {
                    label: "Angry " + data.general.angry + "%",
                    value1: 0.4,
                    value2: 8,
                    value3: data.general.angry
                },
                {
                    label: "HAPPY " + data.general.happy + "%",
                    value1: 1,
                    value2: 6,
                    value3: data.general.happy
                },
                {
                    label: "NEUTRAL " + data.general.neutral + "%",
                    value1: 1.5,
                    value2: 3,
                    value3: data.general.neutral
                }
            ]);
                
            var aSentimentTrendLabelMaps = {};
            data.sentiment.map(item => {
                var sLabel = (key === "day") ? (item.time + ":00") : utility.strToMDStr(item.daytime);
                var oSentimentTrendItem = aSentimentTrendLabelMaps[sLabel];
                if (!oSentimentTrendItem) {
                    oSentimentTrendItem = {};
                }
                oSentimentTrendItem[utility.getMood(item.emotion)] = item.value;
                aSentimentTrendLabelMaps[sLabel] = oSentimentTrendItem;
            });
            var aSentimentTrendData = [];
            for (var itemKey in aSentimentTrendLabelMaps) {
                aSentimentTrendData.push({...{
                    time: itemKey,
                }, ...aSentimentTrendLabelMaps[itemKey]});
            }
            oModel.setProperty("/sentimentTrendData", aSentimentTrendData);

            if (key === this._currentKey) {
                oModel.setProperty("/dataLoading", false);
            }
        },

        _handleAgeData: function(data) {
            var maxValue = 0;
            let requiredAges = [10, 20, 30, 40, 50, 60];
            var ages = [];
            data.map(item => {
                ages.push(item.age);
                var value = Math.max(item.femaleNumber, item.maleNumber);
                if (value > maxValue) {
                    maxValue = value
                }
            });
            this._updateGenderChartScales(maxValue);

            requiredAges.map(num => {
                if ( ages.indexOf(num) < 0 ) {
                    data.push({
                        "age" : num,
                        "femaleNumber" : 0,
                        "maleNumber" : 0
                    });
                }
            });
            data.sort(function(a, b){
                return a.age > b.age;
            });

            let result = [];
            data.map(item => {
                result.push(...[
                    {
                        age: item.age,
                        gender: "Female",
                        number: item.femaleNumber,
                        base: 0
                    },
                    {
                        age: item.age,
                        gender: "Male",
                        number: -item.maleNumber,
                        base: 0
                    }
                ])
            });
            result.reverse();
            return result;
        },

        _updateGenderChartScales: function(maxValue) {
            maxValue *= 2;
            var oGenderChart = this.getView().byId("genderChart");
            oGenderChart.setVizScales([{
                feed: "valueAxis2",
                min: -maxValue,
                max: maxValue
            }]);
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