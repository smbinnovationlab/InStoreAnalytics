var di = (function(){
    var productsMap = {
        "535-059": {
          "price": 15.75,
          "name": "A Brief History of Time",
          "color": "Author",
          "location": "Stephen Hawking",
          "notes": "A landmark volume in science writing by one of the great minds of our time"
        },
        "535-061": {
          "price": 18.99,
          "name": "A Heartbreaking Work of Staggering Genius",
          "color": "Author",
          "location": "Dave Eggers",
          "notes": "A book that redefines both family and narrative for the twenty-first century"
        },
        "535-066": {
          "price": 17.5,
          "name": "A Long Way Gone: Memoirs of a Boy Soldier",
          "color": "Author",
          "location": "Ishmael Beah",
          "notes": "This is how wars are fought now: by children, hopped-up on drugs and wielding AK-47s"
        },
        "535-013": {
          "price": 16,
          "name": "The Bad Beginning: Or, Orphans!",
          "color": "Author",
          "location": "Lemony Snicket ",
          "notes": "Are you made fainthearted by death? Does fire unnerve you? "
        },
        "535-019": {
          "price": 15,
          "name": "Selected Stories",
          "color": "Sparkling",
          "location": "Alice Munro",
          "notes": "Spanning almost thirty years and settings that range from big cities to small towns."
        },
        "535-029": {
          "price": 4.99,
          "name": "Are You There God? It's Me, Margaret",
          "color": "Author",
          "location": "Judy Blume",
          "notes": "A twelve-year-old talks to God about her ardent desire to be grown up."
        },
        "535-032": {
          "price": 4.16,
          "name": "Beloved",
          "color": "Author",
          "location": "Toni Morrison",
          "notes": "Nominated as one of America’s best-loved novels by PBS’s The Great American Read"
        },
        "535-045": {
          "price": 19,
          "name": "Cutting for Stone",
          "color": "Author",
          "location": "Abraham Verghese ",
          "notes": "Cutting for Stone is an unforgettable story of love and betrayal"
        },
        "535-053": {
          "price": 11,
          "name": "Charlie and the Chocolate Factory ",
          "color": "Author",
          "location": "Roald Dahl",
          "notes": "This special edition of Charlie and the Chocolate Factory is perfect for longtime Roald Dahl fan"
        },
        "535-017": {
          "price": 12,
          "name": "The Autobiography of Malcolm X: As Told to Alex Haley",
          "color": "Author",
          "location": "Malcolm X",
          "notes": "ONE OF TIME’S TEN MOST IMPORTANT NONFICTION BOOKS OF THE TWENTIETH CENTURY"
        },
      };

      var rules = {
          male:[
              {
                  minAge: 0,
                  maxAge: 29,
                  mood: {
                      frown: {
                        special: ["535-059"],
                        normal: ["535-061", "535-032", "535-013"]
                      },
                      neutral: {
                        special: ["535-069"],
                        normal: ["535-013", "535-019", "535-029"]
                      },
                      happy: {
                    	  special: ["535-069"],
                          normal: ["535-013", "535-019", "535-029"]
                      },
                      veryHappy: {
                    	  special: ["535-069"],
                          normal: ["535-013", "535-019", "535-029"]
                      }
                  }
                  
              },
              {
                minAge: 30,
                maxAge: 1000,
                mood: {
                    frown: {
                      special: ["535-019"],
                      normal: ["535-029", "535-060", "535-045"]
                    },
                    neutral: {
                    	 special: ["535-019"],
                         normal: ["535-029", "535-060", "535-045"]
                    },
                    happy: {
                    	 special: ["535-019"],
                         normal: ["535-029", "535-060", "535-045"]
                    },
                    veryHappy: {
                    	 special: ["535-019"],
                         normal: ["535-029", "535-060", "535-045"]
                    }
                }
                
            }
          ],
          female: [
            {
                minAge: 0,
                maxAge: 29,
                mood: {
                    frown: {
                      special: ["535-017"],
                      normal: ["535-053", "535-029", "535-013"]
                    },
                    neutral: {
                    	special: ["535-017"],
                        normal: ["535-053", "535-029", "535-013"]
                    },
                    happy: {
                    	special: ["535-017"],
                        normal: ["535-053", "535-029", "535-013"]
                    },
                    veryHappy: {
                    	special: ["535-017"],
                        normal: ["535-053", "535-029", "535-013"]
                    }
                }
                
            },
            {
              minAge: 30,
              maxAge: 1000,
              mood: {
                  frown: {
                	  special: ["535-061"],
                      normal: ["535-053", "535-029", "535-013"]
                  },
                  neutral: {
                	  special: ["535-061"],
                      normal: ["535-053", "535-029", "535-013"]
                  },
                  happy: {
                	  special: ["535-061"],
                      normal: ["535-053", "535-029", "535-013"]
                  },
                  veryHappy: {
                	  special: ["535-061"],
                      normal: ["535-053", "535-029", "535-013"]
                  }
              }
              
          }
        ]
      };

      getRecommendations = function(gender, age, mood, visitCount) {
    	  //You can add your recommendation algorithm here
    	  
        //gender : 0 - male, 1 - female
          gender = gender === 0 ? "female" : "male";
          // {0: angry, 1:frown, 2:happy, 3:neutral};
          if (mood === 0) {
            mood = "frown";
          } else if (mood === 1) {
            mood = "neutral";
          } else if (mood === 2) {
            mood = "veryHappy";
          } else {
            mood = "happy";
          }
          var aRange = rules[gender];
          var aRecommends = [];
          
          for (var i = 0; i < aRange.length; i++) {
            var oRule  = aRange[i];
            if (age >= oRule.minAge && age <= oRule.maxAge ) {
                oRule = oRule.mood[mood];
                if (visitCount > 3) {
                    aRecommends = oRule.special;
                    var leftNum = 3 - oRule.special.length;
                    if (leftNum > 0 && leftNum <= oRule.normal.length) {
                        var shuffled = oRule.normal.sort(() => .5 - Math.random());
                        var selected = shuffled.slice(0, leftNum);
                        aRecommends = [...aRecommends, ...selected];
                    }
                } else {
                    aRecommends = oRule.normal;
                }
                break;
            }
          }
          
          var aProducts = [];
          aRecommends.map(recom => {
            var product = productsMap[recom];
            if (product) {
                aProducts.push({...product, ...{
                    id: recom
                }});
            }
          });
          return aProducts;
      }

      return {
          getRecommendations: getRecommendations
      };
})();