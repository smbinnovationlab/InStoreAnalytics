var di = (function () {
    var productsMap = {
        "1": {
            "name": "Giorgio Armani Air Di Gioia Eau de Parfum 50ml",
            "price": 78.89,
            "color": "Description",
            "location": "Inspired by the air and its freeing power, Air di Gioia is the new refreshing eau de parfum by Giorgio Armani."
        },
        "2": {
            "name": "Davidoff Cool Water Women Eau De Toilette 50 ml",
            "price": 43.06,
            "color": "Description",
            "location": "This Davidoff cool water is a floral fragrance that is sure to leave you awestruck with its stunning aromatic profile."
        },
        "3": {
            "name": "Valentina Eau de Parfum 50ml",
            "price": 86.94,
            "color": "Description",
            "location": "The ingredients that define the femininity and uniqueness of the Valentino woman have inspired a new perfume called Valentina."
        },
        "4": {
            "name": "Moschino Eau So Real Eau de Toilette 50ml",
            "price": 40.83,
            "color": "Description",
            "location": "A lively, joyful essence, contained in the iconic Cheap and Chic bottle, So Real wears an exclusive Moschino style dress."
        },
        "5": {
            "name": "Burberry My Burberry Blush Eau de Parfum 90ml",
            "price": 124.44,
            "color": "Description",
            "location": "Introducing My Burberry Blush, a fruity floral Eau de Parfum with a sparkling twist."
        },
        "6": {
            "name": "Chloe Eau de Parfum 50ml",
            "price": 88.06,
            "color": "Description",
            "location": "Intimate and sensual, the Maison's signature fragrance draws on the classic rose, the ultimate feminine scent, to create a modern and timeless perfume."
        },
        "7": {
            "name": "Gucci Bloom Acqua di Fiori Eau de Toilette 50ml",
            "price": 61.11,
            "color": "Description",
            "location": "Acqua Di Fiori introduces a new chapter to the world of Gucci Bloom."
        },
        "8": {
            "name": "Moschino Chic Petals Eau de Toilette 50ml",
            "price": 40.13,
            "color": "Description",
            "location": "The amazing elegance and genuine beauty of flowers expresses a new sense of lightness, coloured with joy and spontaneity."
        },
        "9": {
            "name": "Guerlain Musc Noble Eau de Parfum 125ml",
            "price": 178.89,
            "color": "Description",
            "location": "Inspired by the lands of the Orient, this creation by Maison Guerlain's perfumer, Thierry Wasser, highlights one of the most mysterious materials in Perfumery: musk."
        },
        "10": {
            "name": "Dolce & Gabbana Velvet Cypress Eau de Parfum 150ml",
            "price": 323.61,
            "color": "Description",
            "location": "The fragrance is built around a hearty dose of Cypress, reflecting its sparkling facet in an elegant fresh woody citrus fragrance."
        },
        "11": {
            "name": "Clive Christian Original Collection 1872 Feminine Eau de Parfum 50ml",
            "price": 301.67,
            "color": "Description",
            "location": "Experience the regal 1872, exotic X and glittering No.1 perfumes elegantly presented in a luxury black coffret."
        },
        "12": {
            "name": "Dolce & Gabbana Mysterious Night Eau de Parfum 100ml",
            "price": 110.56,
            "color": "Description",
            "location": "The One Mysterious Night is an exclusive edition of The One for Men, exploring the enigmatic world of the Middle East for a magic journey through its vibrant and magnetic scents."
        },
        "13": {
            "name": "Clive Christian Private Collection C Green Floral Feminine Eau de Parfum 100ml",
            "price": 511.11,
            "color": "Description",
            "location": "The Private Collection is a striking perfume that celebrate the extraordinary moments of a life's work brought to vivid, fragrant life."
        },
        "14": {
            "name": "Alexander McQueen Eau de Parfum 75ml",
            "price": 120.28,
            "color": "Description",
            "location": "McQueen Eau de Parfum, takes a vintage spirit and gives it a stunningly modern architecture."
        },
        "15": {
            "name": "Parfums de Marly Godolphin Eau de Parfum 75ml",
            "price": 213,
            "color": "Description",
            "location": "The Godolphin Arabian, also known as the Godolphin Barb, was an Arabian horse who was one of three stallions that were the founders of the modern Thoroughbred horse racing."
        },
        "16": {
            "name": "Carolina Herrera CH Confidential Aqua Rose Cruise Eau De Toilette 100ml",
            "price": 147.22,
            "color": "Description",
            "location": "Such as the journey of the coast in which he descends on a trail of dewy flowers, embodies the fragrance of Rose Cruise, the queen of flowers, roses, a symbol of elegance."
        },
        "17": {
            "name": "Dunhill Racing Eau de Parfum 100ml",
            "price": 94.17,
            "color": "Description",
            "location": "The thrill of the open road. The adventure."
        },
        "18": {
            "name": "Diesel Bad Intense Eau de Parfum 50ml",
            "price": 50.2,
            "color": "Description",
            "location": "Diesel decodes the bad boy’s secrets of seduction and reveals his ultimate mystery : Diesel BAD."
        },
        "19": {
            "name": "Calvin Klein CK One Eau de Toilette 100ml",
            "price": 45,
            "color": "Description",
            "location": "A classic fragrance which can be worn day or night, CK1 should be an essential part of your perfume collection."
        },
        "20": {
            "name": "Bvlgari Man In Black Eau de Parfum 60ml",
            "price": 67.78,
            "color": "Description",
            "location": "The new BVLGARI MAN IN BLACK is a daringly charismatic fragrance expressing a new statement of masculinity."
        },
        "21": {
            "name": "Ferrari Scuderia Forte Eau de Parfum 125ml",
            "price": 55.28,
            "color": "Description",
            "location": "A strong fragrance with the audacious character of a successful man."
        },
        "22": {
            "name": "Roberto Cavalli Uomo Silver Eau de Toilette 60ml",
            "price": 72.78,
            "color": "Description",
            "location": "Infused with a sense of chic and rock and roll attitude, Roberto Cavalli Uomo unveiled a new facet of the glamorous Italian fashion house."
        },
        "23": {
            "name": "Emporio Armani Stronger With You Eau de Toilette 50ml",
            "price": 64.72,
            "color": "Description",
            "location": "The new Emporio fragrances perfectly celebrate the power of love!"
        },
        "24": {
            "name": "Trussardi Riflesso Eau de Toilette 50ml  ",
            "price": 70.23,
            "color": "Description",
            "location": "Trussardi Riflesso represents the man who never rests on his laurels and transforms movement and speed into positive energy."
        },
        "25": {
            "name": "Amouage Interlude Man Eau de Parfum 50ml",
            "price": 177.78,
            "color": "Description",
            "location": "The Interlude Man scent is an interpretation of the disorder of life translated at a more intimate level."
        },
        "26": {
            "name": "Guerlain Oud Essentiel Eau de Parfum 125ml",
            "price": 178.89,
            "color": "Description",
            "location": "The fragrance is a majestic celebration of this rare ingredient which reveals its true strength and sensuality."
        },
        "27": {
            "name": "KILIAN MUSK OUD EDP 50ML",
            "price": 383.06,
            "color": "Description",
            "location": " Lemon, mandarin, cardamom, coriander, cypress, Bulgarian Rose, geranium, davana, Rum extract, frankincense."
        },
        "28": {
            "name": "Parfums de Marly Byerley Eau de Parfum 125ml",
            "price": 213.89,
            "color": "Description",
            "location": "The heavenly mixture of notes brings out a rich, elegant and long-lasting signature."
        },
        "29": {
            "name": "Dolce & Gabbana Velvet Collection Wood Eau de Parfum 50ml",
            "price": 207.22,
            "color": "Description",
            "location": "Inspired by lacquered wood, it is a rich ebony wood, attractive benzoin and refined black leather."
        },
        "30": {
            "name": "Giorgio Armani Eau Pour Homme Eau De Toilette 100 ml",
            "price": 110.23,
            "color": "Description",
            "location": "The intense aroma comes to an end with a woody aroma of cedar and patchouli and vetiver."
        },
        "31": {
            "name": "Amouage Journey Man Eau de Parfum 100ml",
            "price": 204.17,
            "color": "Description",
            "location": "This spicy and woody fragrance reveals a chiaroscuro of luminosity with a trust of intrigue."
        },
        "32": {
            "name": "Ermenegildo Zegna Essenze Indonesian Oud 125ml",
            "price": 212.78,
            "color": "Description",
            "location": "The legendary pearl of the forest. ceremonial, ancient and prized."
        },
        "33": {
            "name": "Guerlain L'Homme Ideal Sport Eau de Toilette 100 ml",
            "price": 102.22,
            "color": "Description",
            "location": "A heavy and decidedly masculine cap with guilloché detail borrowed from the world of watch-making."
        },
        "34": {
            "name": "Diptyque 34 Boulevard Saint-Germain Eau de Toilette 50ml",
            "price": 96.39,
            "color": "Description",
            "location": "Woody, Raw materials: Amber/Patchouli Accord, Rose, Cinnamon, Olfactory accident: Blackcurrant Buds."
        },
        "35": {
            "name": "Hugo Boss Scent For Him Intense Eau de Parfum 100ml",
            "price": 93.33,
            "color": "Description",
            "location": "Spicy notes of Ginger and Cardamom, exotic Maninka fruit."
        },
        "36": {
            "name": "Kenzo World Eau de Parfum 75ml",
            "price": 85,
            "color": "Description",
            "location": "Kenzo entrusted the composition of its new fragrance to the exceptional perfumer Francis Kurkdjian."
        },
        "37": {
            "name": "Cartier L'Envol Eau De Toilette 80ml",
            "price": 94.44,
            "color": "Description",
            "location": "To break away from any known sensations, that is the power of the masculine perfume L’Envol de Cartier."
        },
        "38": {
            "name": "24 Elixir Neroli Eau de Toilette 100ml",
            "price": 97.22,
            "color": "Description",
            "location": "It was shaded by a majestic fig tree that was putting the ultimate touch to this unforgettable scented picture."
        },
        "39": {
            "name": "Alexandre J Morning Muscs 100ml",
            "price": 94.44,
            "color": "Description",
            "location": "Top notes are mandarin orange, grapefruit, peach and toffee Middle notes are damask rose, violet."
        },
        "40": {
            "name": "Diptyque Eau de Toilette Oyédo 100ml",
            "price": 102.1,
            "color": "Description",
            "location": "A name with the sharp zing of citrus zest and the minimalistic grace of an antique Japanese garden."
        }
    };

    var rules = {
        male: [
            {
                minAge: 0,
                maxAge: 29,
                mood: {
                    frown: {
                        special: [],
                        normal: ["17", "18"]
                    },
                    neutral: {
                        special: [],
                        normal: ["19", "20"]
                    },
                    happy: {
                        special: [],
                        normal: ["21", "22"]
                    },
                    veryHappy: {
                        special: [],
                        normal: ["23", "24"]
                    }
                }

            },
            {
                minAge: 30,
                maxAge: 1000,
                mood: {
                    frown: {
                        special: [],
                        normal: ["25", "26"]
                    },
                    neutral: {
                        special: [],
                        normal: ["27", "28"]
                    },
                    happy: {
                        special: [],
                        normal: ["29", "30"]
                    },
                    veryHappy: {
                        special: [],
                        normal: ["31", "32"]
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
                        special: [],
                        normal: ["1", "2"]
                    },
                    neutral: {
                        special: [],
                        normal: ["3", "4"]
                    },
                    happy: {
                        special: [],
                        normal: ["5", "6"]
                    },
                    veryHappy: {
                        special: [],
                        normal: ["7", "8"]
                    }
                }

            },
            {
                minAge: 30,
                maxAge: 1000,
                mood: {
                    frown: {
                        special: [],
                        normal: ["9", "10"]
                    },
                    neutral: {
                        special: [],
                        normal: ["11", "12"]
                    },
                    happy: {
                        special: [],
                        normal: ["13", "14"]
                    },
                    veryHappy: {
                        special: [],
                        normal: ["15", "16"]
                    }
                }

            }
        ]
    };

    var randomPerfume = [
        "33", "34", "35", "36", "37", "38", "39", "40"
    ];

    getRecommendations = function (gender, age, mood, visitCount) {
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
            var oRule = aRange[i];
            if (age >= oRule.minAge && age <= oRule.maxAge) {
                oRule = oRule.mood[mood];
                aRecommends = oRule.normal;
                var leftNum = 3 - aRecommends.length;
                if (leftNum > 0 && leftNum <= randomPerfume.length) {
                    var shuffled = randomPerfume.sort(() => .5 - Math.random());
                    var selected = shuffled.slice(0, leftNum);
                    aRecommends = [...aRecommends, ...selected];
                }
                break;
            }
        }

        var aProducts = [];
        aRecommends.map(recom => {
            var product = productsMap[recom];
            if (product) {
                aProducts.push({
                    ...product, ...{
                        id: recom
                    }
                });
            }
        });
        return aProducts;
    }

    return {
        getRecommendations: getRecommendations
    };
})();