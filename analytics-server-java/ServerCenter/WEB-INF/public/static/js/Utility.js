var utility = (function (){
    getWeekDays = function(date) {
        var first = date.getDate() - date.getDay();
        var firstDay = new Date(date);
        firstDay.setDate(first);
        var lastDay = new Date(firstDay);
        lastDay.setDate(lastDay.getDate() + 6);
        return [firstDay, lastDay];
    }

    getMonthDays = function(date) {
        var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
        var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
        return [firstDay, lastDay];
    }
    
    dateToString = function(date) {
        let month = date.getMonth() + 1;
        let day = date.getDate();
        return date.getFullYear() + "-" + (month > 9 ? month : ("0"+ month)) + "-" + (day > 9 ? day : "0"+day);
    }

    strToMDStr = function(str) {
        var date = new Date(str);
        let month = date.getMonth() + 1;
        let day = date.getDate();
        return (month + "/" + day);
    }

    strToDMHMStr = function(str) {
        var date = new Date(str);
        var hour = date.getHours();
        var hourStr = hour > 9 ? hour : ("0" + hour);
        var minute = date.getMinutes();
        var minuteStr = minute > 9 ? minute : ("0" + minute);

        return [
            (tmp = date.getDate()) + 
              ([, 'st', 'nd', 'rd'][/1?.$/.exec(tmp)] || 'th'),
            [ 'January', 'February', 'March', 'April',
              'May', 'June', 'July', 'August',
              'September', 'October', 'November', 'December'
            ][date.getMonth()],
            hourStr + ":" + minuteStr
          ].join(' ')
    }

    getDataAvailablePeriods = function() {
        var date = new Date();
        var newDate = new Date(date);
        newDate.setDate(date.getDate() - 30);
        return [newDate, date];
    }

    initColumnChartProperties = function(oChart, titleTxt, categoryTitle, valueTitle) {
        var oProperties = {
            legend: {
                visible: false
            },
            valueAxis: {
                title: {
                    visible: valueTitle ? true : false,
                    text: valueTitle,
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
            categoryAxis: {
                title: {
                    visible: categoryTitle ? true : false,
                    text: categoryTitle,
                    style: {
                        fontWeight: "normal",
                        color: "#aaaaaa"
                    }
                },
                axisTick: {
                    visible: false
                },
                axisLine: {
                    visible: false
                },
                label: {
                    style: {
                        fontSize: "14px"
                    }
                }
            },
            plotArea: {
                colorPalette: ["#5899DA"]
            },
            title: {
                visible: false
            },
            interaction: {
                selectability: {
                    mode: "SINGLE"
                }
            }
        };
        if (titleTxt) {
            oProperties.title = {
                text: titleTxt,
                alignment: "left"
            }
        }

        oChart.setVizProperties(oProperties);
    }

    initPieChartProperties = function(oChart, titleTxt) {
        var oProperties = {
            legend: {
                label: {
                    style: {
                        fontSize: "14px"
                    }
                }
            },
            tooltip: {
                visible: false
            },
            title: {
                visible: false
            },
            plotArea: {
                dataLabel: {
                    visible: true,
                    distance: -1,
                    style: {
                        fontSize: "18px"
                    }
                },
                colorPalette: ["#FAC364", "#8CD3FF", "#5BBAE6", "#B7DA56"],
                radius: 0.4
            }
        };

        if (titleTxt) {
            oProperties.title = {
                text: titleTxt,
                alignment: "left"
            }
        }

        oChart.setVizProperties(oProperties);
    }

    initDonutChartProperties = function(oChart, titleTxt) {
        var oProperties = {
            legend: {
                visible: false
            },
            plotArea: {
                dataLabel: {
                    visible: true,
                    style: {
                        fontSize: 14
                    },
                    renderer: function(obj) {
                        obj.text = obj.ctx.Label;
                    }
                },
                innerRadiusRatio: 0.6,
                colorPalette: ["#FAC364", "#8CD3FF", "#5BBAE6", "#B7DA56"],
                radius: 0.4
            },
            tooltip: {
                visible: false
            },
            title: {
                visible: false
            }
        };

        if (titleTxt) {
            oProperties.title = {
                text: titleTxt,
                alignment: "left"
            }
        }

        oChart.setVizProperties(oProperties);
    }

    initDensityChartProperties = function(oChart, titleTxt) {
        var oProperties = {
            categoryAxis: {
                title: {
                    visible: false
                },
                label: {
                    style: {
                        fontSize: "14px"
                    }
                }
            },
            categoryAxis2: {
                title: {
                    visible: false,
                },
                label: {
                    style: {
                        fontSize: "14px"
                    }
                }
            },
            interaction: {
                selectability: {
                    mode: "NONE"
                }
            },
            tooltip: {
                visible: false
            },
            title: {
                visible: false
            }
        };

        if (titleTxt) {
            oProperties.title = {
                text: titleTxt,
                alignment: "left"
            }
        }

        oChart.setVizProperties(oProperties);
    }

    initLineChartProperties = function(oChart, titleTxt, smooth, categoryTitle, valueTitle) {
        var oProperties = {
            legend: {
                visible: false
            },
            tooltip: {
                visible: smooth ? false : true
            },
            categoryAxis: {
                title: {
                    visible: categoryTitle ? true : false,
                    text: categoryTitle,
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
                    visible: valueTitle ? true : false,
                    text: valueTitle,
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
            title: {
                visible: false
            },
            plotArea: {
                isSmoothed: smooth ? true : false,
                marker: {
                    visible: smooth ? false : true,
                }
            },
            interaction: {
                selectability: {
                    mode: "SINGLE"
                }
            }
        };

        if (titleTxt) {
            oProperties.title = {
                text: titleTxt,
                alignment: "left"
            }
        }
        
        oChart.setVizProperties(oProperties);
    }

    channel = {
        PeopleView: "PeopleView",
        AppView: "AppView"
    }

    event = {
        ToggleMask: "ToggleMask",
        MaskClicked: "MaskClicked"
    }

    getMood = function(index) {
        var moods = {0: 'Angry', 1:'Frown', 2:'Happy', 3:'Neutral'};
        return moods[index];
    }

    formatInteger = function(num) {
        var oIntegerFormat = sap.ui.core.format.NumberFormat.getIntegerInstance({
            style: "short",
            groupingEnabled: true,
            groupingSeparator: ",",
            shortLimit: 99999
        });
        return oIntegerFormat.format(num);
    }

    return {
        // dataURL : "https://servercenter0ad1619475.hana.ondemand.com/ServerCenter-0.0.1",
        dataURL: "http://"+location.hostname+":8080",
        imageURL : "http://"+location.hostname+":8733",
        videoURL: "ws:"+location.hostname+":50030",
        getWeekDays: getWeekDays,
        getMonthDays: getMonthDays,
        dateToString: dateToString,
        strToMDStr: strToMDStr,
        strToDMHMStr: strToDMHMStr,
        getDataAvailablePeriods: getDataAvailablePeriods,
        initColumnChartProperties: initColumnChartProperties,
        initPieChartProperties: initPieChartProperties,
        initDonutChartProperties: initDonutChartProperties,
        initDensityChartProperties: initDensityChartProperties,
        initLineChartProperties: initLineChartProperties,
        smallChartHeight: "20rem",
        channel: channel,
        event: event,
        getMood: getMood,
        formatInteger: formatInteger
    }
})();