function SwipeHandle(q, f) {
    var g = function() {};
    var z = function(H) {
        setTimeout(H || g, 0)
    };
    var G = {
        addEventListener: !!window.addEventListener,
        touch: ("ontouchstart" in window) || window.DocumentTouch && document instanceof DocumentTouch,
        transitions: (function(H) {
            var J = ["transitionProperty", "WebkitTransition", "MozTransition", "OTransition", "msTransition"];
            for (var I in J) {
                if (H.style[J[I]] !== undefined) {
                    return true
                }
            }
            return false
        })(document.createElement("swipe"))
    };
    if (!q) {
        return
    }
    var c = q.children[0];
    var w, d, v, h;
    f = f || {};
    var o = parseInt(f.startSlide, 10) || 0;
    var A = f.speed || 300;
    f.continuous = f.continuous !== undefined ? f.continuous: true;
    function r() {
        w = c.children;
        h = w.length;
        if (w.length < 2) {
            f.continuous = false
        }
        if (G.transitions && f.continuous && w.length < 3) {
            c.appendChild(w[0].cloneNode(true));
            c.appendChild(c.children[1].cloneNode(true));
            w = c.children
        }
        d = new Array(w.length);
        v = q.getBoundingClientRect().width || q.offsetWidth || 320;
        if (w.length !== 0) {
            c.style.width = (w.length * v) + "px"
        }
        var K = w.length;
        while (K--) {
            var I = w[K];
            I.style.width = v + "px";
            I.setAttribute("data-index", K);
            if (G.transitions) {
                I.style.left = (K * -v) + "px";
                u(K, o > K ? -v: (o < K ? v: 0), 0)
            }
        }
        if (f.continuous && G.transitions) {
            u(k(o - 1), -v, 0);
            u(k(o + 1), v, 0)
        }
        if (!G.transitions) {
            c.style.left = (o * -v) + "px"
        }
        q.style.visibility = "visible";
        if (f.isOpenNextAndPrevious) {
            if (q.querySelector(".imageSliderNext")) {
                return
            }
            var J = document.createElement("div");
            J.className = "nextAndPreviousIcon imageSliderNext";
            J.style.marginTop = ( - Fai.top._htmlFontSize * 1.75) / 2 + "px";
            var H = document.createElement("div");
            H.className = "nextAndPreviousIcon imageSliderPrevious";
            H.style.top = 50 + "%";
            H.style.marginTop = ( - Fai.top._htmlFontSize * 1.75) / 2 + "px";
            J.addEventListener("click",
                function() {
                    t()
                },
                false);
            H.addEventListener("click",
                function() {
                    s()
                },
                false);
            q.appendChild(J);
            q.appendChild(H)
        }
    }
    if (f.bulletsClick) {
        var e = q.querySelector(".bullets");
        if (e) {
            var y = e.querySelectorAll("li");
            for (var m = 0; m < y.length; m++) {
                y[m].onclick = (function(H) {
                    return function() {
                        a(H);
                        F += f.auto || 0
                    }
                })(m)
            }
        }
    }
    function s() {
        if (f.continuous) {
            a(o - 1)
        } else {
            if (o) {
                a(o - 1)
            }
        }
    }
    function t() {
        if (f.continuous) {
            a(o + 1)
        } else {
            if (o < w.length - 1) {
                a(o + 1)
            }
        }
    }
    function k(H) {
        return (w.length + (H % w.length)) % w.length
    }
    function a(L, I) {
        if (o == L) {
            return
        }
        if (G.transitions) {
            var K = Math.abs(o - L) / (o - L);
            if (f.continuous) {
                var H = K;
                K = -d[k(L)] / v;
                if (K !== H) {
                    L = -K * w.length + L
                }
            }
            var J = Math.abs(o - L) - 1;
            while (J--) {
                u(k((L > o ? L: o) - J - 1), v * K, 0)
            }
            L = k(L);
            u(o, v * K, I || A);
            u(L, 0, I || A);
            if (f.continuous) {
                u(k(L - K), -(v * K), 0)
            }
        } else {
            L = k(L);
            l(o * -v, L * -v, I || A)
        }
        o = L;
        z(f.callback && f.callback(o, w[o]))
    }
    function u(H, J, I) {
        p(H, J, I);
        d[H] = J
    }
    function p(I, L, K) {
        var H = w[I];
        var J = H && H.style;
        if (!J) {
            return
        }
        J.webkitTransitionDuration = J.MozTransitionDuration = J.msTransitionDuration = J.OTransitionDuration = J.transitionDuration = K + "ms";
        J.webkitTransform = "translate(" + L + "px,0)translateZ(0)";
        J.msTransform = J.MozTransform = J.OTransform = "translateX(" + L + "px)"
    }
    function l(L, K, H) {
        if (!H) {
            c.style.left = K + "px";
            return
        }
        var J = +new Date;
        var I = setInterval(function() {
                var M = +new Date - J;
                if (M > H) {
                    c.style.left = K + "px";
                    if (F) {
                        C()
                    }
                    f.transitionEnd && f.transitionEnd.call(event, o, w[o]);
                    clearInterval(I);
                    return
                }
                c.style.left = (((K - L) * (Math.floor((M / H) * 100) / 100)) + L) + "px"
            },
            4)
    }
    var F = f.auto || 0;
    var B;
    function C() {
        B = setTimeout(t, F)
    }
    function x() {
        F = 0;
        clearTimeout(B)
    }
    var i = {};
    var D = {};
    var E;
    var b = {
        handleEvent: function(H) {
            switch (H.type) {
                case "touchstart":
                    this.start(H);
                    break;
                case "touchmove":
                    this.move(H);
                    break;
                case "touchend":
                    z(this.end(H));
                    break;
                case "webkitTransitionEnd":
                case "msTransitionEnd":
                case "oTransitionEnd":
                case "otransitionend":
                case "transitionend":
                    z(this.transitionEnd(H));
                    break;
                case "resize":
                    z(r.call());
                    break
            }
            if (f.stopPropagation) {
                H.stopPropagation()
            }
        },
        start: function(H) {
            var I = H.touches[0];
            i = {
                x: I.pageX,
                y: I.pageY,
                time: +new Date
            };
            E = undefined;
            D = {};
            c.addEventListener("touchmove", this, false);
            c.addEventListener("touchend", this, false)
        },
        move: function(H) {
            if (H.touches.length > 1 || H.scale && H.scale !== 1) {
                return
            }
            if (f.disableScroll) {
                H.preventDefault()
            }
            var I = H.touches[0];
            D = {
                x: I.pageX - i.x,
                y: I.pageY - i.y
            };
            if (typeof E == "undefined") {
                E = !!(E || Math.abs(D.x) < Math.abs(D.y))
            }
            if (!E) {
                H.preventDefault();
                x();
                if (f.continuous) {
                    p(k(o - 1), D.x + d[k(o - 1)], 0);
                    p(o, D.x + d[o], 0);
                    p(k(o + 1), D.x + d[k(o + 1)], 0)
                } else {
                    D.x = D.x / ((!o && D.x > 0 || o == w.length - 1 && D.x < 0) ? (Math.abs(D.x) / v + 1) : 1);
                    p(o - 1, D.x + d[o - 1], 0);
                    p(o, D.x + d[o], 0);
                    p(o + 1, D.x + d[o + 1], 0)
                }
            }
        },
        end: function(J) {
            var L = +new Date - i.time;
            var I = Number(L) < 250 && Math.abs(D.x) > 20 || Math.abs(D.x) > v / 2;
            var H = !o && D.x > 0 || o == w.length - 1 && D.x < 0;
            if (f.continuous) {
                H = false
            }
            var K = D.x < 0;
            if (!E) {
                if (I && !H) {
                    if (K) {
                        if (f.continuous) {
                            u(k(o - 1), -v, 0);
                            u(k(o + 2), v, 0)
                        } else {
                            u(o - 1, -v, 0)
                        }
                        u(o, d[o] - v, A);
                        u(k(o + 1), d[k(o + 1)] - v, A);
                        o = k(o + 1)
                    } else {
                        if (f.continuous) {
                            u(k(o + 1), v, 0);
                            u(k(o - 2), -v, 0)
                        } else {
                            u(o + 1, v, 0)
                        }
                        u(o, d[o] + v, A);
                        u(k(o - 1), d[k(o - 1)] + v, A);
                        o = k(o - 1)
                    }
                    f.callback && f.callback(o, w[o])
                } else {
                    if (f.continuous) {
                        u(k(o - 1), -v, A);
                        u(o, 0, A);
                        u(k(o + 1), v, A)
                    } else {
                        u(o - 1, -v, A);
                        u(o, 0, A);
                        u(o + 1, v, A)
                    }
                }
            }
            c.removeEventListener("touchmove", b, false);
            c.removeEventListener("touchend", b, false)
        },
        transitionEnd: function(H) {
            if (parseInt(H.target.getAttribute("data-index"), 10) == o) {
                if (F) {
                    C()
                }
                f.transitionEnd && f.transitionEnd.call(H, o, w[o])
            }
        }
    };
    r();
    if (F) {
        C()
    }
    if (G.addEventListener) {
        if (G.touch) {
            c.addEventListener("touchstart", b, false)
        }
        if (G.transitions) {
            c.addEventListener("webkitTransitionEnd", b, false);
            c.addEventListener("msTransitionEnd", b, false);
            c.addEventListener("oTransitionEnd", b, false);
            c.addEventListener("otransitionend", b, false);
            c.addEventListener("transitionend", b, false)
        }
    } else {}
    return {
        setup: function() {
            r()
        },
        slide: function(I, H) {
            x();
            a(I, H)
        },
        prev: function() {
            x();
            s()
        },
        next: function() {
            x();
            t()
        },
        getPos: function() {
            return o
        },
        getNumSlides: function() {
            return h
        },
        kill: function() {
            x();
            c.style.width = "auto";
            c.style.left = 0;
            var I = w.length;
            while (I--) {
                var H = w[I];
                H.style.width = "100%";
                H.style.left = 0;
                if (G.transitions) {
                    p(I, 0, 0)
                }
            }
            if (G.addEventListener) {
                c.removeEventListener("touchstart", b, false);
                c.removeEventListener("webkitTransitionEnd", b, false);
                c.removeEventListener("msTransitionEnd", b, false);
                c.removeEventListener("oTransitionEnd", b, false);
                c.removeEventListener("otransitionend", b, false);
                c.removeEventListener("transitionend", b, false);
                window.removeEventListener("resize", b, false)
            } else {
                window.onresize = null
            }
        }
    }
} (function(a) {
    a.fn.swipehandle = function(b, c) {
        return new SwipeHandle(b, c)
    }
})(jm); (function(f) {
    function p(v, A) {
        var z = (v & 65535) + (A & 65535),
            w = (v >> 16) + (A >> 16) + (z >> 16);
        return (w << 16) | (z & 65535)
    }
    function t(v, w) {
        return (v << w) | (v >>> (32 - w))
    }
    function c(B, y, w, v, A, z) {
        return p(t(p(p(y, B), p(v, z)), A), w)
    }
    function b(y, w, C, B, v, A, z) {
        return c((w & C) | ((~w) & B), y, w, v, A, z)
    }
    function h(y, w, C, B, v, A, z) {
        return c((w & B) | (C & (~B)), y, w, v, A, z)
    }
    function o(y, w, C, B, v, A, z) {
        return c(w ^ C ^ B, y, w, v, A, z)
    }
    function a(y, w, C, B, v, A, z) {
        return c(C ^ (w | (~B)), y, w, v, A, z)
    }
    function d(G, B) {
        G[B >> 5] |= 128 << ((B) % 32);
        G[(((B + 64) >>> 9) << 4) + 14] = B;
        var y, A, z, w, v, F = 1732584193,
            E = -271733879,
            D = -1732584194,
            C = 271733878;
        for (y = 0; y < G.length; y += 16) {
            A = F;
            z = E;
            w = D;
            v = C;
            F = b(F, E, D, C, G[y], 7, -680876936);
            C = b(C, F, E, D, G[y + 1], 12, -389564586);
            D = b(D, C, F, E, G[y + 2], 17, 606105819);
            E = b(E, D, C, F, G[y + 3], 22, -1044525330);
            F = b(F, E, D, C, G[y + 4], 7, -176418897);
            C = b(C, F, E, D, G[y + 5], 12, 1200080426);
            D = b(D, C, F, E, G[y + 6], 17, -1473231341);
            E = b(E, D, C, F, G[y + 7], 22, -45705983);
            F = b(F, E, D, C, G[y + 8], 7, 1770035416);
            C = b(C, F, E, D, G[y + 9], 12, -1958414417);
            D = b(D, C, F, E, G[y + 10], 17, -42063);
            E = b(E, D, C, F, G[y + 11], 22, -1990404162);
            F = b(F, E, D, C, G[y + 12], 7, 1804603682);
            C = b(C, F, E, D, G[y + 13], 12, -40341101);
            D = b(D, C, F, E, G[y + 14], 17, -1502002290);
            E = b(E, D, C, F, G[y + 15], 22, 1236535329);
            F = h(F, E, D, C, G[y + 1], 5, -165796510);
            C = h(C, F, E, D, G[y + 6], 9, -1069501632);
            D = h(D, C, F, E, G[y + 11], 14, 643717713);
            E = h(E, D, C, F, G[y], 20, -373897302);
            F = h(F, E, D, C, G[y + 5], 5, -701558691);
            C = h(C, F, E, D, G[y + 10], 9, 38016083);
            D = h(D, C, F, E, G[y + 15], 14, -660478335);
            E = h(E, D, C, F, G[y + 4], 20, -405537848);
            F = h(F, E, D, C, G[y + 9], 5, 568446438);
            C = h(C, F, E, D, G[y + 14], 9, -1019803690);
            D = h(D, C, F, E, G[y + 3], 14, -187363961);
            E = h(E, D, C, F, G[y + 8], 20, 1163531501);
            F = h(F, E, D, C, G[y + 13], 5, -1444681467);
            C = h(C, F, E, D, G[y + 2], 9, -51403784);
            D = h(D, C, F, E, G[y + 7], 14, 1735328473);
            E = h(E, D, C, F, G[y + 12], 20, -1926607734);
            F = o(F, E, D, C, G[y + 5], 4, -378558);
            C = o(C, F, E, D, G[y + 8], 11, -2022574463);
            D = o(D, C, F, E, G[y + 11], 16, 1839030562);
            E = o(E, D, C, F, G[y + 14], 23, -35309556);
            F = o(F, E, D, C, G[y + 1], 4, -1530992060);
            C = o(C, F, E, D, G[y + 4], 11, 1272893353);
            D = o(D, C, F, E, G[y + 7], 16, -155497632);
            E = o(E, D, C, F, G[y + 10], 23, -1094730640);
            F = o(F, E, D, C, G[y + 13], 4, 681279174);
            C = o(C, F, E, D, G[y], 11, -358537222);
            D = o(D, C, F, E, G[y + 3], 16, -722521979);
            E = o(E, D, C, F, G[y + 6], 23, 76029189);
            F = o(F, E, D, C, G[y + 9], 4, -640364487);
            C = o(C, F, E, D, G[y + 12], 11, -421815835);
            D = o(D, C, F, E, G[y + 15], 16, 530742520);
            E = o(E, D, C, F, G[y + 2], 23, -995338651);
            F = a(F, E, D, C, G[y], 6, -198630844);
            C = a(C, F, E, D, G[y + 7], 10, 1126891415);
            D = a(D, C, F, E, G[y + 14], 15, -1416354905);
            E = a(E, D, C, F, G[y + 5], 21, -57434055);
            F = a(F, E, D, C, G[y + 12], 6, 1700485571);
            C = a(C, F, E, D, G[y + 3], 10, -1894986606);
            D = a(D, C, F, E, G[y + 10], 15, -1051523);
            E = a(E, D, C, F, G[y + 1], 21, -2054922799);
            F = a(F, E, D, C, G[y + 8], 6, 1873313359);
            C = a(C, F, E, D, G[y + 15], 10, -30611744);
            D = a(D, C, F, E, G[y + 6], 15, -1560198380);
            E = a(E, D, C, F, G[y + 13], 21, 1309151649);
            F = a(F, E, D, C, G[y + 4], 6, -145523070);
            C = a(C, F, E, D, G[y + 11], 10, -1120210379);
            D = a(D, C, F, E, G[y + 2], 15, 718787259);
            E = a(E, D, C, F, G[y + 9], 21, -343485551);
            F = p(F, A);
            E = p(E, z);
            D = p(D, w);
            C = p(C, v)
        }
        return [F, E, D, C]
    }
    function q(w) {
        var x, v = "";
        for (x = 0; x < w.length * 32; x += 8) {
            v += String.fromCharCode((w[x >> 5] >>> (x % 32)) & 255)
        }
        return v
    }
    function i(w) {
        var x, v = [];
        v[(w.length >> 2) - 1] = undefined;
        for (x = 0; x < v.length; x += 1) {
            v[x] = 0
        }
        for (x = 0; x < w.length * 8; x += 8) {
            v[x >> 5] |= (w.charCodeAt(x / 8) & 255) << (x % 32)
        }
        return v
    }
    function k(v) {
        return q(d(i(v), v.length * 8))
    }
    function e(x, A) {
        var w, z = i(x),
            v = [],
            y = [],
            B;
        v[15] = y[15] = undefined;
        if (z.length > 16) {
            z = d(z, x.length * 8)
        }
        for (w = 0; w < 16; w += 1) {
            v[w] = z[w] ^ 909522486;
            y[w] = z[w] ^ 1549556828
        }
        B = d(v.concat(i(A)), 512 + A.length * 8);
        return q(d(y.concat(B), 512 + 128))
    }
    function u(y) {
        var A = "0123456789abcdef",
            w = "",
            v, z;
        for (z = 0; z < y.length; z += 1) {
            v = y.charCodeAt(z);
            w += A.charAt((v >>> 4) & 15) + A.charAt(v & 15)
        }
        return w
    }
    function m(v) {
        return unescape(encodeURIComponent(v))
    }
    function r(v) {
        return k(m(v))
    }
    function l(v) {
        return u(r(v))
    }
    function g(v, w) {
        return e(m(v), m(w))
    }
    function s(v, w) {
        return u(g(v, w))
    }
    f.md5 = function(w, x, v) {
        if (!x) {
            if (!v) {
                return l(w)
            } else {
                return r(w)
            }
        }
        if (!v) {
            return s(x, w)
        } else {
            return g(x, w)
        }
    }
})(jm); (function(f) {
    var a = [];
    var b = function(k) {
        if (!k.jqmCSS3AnimateId) {
            k.jqmCSS3AnimateId = f.uuid()
        }
        return k.jqmCSS3AnimateId
    };
    var d = function(k) {
        if (typeof k == "string" || k instanceof String) {
            return document.getElementById(k)
        } else {
            if (f.is$(k)) {
                return k[0]
            } else {
                return k
            }
        }
    };
    var i = function(o, k) {
        var l, p, m = d(o);
        p = b(m);
        if (a[p]) {
            a[p].animate(k);
            l = a[p]
        } else {
            l = g(m, k);
            a[p] = l
        }
        return l
    };
    f.fn.css3Animate = function(m) {
        if (!m.complete && m.callback) {
            m.complete = m.callback
        }
        var l = i(this[0], m);
        m.complete = null;
        m.sucess = null;
        m.failure = null;
        for (var k = 1; k < this.length; k++) {
            l.link(this[k], m)
        }
        return l
    };
    f.css3AnimateQueue = function() {
        return new g.queue()
    };
    var h = f.feat.cssTransformStart;
    var e = f.feat.cssTransformEnd;
    var c = f.feat.cssPrefix.replace(/-/g, "") + "TransitionEnd";
    c = (f.os.fennec || f.feat.cssPrefix == "" || f.os.ie) ? "transitionend": c;
    c = c.replace(c.charAt(0), c.charAt(0).toLowerCase());
    var g = (function() {
        var k = function(l, m) {
            if (! (this instanceof k)) {
                return new k(l, m)
            }
            this.callbacksStack = [];
            this.activeEvent = null;
            this.countStack = 0;
            this.isActive = false;
            this.el = l;
            this.linkFinishedProxy_ = f.proxy(this.linkFinished, this);
            if (!this.el) {
                return
            }
            this.animate(m);
            var o = this;
            jm(this.el).bind("destroy",
                function() {
                    var p = o.el.jqmCSS3AnimateId;
                    o.callbacksStack = [];
                    if (a[p]) {
                        delete a[p]
                    }
                })
        };
        k.prototype = {
            animate: function(w) {
                if (this.isActive) {
                    this.cancel()
                }
                this.isActive = true;
                if (!w) {
                    alert("Please provide configuration options for animation of " + this.el.id);
                    return
                }
                var m = !!w.addClass;
                if (m) {
                    if (w.removeClass) {
                        jm(this.el).replaceClass(w.removeClass, w.addClass)
                    } else {
                        jm(this.el).addClass(w.addClass)
                    }
                } else {
                    var u = numOnly(w.time);
                    if (u == 0) {
                        w.time = 0
                    }
                    if (!w.y) {
                        w.y = 0
                    }
                    if (!w.x) {
                        w.x = 0
                    }
                    if (w.previous) {
                        var r = new f.getCssMatrix(this.el);
                        w.y += numOnly(r.f);
                        w.x += numOnly(r.e)
                    }
                    if (!w.origin) {
                        w.origin = "0% 0%"
                    }
                    if (!w.scale) {
                        w.scale = "1"
                    }
                    if (!w.rotateY) {
                        w.rotateY = "0"
                    }
                    if (!w.rotateX) {
                        w.rotateX = "0"
                    }
                    if (!w.skewY) {
                        w.skewY = "0"
                    }
                    if (!w.skewX) {
                        w.skewX = "0"
                    }
                    if (!w.timingFunction) {
                        w.timingFunction = "linear"
                    }
                    if (typeof(w.x) == "number" || (w.x.indexOf("%") == -1 && w.x.toLowerCase().indexOf("px") == -1 && w.x.toLowerCase().indexOf("deg") == -1)) {
                        w.x = parseInt(w.x) + "px"
                    }
                    if (typeof(w.y) == "number" || (w.y.indexOf("%") == -1 && w.y.toLowerCase().indexOf("px") == -1 && w.y.toLowerCase().indexOf("deg") == -1)) {
                        w.y = parseInt(w.y) + "px"
                    }
                    var v = "translate" + h + (w.x) + "," + (w.y) + e + " scale(" + parseFloat(w.scale) + ") rotate(" + w.rotateX + ")";
                    if (!f.os.opera) {
                        v += " rotateY(" + w.rotateY + ")"
                    }
                    v += " skew(" + w.skewX + "," + w.skewY + ")";
                    this.el.style[f.feat.cssPrefix + "Transform"] = v;
                    this.el.style[f.feat.cssPrefix + "BackfaceVisibility"] = "hidden";
                    var t = f.feat.cssPrefix + "Transform";
                    if (w.opacity !== undefined) {
                        this.el.style.opacity = w.opacity;
                        t += ", opacity"
                    }
                    if (w.width) {
                        this.el.style.width = w.width;
                        t = "all"
                    }
                    if (w.height) {
                        this.el.style.height = w.height;
                        t = "all"
                    }
                    this.el.style[f.feat.cssPrefix + "TransitionProperty"] = "all";
                    if (("" + w.time).indexOf("s") === -1) {
                        var p = "ms";
                        var o = w.time + p
                    } else {
                        if (w.time.indexOf("ms") !== -1) {
                            var p = "ms";
                            var o = w.time
                        } else {
                            var p = "s";
                            var o = w.time + p
                        }
                    }
                    this.el.style[f.feat.cssPrefix + "TransitionDuration"] = o;
                    this.el.style[f.feat.cssPrefix + "TransitionTimingFunction"] = w.timingFunction;
                    this.el.style[f.feat.cssPrefix + "TransformOrigin"] = w.origin
                }
                this.callbacksStack.push({
                    complete: w.complete,
                    success: w.success,
                    failure: w.failure
                });
                this.countStack++;
                var s = this;
                var l = window.getComputedStyle(this.el);
                if (m) {
                    var q = l[f.feat.cssPrefix + "TransitionDuration"];
                    var u = numOnly(q);
                    w.time = u;
                    if (q.indexOf("ms") !== -1) {
                        p = "ms"
                    } else {
                        w.time *= 1000;
                        p = "s"
                    }
                }
                if (u == 0 || (p == "ms" && u < 5) || l.display == "none") {
                    f.asap(f.proxy(this.finishAnimation, this, [false]))
                } else {
                    var s = this;
                    this.activeEvent = function(x) {
                        clearTimeout(s.timeout);
                        s.finishAnimation(x);
                        s.el.removeEventListener(c, s.activeEvent, false)
                    };
                    s.timeout = setTimeout(this.activeEvent, numOnly(w.time) + 50);
                    this.el.addEventListener(c, this.activeEvent, false)
                }
            },
            addCallbackHook: function(l) {
                if (l) {
                    this.callbacksStack.push(l)
                }
                this.countStack++;
                return this.linkFinishedProxy_
            },
            linkFinished: function(l) {
                if (l) {
                    this.cancel()
                } else {
                    this.finishAnimation()
                }
            },
            finishAnimation: function(l) {
                if (l) {
                    l.preventDefault()
                }
                if (!this.isActive) {
                    return
                }
                this.countStack--;
                if (this.countStack == 0) {
                    this.fireCallbacks(false)
                }
            },
            fireCallbacks: function(l) {
                this.clearEvents();
                var q = this.callbacksStack;
                this.cleanup();
                for (var p = 0; p < q.length; p++) {
                    var m = q[p]["complete"];
                    var r = q[p]["success"];
                    var o = q[p]["failure"];
                    if (m && typeof(m) == "function") {
                        m(l)
                    }
                    if (l && o && typeof(o) == "function") {
                        o()
                    } else {
                        if (r && typeof(r) == "function") {
                            r()
                        }
                    }
                }
            },
            cancel: function() {
                if (!this.isActive) {
                    return
                }
                this.fireCallbacks(true)
            },
            cleanup: function() {
                this.callbacksStack = [];
                this.isActive = false;
                this.countStack = 0
            },
            clearEvents: function() {
                if (this.activeEvent) {
                    this.el.removeEventListener(c, this.activeEvent, false)
                }
                this.activeEvent = null
            },
            link: function(l, o) {
                var m = {
                    complete: o.complete,
                    success: o.success,
                    failure: o.failure
                };
                o.complete = this.addCallbackHook(m);
                o.success = null;
                o.failure = null;
                i(l, o);
                o.complete = m.complete;
                o.success = m.success;
                o.failure = m.failure;
                return this
            }
        };
        return k
    })();
    g.queue = function() {
        return {
            elements: [],
            push: function(k) {
                this.elements.push(k)
            },
            pop: function() {
                return this.elements.pop()
            },
            run: function() {
                var l = this;
                if (this.elements.length == 0) {
                    return
                }
                if (typeof(this.elements[0]) == "function") {
                    var k = this.shift();
                    k()
                }
                if (this.elements.length == 0) {
                    return
                }
                var m = this.shift();
                if (this.elements.length > 0) {
                    m.complete = function(o) {
                        if (!o) {
                            l.run()
                        }
                    }
                }
                g(document.getElementById(m.id), m)
            },
            shift: function() {
                return this.elements.shift()
            }
        }
    }
})(jm); (function(b) {
    b.fn.drawer = function(e) {
        var d;
        for (var c = 0; c < this.length; c++) {
            d = new a(this[c], e)
        }
        return this.length == 1 ? d: this
    };
    var a = (function() {
        var d = b.feat.cssTransformStart;
        var c = b.feat.cssTransformEnd;
        var f = false;
        var e = function(g, i) {
            if (typeof g == "string" || g instanceof String) {
                this.el = document.getElementById(g)
            } else {
                this.el = g
            }
            if (!this.el) {
                alert("Could not find element for drawer " + g);
                return
            }
            if (this instanceof e) {
                for (j in i) {
                    this[j] = i[j]
                }
            } else {
                return new e(g, i)
            }
            this.direction = this.direction.toLowerCase();
            try {
                this.handle = this.el.querySelectorAll(".drawer_handle")[0];
                if (!this.handle) {
                    return alert("Could not find handle for drawer -  " + g)
                }
                var h = this;
                this.handle.addEventListener("touchmove",
                    function(l) {
                        h.touchMove(l)
                    },
                    false);
                this.handle.addEventListener("touchend",
                    function(l) {
                        h.touchEnd(l)
                    },
                    false)
            } catch(k) {
                alert("error adding drawer" + k)
            }
            this.zIndex = b(this.el).css("zIndex")
        };
        e.prototype = {
            lockY: 0,
            lockX: 0,
            boolScrollLock: false,
            currentDrawer: null,
            maxTop: 0,
            startTop: 0,
            maxLeft: 0,
            startLeft: 0,
            timeMoved: 0,
            vdistanceMoved: 0,
            hdistanceMoved: 0,
            direction: "down",
            prevTime: 0,
            handle: null,
            zIndex: 1,
            touchMove: function(h) {
                try {
                    if (!f) {
                        f = true;
                        this.touchStart(h)
                    }
                    if (this.currentDrawer != null) {
                        h.preventDefault();
                        var o = {
                            x: 0,
                            y: 0
                        };
                        if (this.direction == "down" || this.direction == "up") {
                            var p = 0,
                                t = 0;
                            var k = this.lockY - h.touches[0].pageY;
                            k = -k;
                            var p = this.startTop + k;
                            var q = -p;
                            try {
                                var t = numOnly(b.getCssMatrix(this.el).f)
                            } catch(r) {
                                var t = 0
                            }
                            o.y = p;
                            this.vdistanceMoved += Math.abs(t) - Math.abs(p)
                        } else {
                            var s = 0,
                                g = 0;
                            var l = this.lockX - h.touches[0].pageX;
                            l = -l;
                            var s = this.startLeft + l;
                            var i = -s;
                            try {
                                var g = numOnly(b.getCssMatrix(this.el).e)
                            } catch(r) {
                                var g = 0
                            }
                            o.x = s;
                            this.hdistanceMoved += Math.abs(g) - Math.abs(s)
                        }
                        this.drawerMove(this.currentDrawer, o, 0)
                    }
                } catch(m) {
                    alert("error in scrollMove: " + m)
                }
            },
            touchStart: function(g) {
                var k = this.handle;
                var i = this.el;
                if (!k) {
                    return
                }
                try {
                    if (g.touches[0].target && g.touches[0].target.type != undefined) {
                        var h = g.touches[0].target.tagName.toLowerCase();
                        if (h == "select" || h == "input" || h == "button") {
                            return
                        }
                    }
                    this.vdistanceMoved = 0;
                    this.hdistanceMoved = 0;
                    this.maxTop = numOnly(this.el.style.height) - numOnly(this.handle.style.height);
                    this.maxLeft = numOnly(this.el.style.width) - numOnly(this.handle.style.width);
                    if (this.direction == "up") {
                        this.maxTop *= -1
                    }
                    if (this.direction == "left") {
                        this.maxLeft *= -1
                    }
                    if (g.touches.length == 1 && this.boolScrollLock == false) {
                        try {
                            this.startTop = numOnly(b.getCssMatrix(this.el).f);
                            this.startLeft = numOnly(b.getCssMatrix(this.el).e)
                        } catch(l) {
                            this.startTop = 0;
                            this.startLeft = 0;
                            console.log("error drawer touchstart " + l)
                        }
                        this.lockY = g.touches[0].pageY;
                        this.lockX = g.touches[0].pageX;
                        this.currentDrawer = i;
                        g.preventDefault()
                    }
                } catch(l) {
                    alert("error in drawer start: " + l)
                }
            },
            touchEnd: function(k) {
                if (this.currentDrawer != null) {
                    k.preventDefault();
                    k.stopPropagation();
                    var m = {
                        x: 0,
                        y: 0
                    };
                    if (this.direction == "up" || this.direction == "down") {
                        var i = -this.vdistanceMoved;
                        var h = Math.ceil(Math.abs(i) / Math.abs(this.maxTop) * 100);
                        var l = numOnly(b.getCssMatrix(this.el).f);
                        if (h > 17) {
                            if (i > 0) {
                                m.y = this.maxTop
                            }
                        } else {
                            if (Math.floor(this.maxTop / l) > 2) {
                                m.y = 0
                            }
                        }
                    } else {
                        var i = -this.hdistanceMoved;
                        var h = Math.ceil(Math.abs(i) / Math.abs(this.maxLeft) * 100);
                        var g = numOnly(b.getCssMatrix(this.el).e);
                        if (h > 17) {
                            if (i > 0) {
                                m.x = this.maxLeft
                            } else {
                                m.x = 0
                            }
                        } else {
                            if (Math.floor(this.maxLeft / g) > 2) {
                                m.y = 0
                            } else {
                                m.x = this.maxLeft
                            }
                        }
                    }
                    if (m.y > 0 || m.x > 0) {
                        this.el.zIndex = "9999"
                    } else {
                        this.el.zIndex = this.zIndex
                    }
                    this.drawerMove(this.currentDrawer, m, 300, "ease-out");
                    this.currentDrawer = null
                }
                this.vdistanceMoved = 0;
                f = false
            },
            drawerMove: function(g, k, i, h) {
                if (!i) {
                    i = 0
                }
                if (!h) {
                    h = "linear"
                }
                g.style[b.feat.cssPrefix + "Transform"] = "translate" + d + k.x + "px," + k.y + "px" + c;
                g.style[b.feat.cssPrefix + "TransitionDuration"] = i + "ms";
                g.style[b.feat.cssPrefix + "BackfaceVisibility"] = "hidden";
                g.style[b.feat.cssPrefix + "TransformStyle"] = "preserve-3d";
                g.style[b.feat.cssPrefix + "TransitionTimingFunction"] = h
            }
        };
        return e
    })()
})(jm); (function(d) {
    var b = [];
    var c = function(f) {
        if (!f.jqmCarouselId) {
            f.jqmCarouselId = d.uuid()
        }
        return f.jqmCarouselId
    };
    d.fn.carousel = function(h) {
        var g, k;
        for (var f = 0; f < this.length; f++) {
            k = c(this[f]);
            if (!b[k]) {
                g = new e(this[f], h);
                b[k] = g
            } else {
                g = b[k]
            }
        }
        return this.length == 1 ? g: this
    };
    var e = (function() {
        var g = d.feat.cssTransformStart;
        var f = d.feat.cssTransformEnd;
        var h = function(q, i) {
            if (typeof q === "string" || q instanceof String) {
                this.container = document.getElementById(q)
            } else {
                this.container = q
            }
            if (!this.container) {
                alert("Error finding container for carousel " + q);
                return
            }
            if (this instanceof h) {
                for (var o in i) {
                    if (i.hasOwnProperty(o)) {
                        this[o] = i[o]
                    }
                }
            } else {
                return new h(q, i)
            }
            var r = this;
            jm(this.container).bind("destroy",
                function(u) {
                    var v = r.container.jqmCarouselId;
                    window.removeEventListener("orientationchange", r.orientationHandler, false);
                    if (b[v]) {
                        delete b[v]
                    }
                    u.stopPropagation()
                });
            this.container.style.overflow = "hidden";
            if (this.vertical) {
                this.horizontal = false
            }
            var l = document.createElement("div");
            this.container.appendChild(l);
            var t = d(l);
            var s = d(this.container);
            var p = Array.prototype.slice.call(this.container.childNodes);
            while (p.length > 0) {
                var k = p.splice(0, 1);
                k = s.find(k);
                if (k.get() == l) {
                    continue
                }
                t.append(k.get())
            }
            if (this.horizontal) {
                l.style.display = "block";
                l.style["float"] = "left"
            } else {
                l.style.display = "block"
            }
            this.el = l;
            this.refreshItems();
            var m = jm(l);
            m.bind("touchmove",
                function(u) {
                    r.touchMove(u)
                });
            m.bind("touchend",
                function(u) {
                    r.touchEnd(u)
                });
            m.bind("touchstart",
                function(u) {
                    r.touchStart(u)
                });
            this.orientationHandler = function() {
                r.onMoveIndex(r.carouselIndex, 0)
            };
            window.addEventListener("orientationchange", this.orientationHandler, false)
        };
        h.prototype = {
            startX: 0,
            startY: 0,
            dx: 0,
            dy: 0,
            glue: false,
            myDivWidth: 0,
            myDivHeight: 0,
            cssMoveStart: 0,
            childrenCount: 0,
            carouselIndex: 0,
            vertical: false,
            horizontal: true,
            el: null,
            movingElement: false,
            container: null,
            lockMove: false,
            okToMove: false,
            photoAllJson: [],
            photoListDetailShowDom: null,
            moduleId: null,
            switchJump: null,
            touchStart: function(m) {
                this.okToMove = false;
                this.myDivWidth = numOnly(this.container.clientWidth);
                this.myDivHeight = numOnly(this.container.clientHeight);
                this.lockMove = false;
                if (m.touches[0].target && m.touches[0].target.type !== undefined) {
                    var l = m.touches[0].target.tagName.toLowerCase();
                    if (l === "select" || l === "input" || l === "button") {
                        return
                    }
                }
                if (m.touches.length === 1) {
                    this.movingElement = true;
                    this.startY = m.touches[0].pageY;
                    this.startX = m.touches[0].pageX;
                    var i = d.getCssMatrix(this.el);
                    if (this.vertical) {
                        try {
                            this.cssMoveStart = numOnly(i.f)
                        } catch(k) {
                            this.cssMoveStart = 0
                        }
                    } else {
                        try {
                            this.cssMoveStart = numOnly(i.e)
                        } catch(k) {
                            this.cssMoveStart = 0
                        }
                    }
                }
            },
            touchMove: function(l) {
                if (!this.movingElement) {
                    return
                }
                if (l.touches.length > 1) {
                    return this.touchEnd(l)
                }
                var m = {
                    x: l.touches[0].pageX - this.startX,
                    y: l.touches[0].pageY - this.startY
                };
                if (this.vertical) {
                    var k = {
                        x: 0,
                        y: 0
                    };
                    this.dy = l.touches[0].pageY - this.startY;
                    this.dy += this.cssMoveStart;
                    k.y = this.dy;
                    l.preventDefault()
                } else {
                    if ((!this.lockMove && a(m.x, m.y)) || Math.abs(this.dx) > 5) {
                        var k = {
                            x: 0,
                            y: 0
                        };
                        this.dx = l.touches[0].pageX - this.startX;
                        this.dx += this.cssMoveStart;
                        l.preventDefault();
                        k.x = this.dx
                    } else {
                        return this.lockMove = true
                    }
                }
                var i = this.vertical ? ((this.dy % this.myDivHeight) / this.myDivHeight * 100) * -1 : ((this.dx % this.myDivWidth) / this.myDivWidth * 100) * -1;
                if (!this.okToMove) {
                    oldStateOkToMove = this.okToMove;
                    this.okToMove = this.glue ? Math.abs(i) > this.glue && Math.abs(i) < (100 - this.glue) : true;
                    if (this.okToMove && !oldStateOkToMove) {
                        d.trigger(this, "movestart", [this.el])
                    }
                }
                if (this.okToMove && k) {
                    this.moveCSS3(this.el, k)
                }
            },
            touchEnd: function(r) {
                if (!this.movingElement) {
                    return
                }
                d.trigger(this, "movestop", [this.el]);
                var s = false;
                try {
                    var o = d.getCssMatrix(this.el);
                    var k = this.vertical ? numOnly(o.f) : numOnly(o.e);
                    if (1 == 2 && k > 0) {
                        this.moveCSS3(this.el, {
                                x: 0,
                                y: 0
                            },
                            "300")
                    } else {
                        var i = this.vertical ? ((this.dy % this.myDivHeight) / this.myDivHeight * 100) * -1 : ((this.dx % this.myDivWidth) / this.myDivWidth * 100) * -1;
                        var l = this.carouselIndex;
                        if (k < this.cssMoveStart && i > 3) {
                            l++
                        } else {
                            if ((k > this.cssMoveStart && i < 97)) {
                                l--
                            }
                        }
                        var p = l;
                        if (l > (this.childrenCount - 1)) {
                            l = 0;
                            p = this.childrenCount
                        }
                        if (l < 0) {
                            l = this.childrenCount - 1;
                            p = -1
                        }
                        var m = {
                            x: 0,
                            y: 0
                        };
                        if (this.vertical) {
                            m.y = (p * this.myDivHeight * -1)
                        } else {
                            m.x = (p * this.myDivWidth * -1)
                        }
                        this.moveCSS3(this.el, m, "150");
                        if (this.carouselIndex != l) {
                            s = true
                        }
                        this.carouselIndex = l;
                        var q = this;
                        window.setTimeout(function() {
                                q.onMoveIndex(l, "1ms", q.photoAllJson[l])
                            },
                            155)
                    }
                } catch(r) {
                    console.log(r)
                }
                this.dx = 0;
                this.movingElement = false;
                this.startX = 0;
                this.dy = 0;
                this.startY = 0
            },
            onMoveIndex: function(k, m, r) {
                this.myDivWidth = numOnly(this.container.clientWidth);
                this.myDivHeight = numOnly(this.container.clientHeight);
                var p = this.myDivHeight / this.myDivWidth;
                if (r) {
                    var i = r.width,
                        y = r.height,
                        x = y / i,
                        q = this.container.querySelectorAll("img")[k],
                        w = '<div class="photoListDetailLoadingDom" id="photoListDetailLoadingDom"><div class="photoLoading spin"></div></div>';
                    if (this.myDivWidth > i && this.myDivHeight > y) {
                        var t = this.myDivWidth / i > this.myDivHeight / y ? this.myDivHeight / y: this.myDivWidth / i;
                        q.style.width = i * t + "px";
                        q.style.height = y * t + "px"
                    }
                    if (!q.src) {
                        jm(q.parentNode).append(w);
                        q.src = r.picPath
                    }
                    jm(q).load(function() {
                        jm(document.getElementById("photoListDetailLoadingDom")).remove()
                    });
                    this.photoDetailAjax(this.photoAllJson[k]);
                    this.photoListDetailShowDom.querySelector(".webPhotoListDetail").className = "webPhotoListDetail"
                }
                var u = false;
                var v = Math.abs(k - this.carouselIndex);
                var l = k;
                if (l < 0) {
                    l = 0
                }
                if (l > this.childrenCount - 1) {
                    l = this.childrenCount - 1
                }
                var s = {
                    x: 0,
                    y: 0
                };
                if (this.vertical) {
                    s.y = (l * this.myDivHeight * -1)
                } else {
                    s.x = (l * this.myDivWidth * -1)
                }
                var o = m ? m: 50 + parseInt((v * 20));
                this.moveCSS3(this.el, s, o);
                if (this.carouselIndex != l) {
                    u = true
                }
                this.carouselIndex = l
            },
            photoDetailAjax: function(k) {
                if (!this.photoListDetailShowDom && !this.moduleId) {
                    return
                }
                var i = this.photoListDetailShowDom.querySelector(".drawer_handle"),
                    l = i.parentNode.querySelector(".handleIcon");
                descriptionDom = document.getElementById("descriptionDom" + this.moduleId);
                if (k.detail === undefined) {
                    jm.ajax({
                        url: "ajax/photo_h.jsp",
                        data: "cmd=getInfo&id=" + jm.encodeUrl(k.id) + "&groupId=" + jm.encodeUrl(k.groupId),
                        error: function() {},
                        success: function(m) {
                            m = m.trim();
                            i.innerHTML = "";
                            descriptionDom.innerHTML = "";
                            i.innerHTML = jm.encodeHtml(k.name);
                            descriptionDom.innerHTML = jm.encodeHtml(k.basic) + (k.basic && m ? "<br /><br />": "") + m;
                            k.detail = m;
                            if (descriptionDom.innerHTML) {
                                descriptionDom.parentNode.style.bottom = i.offsetHeight * 2 - descriptionDom.parentNode.offsetHeight + "px";
                                l.className = "handleIcon handleUp"
                            } else {
                                descriptionDom.parentNode.style.bottom = i.offsetHeight - descriptionDom.parentNode.offsetHeight + "px";
                                l.className = "handleIcon"
                            }
                        }
                    })
                } else {
                    i.innerHTML = "";
                    descriptionDom.innerHTML = "";
                    i.innerHTML = jm.encodeHtml(k.name);
                    descriptionDom.innerHTML = jm.encodeHtml(k.basic) + (k.basic && k.detail ? "<br /><br />": "") + k.detail;
                    if (descriptionDom.innerHTML) {
                        descriptionDom.parentNode.style.bottom = i.offsetHeight * 2 - descriptionDom.parentNode.offsetHeight + "px";
                        l.className = "handleIcon handleUp"
                    } else {
                        descriptionDom.parentNode.style.bottom = i.offsetHeight - descriptionDom.parentNode.offsetHeight + "px";
                        l.className = "handleIcon"
                    }
                }
            },
            moveCSS3: function(i, m, l, k) {
                if (!l) {
                    l = 0
                } else {
                    l = parseInt(l)
                }
                if (!k) {
                    k = "linear"
                }
                i.style[d.feat.cssPrefix + "Transform"] = "translate" + g + m.x + "px," + m.y + "px" + f;
                i.style[d.feat.cssPrefix + "TransitionDuration"] = l + "ms";
                i.style[d.feat.cssPrefix + "BackfaceVisibility"] = "hidden";
                i.style[d.feat.cssPrefix + "TransformStyle"] = "preserve-3d";
                i.style[d.feat.cssPrefix + "TransitionTimingFunction"] = k
            },
            addItem: function(i) {
                if (i && i.nodeType) {
                    this.container.childNodes[1].appendChild(i);
                    this.refreshItems()
                }
            },
            refreshItems: function() {
                var u = 0;
                var s = this;
                var l = this.el;
                d(this.container).find(".prevPhoto").remove();
                d(this.container).find(".nextPhoto").remove();
                n = l.childNodes[0];
                var p;
                var o = "100%";
                var k = [];
                for (; n; n = n.nextSibling) {
                    if (n.nodeType === 1) {
                        k.push(n);
                        u++
                    }
                }
                var t = document.createElement("div");
                t.className = "prevPhoto";
                d(this.container).prepend(t);
                var r = document.createElement("div");
                r.className = "nextPhoto";
                d(this.container).append(r);
                var m = (100 / u) + "%";
                this.childrenCount = u;
                p = parseFloat(100 / u) + "%";
                for (var q = 0; q < k.length; q++) {
                    k[q].style.width = p
                }
                r.style.position = "absolute";
                t.style.position = "absolute";
                function v(y) {
                    var w = s.carouselIndex;
                    if (y.className == "prevPhoto") {
                        w--
                    } else {
                        w++
                    }
                    var i = w;
                    if (w > (s.childrenCount - 1)) {
                        w = 0;
                        i = s.childrenCount
                    }
                    if (w < 0) {
                        w = s.childrenCount - 1;
                        i = -1
                    }
                    var x = {
                        x: 0,
                        y: 0
                    };
                    x.x = (i * s.myDivWidth * -1);
                    s.moveCSS3(s.el, x, "150");
                    if (s.carouselIndex != w) {
                        runFinal = true
                    }
                    s.carouselIndex = w;
                    window.setTimeout(function() {
                            s.onMoveIndex(w, "1ms", s.photoAllJson[w])
                        },
                        155)
                }
                t.onclick = function() {
                    v(this)
                };
                r.onclick = function() {
                    v(this)
                };
                this.moveCSS3(l, {
                    x: 0,
                    y: 0
                });
                l.style.width = Math.ceil((this.childrenCount) * 100) + "%";
                l.style.height = "100%";
                l.style["min-height"] = "100%";
                this.onMoveIndex(this.carouselIndex)
            }
        };
        return h
    })();
    function a(h, f) {
        var m = h;
        var l = f;
        var k = Math.round(Math.sqrt(Math.pow(m, 2) + Math.pow(l, 2)));
        var g = Math.atan2(l, m);
        var i = Math.round(g * 180 / Math.PI);
        if (i < 0) {
            i = 360 - Math.abs(i)
        }
        if (((i <= 215) && (i >= 155)) || ((i <= 45) && (i >= 0)) || ((i <= 360) && (i >= 315))) {
            return true
        } else {
            return false
        }
    }
})(jm); (function(e) {
    e.fn.Calendar = function(k) {
        return new a(this, k)
    };
    var f = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"],
        b = ["日", "一", "二", "三", "四", "五", "六"],
        i = /^(\+|\-)?(\d+)(M|Y)$/i,
        d = function(k, l) {
            return 32 - new Date(k, l, 32).getDate()
        },
        g = function(k, l) {
            return new Date(k, l, 1).getDay()
        },
        c = function(m, k) {
            var l = "" + m;
            while (l.length < k) {
                l = "0" + l
            }
            return l
        },
        h = function(k) {
            return k.is("select, input") ? k.val() : k.attr("data-value")
        };
    if (typeof LS != "undefined") {
        b = [LS.date_Sun, LS.date_Mon, LS.date_Tue, LS.date_Wed, LS.date_Thu, LS.date_Fri, LS.date_Sat]
    }
    var a = function(l, k) {
        this._options = {
            date: null,
            firstDay: 1,
            maxDate: null,
            minDate: null,
            swipeable: false,
            slideAble: false,
            distance: 30,
            monthChangeable: false,
            yearChangeable: false,
            beforeShowDay: null,
            showHour: false,
            minHour: 0,
            onSelectDate: null,
            onAfterSelectDate: null,
            onAfterRefresh: null,
            inputExtClass: "",
            onBeforeInitCalendar: null,
            onBeforeCancel: null,
            beforeShowTime: null,
            onAfterHidePicker: null,
            _privateDate: {}
        };
        this._initOptions(k);
        this._init(l);
        this.setTargetOption(this._container[0].id, this)
    };
    a.prototype = {
        _initOptions: function(m) {
            var o = this._options;
            for (var l in m) {
                o[l] = m[l]
            }
        },
        _init: function(l) {
            var k = this;
            k._container = jm("#" + l[0].id);
            el = k._container || k.$el,
                k._initDateInput(el, l)
        },
        _hideDate: function(k, m) {
            var l = m._options,
                o = l.onAfterHidePicker;
            k.removeClass("ui-calendar-show");
            l._privateDate = {};
            setTimeout(function() {
                    k.remove();
                    typeof o == "function" && o(m)
                },
                300)
        },
        _showDate: function(k) {
            k.addClass("ui-calendar-show")
        },
        _initDateInput: function(l, o) {
            var k = document.createElement("input"),
                m = this;
            k.className = "_initDateInput g_itext " + m._options.inputExtClass;
            k.id = l[0].id + "input";
            k.readOnly = true;
            k.type = "text";
            k.placeholder = m._options.placeholder;
            e(l[0].parentNode).append(e(k));
            this._input = e(k);
            e(k).on("click",
                function() {
                    var t = m._options,
                        s = m._container || m.$el,
                        r = e.proxy(m._eventHandler, m),
                        p = new Date(e(k).val()),
                        q = t.onBeforeInitCalendar,
                        u;
                    if ( !! p.getTime()) {
                        u = p
                    } else {
                        if ( !! t.date) {
                            u = t.date
                        } else {
                            u = new Date()
                        }
                    }
                    typeof q == "function" && q(m);
                    m.minDate(t.minDate).maxDate(t.maxDate).date(u).refresh();
                    s.addClass("ui-calendar").on("click", r);
                    t.swipeable && s.on("swipeLeft swipeRight", r);
                    t.slideAble && s.on("touchstart touchend", ".ui-calendar-calendar", r);
                    setTimeout(function() {
                            m._showDate(s)
                        },
                        100)
                })
        },
        _create: function() {
            var k = this.$el;
            if (!k) {
                k = this.$el = e("<div>")
            }
            k.appendTo(this._options.container || (k.parent().length ? "": document.body))
        },
        _eventHandler: function(r) {
            var k = this._options,
                u = (this._container || this.$el).get(0),
                m = this,
                p,
                s,
                v,
                o,
                l,
                t,
                q;
            switch (r.type) {
                case "swipeLeft":
                case "swipeRight":
                    return this.switchMonthTo((r.type == "swipeRight" ? "-": "+") + "1M");
                case "change":
                    l = e(".ui-calendar-header .ui-calendar-year, .ui-calendar-header .ui-calendar-month", this._container);
                    return this.switchMonthTo(h(l.eq(1)), h(l.eq(0)));
                case "touchstart":
                    k._privateDate.startX = r.pageX;
                    if (e(r.target).closest(".ui-calendar-calendar-wrap").length > 0) {
                        r.preventDefault();
                        return false
                    }
                case "touchend":
                    k._privateDate.endX = r.pageX;
                    t = k._privateDate.startX + k.distance <= k._privateDate.endX;
                    q = k._privateDate.endX + k.distance <= k._privateDate.startX;
                    if (t && !k._privateDate.moving) {
                        k._privateDate.slideAnimate = true;
                        k._privateDate.animateDirtion = "left";
                        m.switchMonthTo("-1M");
                        r.preventDefault();
                        return false
                    } else {
                        if (q && !k._privateDate.moving) {
                            k._privateDate.slideAnimate = true;
                            k._privateDate.animateDirtion = "right";
                            m.switchMonthTo("+1M");
                            r.preventDefault();
                            return false
                        }
                    }
                default:
                    s = r.target;
                    if ((p = e(s).closest(".ui-calendar-calendar tbody a", u)) && p.length) {
                        r.preventDefault();
                        v = p.parent();
                        this._option("selectedDate", o = new Date(v.attr("data-year"), v.attr("data-month"), p.text()));
                        jm(this._container).trigger("select", o, e.calendar.formatDate(o), this);
                        this.refresh();
                        this._options._selectDate = this._options._selectedYear + "-" + (this._options._selectedMonth + 1) + "-" + this._options._selectedDay;
                        typeof this._options.onSelectDate == "function" && this._options.onSelectDate(this._options._selectDate, this);
                        if (!this._options.showHour) {
                            this._hideDate(this._container, this);
                            this._input.val(this._options._selectDate)
                        }
                        typeof this._options.onAfterSelectDate == "function" && this._options.onAfterSelectDate(this._options._selectDate, this)
                    } else {
                        if ((p = e(s).closest(".ui-calendar-prev, .ui-calendar-next", u)) && p.length) {
                            r.preventDefault();
                            this.switchMonthTo((p.is(".ui-calendar-prev") ? "-": "+") + "1M")
                        } else {
                            if (((p = e(s).closest(".ui-calendar-return", u)) || (p = e(s).closest(".ui-calendar-cancel", u))) && p.length) {
                                r.preventDefault();
                                this._hideDate(this._container, this)
                            }
                        }
                    }
            }
        },
        _bindTimePickerEvent: function() {
            var p = this,
                o = 0,
                r = ["height", "margin", "padding", "borderTopWidth", "borderBottomWidth"],
                l,
                q,
                s,
                t;
            var k = (function() {
                var v = document.createElement("_");
                if (! ("pointerEvents" in v.style)) {
                    return false
                }
                v.style.pointerEvents = "auto";
                v.style.pointerEvents = "x";
                document.body.appendChild(v);
                var u = getComputedStyle(v).pointerEvents === "auto";
                document.body.removeChild(v);
                return u
            })();
            t = p._timePicker = {};
            p._timePicker.container = e("#fk-timePickerPanel");
            p._timePicker.items = t.container.find("li").not(".fk-timeBan");
            p._timePicker.timeMarkEL = e(".mobiCalendarPlugins .J_timeMark");
            if (!k) {
                p._timePicker.container.find(".J_timeMaskWrap").css("z-index", "0");
                p._timePicker.container.find(".J_timeMark").addClass("f-highLight")
            }
            q = p._timePicker.items.eq(0);
            for (var m = 0; m < r.length; m++) {
                s = q.css(r[m]);
                if (r[m] == "margin" || r[m] == "padding") {
                    s = typeof s == "string" && s.length > 0 ? s.split(" ") : ["0px", "0px"];
                    o += parseFloat(s[0].replace("px", ""));
                    if (s.length == 3) {
                        o += parseFloat(s[2].replace("px", ""))
                    } else {
                        if (s.length == 2) {
                            o += parseFloat(s[0].replace("px", ""))
                        }
                    }
                } else {
                    s = typeof s == "string" && s.length > 0 ? parseFloat(s.replace("px", "")) : 0;
                    o += s
                }
            }
            p._timePicker.itemHeight = t.items[0].offsetHeight + !!t.items.eq(0);
            l = new iScroll("fk-timePickerPanel", {
                vScrollbar: true,
                onScrollStart: function() {
                    p._timePicker.container.removeClass("J_endTimeFlag")
                },
                onScrollEnd: function() {
                    if (p._timePicker.container.hasClass("J_endTimeFlag")) {
                        return
                    }
                    var u = p._timePicker,
                        A = u.items.eq(0),
                        x = Math.abs(A.offset().top) + parseFloat(A.height() / 2),
                        y = [],
                        z = u.timeMarkEL.offset().top + t.timeMarkEL.height() / 2,
                        C = u.container.find("li").not(".fk-timeBan"),
                        B,
                        w;
                    for (var v = 0; v < u.items.length; v++) {
                        y.push(u.items.eq(v).offset().top + parseFloat(A.height() / 2))
                    }
                    B = Math.abs(y[0] - z);
                    w = 0;
                    for (var v = 0; v < y.length; v++) {
                        if (Math.abs(y[v] - z) < B) {
                            B = Math.abs(y[v] - z);
                            w = v
                        }
                    }
                    if (e(C[w - 2]).length < 1) {
                        return
                    }
                    u.container.addClass("J_endTimeFlag");
                    l.scrollTo(0, -C[w - 2].offsetTop, 300);
                    p._options._selectTime = parseInt(C.eq(w).attr("data-hour"));
                    p._options.showHour && p._container.find(".ui-calendar-navHeadHour").text((p._options._selectTime < 10 ? ("0" + p._options._selectTime) : p._options._selectTime) + ":00")
                }
            });
            if ( !! p._options._selectTime) {
                l.scrollTo(0, -p._timePicker.container.find("li")[p._options._selectTime].offsetTop, 0)
            }
            p._container.find(".J_saveTimeBtn ").on("click",
                function(w) {
                    var v, u;
                    w.preventDefault();
                    u = p._options._selectTime = p._options._selectTime || 0;
                    v = u == 0 ? "00:00": (u < 10 ? ("0" + u + ":00") : u + ":00");
                    p._options._selectDate = p._options._selectedYear + "-" + (p._options._selectedMonth + 1) + "-" + p._options._selectedDay + " " + v;
                    p._hideDate(p._container, p);
                    p._input.val(p._options._selectDate)
                })
        },
        _option: function(l, r) {
            var p = this,
                m = p._options,
                k, o, q;
            if (r !== undefined) {
                switch (l) {
                    case "minDate":
                    case "maxDate":
                        m[l] = r ? e.calendar.parseDate(r) : null;
                        break;
                    case "selectedDate":
                        o = m.minDate;
                        q = m.maxDate;
                        r = e.calendar.parseDate(r);
                        r = o && o > r ? o: q && q < r ? q: r;
                        m._selectedYear = m._drawYear = r.getFullYear();
                        m._selectedMonth = m._drawMonth = r.getMonth();
                        m._selectedDay = r.getDate();
                        m._selectTime = m._selectTime || m.minHour || 0;
                        break;
                    case "date":
                        this._option("selectedDate", r);
                        m[l] = this._option("selectedDate");
                        break;
                    default:
                        m[l] = r
                }
                m._invalid = true;
                return this
            }
            return l == "selectedDate" ? new Date(m._selectedYear, m._selectedMonth, m._selectedDay) : m[l]
        },
        _optionCash: {},
        getTargetOption: function(k) {
            return this._optionCash[k]
        },
        setTargetOption: function(l, k) {
            this._optionCash[l] = k
        },
        delTargetOption: function(k) {
            delete this._optionCash[k]
        },
        switchToToday: function() {
            var k = new Date();
            return this.switchMonthTo(k.getMonth(), k.getFullYear())
        },
        switchMonthTo: function(p, l) {
            var m = this._options,
                o = this.minDate(),
                s = this.maxDate(),
                r,
                q,
                k;
            if (Object.prototype.toString.call(p) === "[object String]" && i.test(p)) {
                r = RegExp.$1 == "-" ? -parseInt(RegExp.$2, 10) : parseInt(RegExp.$2, 10);
                q = RegExp.$3.toLowerCase();
                p = m._drawMonth + (q == "m" ? r: 0);
                l = m._drawYear + (q == "y" ? r: 0)
            } else {
                p = parseInt(p, 10);
                l = parseInt(l, 10)
            }
            k = new Date(l, p, 1);
            k = o && o > k ? o: s && s < k ? s: k;
            p = k.getMonth();
            l = k.getFullYear();
            if (p != m._drawMonth || l != m._drawYear) {
                jm(this._container).trigger("monthchange", m._drawMonth = p, m._drawYear = l, this);
                m._invalid = true;
                this.refresh()
            }
            return this
        },
        refresh: function() {
            var o = this,
                l = this._options,
                p = this._container || this.$el,
                w = l.onAfterRefresh,
                s = e.proxy(this._eventHandler, this),
                y,
                t,
                k,
                v,
                u,
                x,
                r,
                q,
                m;
            if (!l._invalid) {
                return
            }
            e(".ui-calendar-header select", p).off("change", s);
            if (l.slideAble && l._privateDate.slideAnimate) {
                y = p.find(".ui-calendar-calendar");
                v = l._privateDate.animateDirtion == "right";
                u = l._privateDate.animateDirtion == "left";
                x = v ? "-": "";
                r = y.width();
                q = v ? r: 0;
                p.empty().append(this._generateHTML());
                k = p.find(".ui-calendar-calendar-wrap").addClass("fk-calendarWrapAnimate");
                m = p.find(".ui-calendar-calendar");
                if (v) {
                    y.insertBefore(m);
                    l._privateDate.animateDirtion = null
                } else {
                    if (u) {
                        y.insertAfter(m);
                        k.css("transform", "translateX(-" + r + "px)");
                        l._privateDate.animateDirtion = null
                    }
                }
                p.appendTo(e("#webFooterBox"));
                k.one("webkitTransitionEnd msTransitionEnd oTransitionEnd transitionend",
                    function() {
                        if ( !! y && y.length > 0) {
                            y.remove();
                            k.removeClass("fk-calendarWrapAnimate");
                            o._animateRunCtrl(k, "transform", "none");
                            l._privateDate.moving = false;
                            k = null;
                            y = null
                        }
                    });
                setTimeout(function() {
                        o._animateRunCtrl(k, "transform", "translateX(" + x + q + "px)")
                    },
                    20);
                l._privateDate.slideAnimate = false;
                l._privateDate.moving = true
            } else {
                p.empty().append(this._generateHTML());
                p.appendTo(e("#webFooterBox"))
            }
            p.find(".J_cancelBtn ").on("click",
                function(A) {
                    var B = l.onBeforeCancel,
                        z = o;
                    typeof B == "function" && B(z);
                    A.preventDefault();
                    z._hideDate(p, z);
                    o = null
                });
            e(".ui-calendar-header select", p).on("change", s);
            l._invalid = false;
            l.showHour && this._bindTimePickerEvent();
            if (typeof w == "function") {
                w(this)
            }
            return this
        },
        _animateRunCtrl: function(o, k, m) {
            var p = ["", "-webkit-", "-moz-", "-ms-"],
                l = p.length;
            while (l) {
                l--;
                o.css(p[l] + k, m)
            }
        },
        destroy: function() {
            var l = this._container || this.$el,
                m = this._input,
                k = this._eventHandler;
            this.delTargetOption(m.attr("id"));
            e(".ui-calendar-header select", l).off("change", k);
            l.remove();
            return this.$super("destroy")
        },
        _generateHTML: function() {
            var k = this._options,
                r = k._drawYear,
                v = k._drawMonth,
                q = new Date(),
                z = new Date(q.getFullYear(), q.getMonth(), q.getDate()),
                t = this.minDate(),
                o = this.maxDate(),
                x = this.selectedDate(),
                w = "",
                u,
                s,
                m,
                A,
                y,
                l,
                B,
                p;
            m = (isNaN(m = parseInt(k.firstDay, 10)) ? 0 : m);
            w += this._renderNavHeader(k);
            w += this._renderHead(k, r, v, t, o) + '<div class="ui-calendar-calendar-wrap"><table  class="ui-calendar-calendar"><thead><tr>';
            for (u = 0; u < 7; u++) {
                A = (u + m) % 7;
                w += "<th" + ((u + m + 6) % 7 >= 5 ? ' class="ui-calendar-week-end"': "") + "><span>" + b[A] + "</span></th>"
            }
            w += "</thead></tr><tbody>";
            l = d(r, v);
            y = (g(r, v) - m + 7) % 7;
            B = Math.ceil((y + l) / 7);
            p = new Date(r, v, 1 - y);
            for (u = 0; u < B; u++) {
                w += "<tr>";
                for (s = 0; s < 7; s++) {
                    w += this._renderDay(s, p, m, v, x, z, t, o);
                    p.setDate(p.getDate() + 1)
                }
                w += "</tr>"
            }
            w += "</tbody></table></div>";
            w += this._renderBottom();
            w += this._renderTime();
            return w
        },
        _renderNavHeader: function(k) {
            return k.drawNavHeaderHtml && k.drawNavHeaderHtml.length > 0 ? k.drawNavHeaderHtml: ""
        },
        _renderBottom: function() {
            return '<div class="ui-calendar-bottom"><a class="ui-calendar-day" href="#"></a><a class="ui-calendar-cancel" href="#"></a></div>'
        },
        _renderHead: function(q, m, r, o, k) {
            var s = '<div class="ui-calendar-header">',
                l = new Date(m, r, -1),
                u = new Date(m, r + 1, 1),
                w = ( !! o && typeof o.getFullYear == "function") ? o.getFullYear() : null,
                t = ( !! o && typeof o.getMonth == "function") ? o.getMonth() : null,
                v = p + 58,
                v = v <= 2028 ? v: 2028,
                p;
            s += '<a class="ui-calendar-return"></a><div class="ui-calendar-title">';
            if (q.yearChangeable) {
                s += '<select class="ui-calendar-year">';
                for (p = (w != null && w < m ? w: m); p < v; p++) {
                    s += '<option value="' + p + '" ' + (p == m ? 'selected="selected"': "") + ">" + p + "</option>"
                }
                s += "</select>"
            } else {
                s += '<span class="ui-calendar-year" data-value="' + m + '">' + m + "年</span>"
            }
            if (q.monthChangeable) {
                s += '<select class="ui-calendar-month">';
                for (p = (t != null && t < r ? t: r); p < 12; p++) {
                    s += '<option value="' + p + '" ' + (p == r ? 'selected="selected"': "") + ">" + f[p] + "</option>"
                }
                s += "</select>"
            } else {
                s += '<span class="ui-calendar-month" data-value="' + r + '">' + f[r] + "</span>"
            }
            s += '</div><div class="ui-calendar-preandnext"><a class="ui-calendar-prev' + (o && o > l ? " ui-state-disable": "") + '" href="#"></a><a class="ui-calendar-next' + (k && k < u ? " ui-state-disable": "") + '" href="#"></a></div></div>';
            return s
        },
        _renderDay: function(r, o, l, s, t, u, q, k) {
            var m = (o.getMonth() !== s),
                v = this._options.beforeShowDay,
                p;
            p = ((typeof v == "function" && !v(o, this))) || m || (q && o < q) || (k && o > k);
            return "<td class='" + ((r + l + 6) % 7 >= 5 ? "ui-calendar-week-end": "") + (p ? " ui-calendar-unSelectable ui-state-disabled": "") + (m || p ? "": (o.getTime() === t.getTime() ? " ui-calendar-current-day": "") + (o.getTime() === u.getTime() ? " ui-calendar-today": "")) + "'" + (p ? "": " data-month='" + o.getMonth() + "' data-year='" + o.getFullYear() + "'") + ">" + (m ? "&#xa0;": (p ? "<span class='ui-state-default'>" + o.getDate() + "</span>": "<a class='ui-state-default" + (o.getTime() === u.getTime() ? " ui-state-highlight": "") + (o.getTime() === t.getTime() ? " ui-state-active": "") + "' href='#'>" + o.getDate() + "</a>")) + "</td>"
        },
        _renderTime: function() {
            var o = "",
                m = this._options,
                k = false,
                p = m.beforeShowTime;
            if (!m.showHour) {
                o += "<div class='fk-timePickerSubmit'>";
                o += "<div class='J_cancelBtn f-dateCancelBtn'>" + LS.cancel + "</div>";
                o += "</div>";
                return o
            }
            o += "<div class='fk-timePickerPanelWrap'>";
            o += "<div class='J_timeMaskWrap f-timeMaskWrap'>";
            o += "<div class='f-timeMaskTop'></div>";
            o += "<div class='J_timeMark f-timeMark'></div>";
            o += "<div class='f-timeMaskBottom'></div>";
            o += "</div>";
            o += "<div id='fk-timePickerPanel' class='timePickerPanel'>";
            o += "<ul>";
            o += "<li>&nbsp</li>";
            o += "<li>&nbsp</li>";
            for (var l = 0; l <= 23; l++) {
                k = typeof p == "function" && !p(l, this);
                o += "<li class='" + (k ? "fk-timeBan": "") + "' data-hour='" + l + "'>" + (l < 10 ? ("0" + l) : l) + ":00</li>"
            }
            o += "<li>&nbsp</li>";
            o += "<li>&nbsp</li>";
            o += "</ul>";
            o += "</div>";
            o += "</div>";
            o += "<div class='fk-timePickerSubmit'>";
            o += "<div class='J_cancelBtn f-cancelBtn'>取消</div>";
            o += "<div class='J_saveTimeBtn f-saveTimeBtn'>确定</div>";
            o += "</div>";
            return o
        }
    };
    e.each(["maxDate", "minDate", "date", "selectedDate"],
        function(l, k) {
            a.prototype[k] = function(m) {
                return this._option(k, m)
            }
        });
    e.calendar = {
        parseDate: function(l) {
            var k = /^(\d{4})(?:\-|\/)(\d{1,2})(?:\-|\/)(\d{1,2})$/;
            return Object.prototype.toString.call(l) === "[object Date]" ? l: k.test(l) ? new Date(parseInt(RegExp.$1, 10), parseInt(RegExp.$2, 10) - 1, parseInt(RegExp.$3, 10)) : null
        },
        formatDate: function(k) {
            return k.getFullYear() + "-" + c(k.getMonth() + 1, 2) + "-" + c(k.getDate(), 2)
        }
    }
})(jm);
/*!
 * iScroll Lite base on iScroll v4.1.6 ~ Copyright (c) 2011 Matteo Spinelli, http://cubiq.org
 * Released under MIT license, http://cubiq.org/license
 */
(function() {
    var s = Math,
        d = function(m) {
            return m >> 0
        },
        w = (/webkit/i).test(navigator.appVersion) ? "webkit": (/firefox/i).test(navigator.userAgent) ? "Moz": "opera" in window ? "O": "",
        x = (/android/gi).test(navigator.appVersion),
        i = (/iphone|ipad/gi).test(navigator.appVersion),
        c = (/playbook/gi).test(navigator.appVersion),
        p = (/hp-tablet/gi).test(navigator.appVersion),
        l = "WebKitCSSMatrix" in window && "m11" in new WebKitCSSMatrix(),
        v = "ontouchstart" in window && !p,
        f = w + "Transform" in document.documentElement.style,
        g = i || c,
        q = (function() {
            return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame ||
                function(m) {
                    return setTimeout(m, 17)
                }
        })(),
        o = (function() {
            return window.cancelRequestAnimationFrame || window.webkitCancelAnimationFrame || window.webkitCancelRequestAnimationFrame || window.mozCancelRequestAnimationFrame || window.oCancelRequestAnimationFrame || window.msCancelRequestAnimationFrame || clearTimeout
        })(),
        h = "onorientationchange" in window ? "orientationchange": "resize",
        b = v ? "touchstart": "mousedown",
        r = v ? "touchmove": "mousemove",
        e = v ? "touchend": "mouseup",
        u = v ? "touchcancel": "mouseup",
        a = "translate" + (l ? "3d(": "("),
        k = l ? ",0)": ")",
        t = function(z, m) {
            var A = this,
                B = document,
                y;
            A.wrapper = typeof z == "object" ? z: B.getElementById(z);
            A.wrapper.style.overflow = "hidden";
            A.scroller = A.wrapper.children[0];
            A.options = {
                hScroll: true,
                vScroll: true,
                x: 0,
                y: 0,
                bounce: true,
                bounceLock: false,
                momentum: true,
                lockDirection: true,
                useTransform: true,
                useTransition: false,
                remarkXCoordinate: false,
                remarkNavLeftRight: null,
                onRefresh: null,
                onBeforeScrollStart: function(C) {
                    C.preventDefault()
                },
                onScrollStart: null,
                onBeforeScrollMove: null,
                onScrollMove: null,
                onBeforeScrollEnd: null,
                onScrollEnd: null,
                onTouchEnd: null,
                onDestroy: null
            };
            for (y in m) {
                A.options[y] = m[y]
            }
            A.x = A.options.x;
            A.y = A.options.y;
            A.options.useTransform = f ? A.options.useTransform: false;
            A.options.hScrollbar = A.options.hScroll && A.options.hScrollbar;
            A.options.vScrollbar = A.options.vScroll && A.options.vScrollbar;
            A.options.useTransition = g && A.options.useTransition;
            A.scroller.style[w + "TransitionProperty"] = A.options.useTransform ? "-" + w.toLowerCase() + "-transform": "top left";
            A.scroller.style[w + "TransitionDuration"] = "0ms";
            A.scroller.style[w + "TransformOrigin"] = "0 0";
            if (A.options.useTransition) {
                A.scroller.style[w + "TransitionTimingFunction"] = "cubic-bezier(0.33,0.66,0.66,1)"
            }
            if (A.options.useTransform) {
                A.scroller.style[w + "Transform"] = a + A.x + "px," + A.y + "px" + k
            } else {
                A.scroller.style.cssText += ";position:absolute;top:" + A.y + "px;left:" + A.x + "px"
            }
            A.refresh();
            A._bind(h, window);
            A._bind(b);
            if (!v) {
                A._bind("mouseout", A.wrapper)
            }
        };
    t.prototype = {
        enabled: true,
        x: 0,
        y: 0,
        steps: [],
        scale: 1,
        handleEvent: function(y) {
            var m = this;
            switch (y.type) {
                case b:
                    if (!v && y.button !== 0) {
                        return
                    }
                    m._start(y);
                    break;
                case r:
                    m._move(y);
                    break;
                case e:
                case u:
                    m._end(y);
                    break;
                case h:
                    m._resize();
                    break;
                case "mouseout":
                    m._mouseout(y);
                    break;
                case "webkitTransitionEnd":
                    m._transitionEnd(y);
                    break
            }
        },
        _resize: function() {
            this.refresh()
        },
        _pos: function(m, z) {
            m = this.hScroll ? m: 0;
            z = this.vScroll ? z: 0;
            if (this.options.useTransform) {
                this.scroller.style[w + "Transform"] = a + m + "px," + z + "px" + k + " scale(" + this.scale + ")"
            } else {
                m = d(m);
                z = d(z);
                this.scroller.style.left = m + "px";
                this.scroller.style.top = z + "px"
            }
            this.x = m;
            this.y = z;
            if (this.options.remarkXCoordinate) {
                jm.setCookie("startX", this.x);
                if (Object.prototype.toString.call(this.options.remarkNavLeftRight) === "[object Function]") {
                    this.options.remarkNavLeftRight(this.x, this)
                }
            }
        },
        _start: function(C) {
            var B = this,
                z = v ? C.touches[0] : C,
                A,
                m,
                D;
            if (!B.enabled) {
                return
            }
            if (B.options.onBeforeScrollStart) {
                B.options.onBeforeScrollStart.call(B, C)
            }
            if (B.options.useTransition) {
                B._transitionTime(0)
            }
            B.moved = false;
            B.animating = false;
            B.zoomed = false;
            B.distX = 0;
            B.distY = 0;
            B.absDistX = 0;
            B.absDistY = 0;
            B.dirX = 0;
            B.dirY = 0;
            if (B.options.momentum) {
                if (B.options.useTransform) {
                    A = getComputedStyle(B.scroller, null)[w + "Transform"].replace(/[^0-9-.,]/g, "").split(",");
                    m = A[4] * 1;
                    D = A[5] * 1
                } else {
                    m = getComputedStyle(B.scroller, null).left.replace(/[^0-9-]/g, "") * 1;
                    D = getComputedStyle(B.scroller, null).top.replace(/[^0-9-]/g, "") * 1
                }
                if (m != B.x || D != B.y) {
                    if (B.options.useTransition) {
                        B._unbind("webkitTransitionEnd")
                    } else {
                        o(B.aniTime)
                    }
                    B.steps = [];
                    B._pos(m, D)
                }
            }
            B.startX = B.x;
            B.startY = B.y;
            B.pointX = z.pageX;
            B.pointY = z.pageY;
            B.startTime = C.timeStamp || Date.now();
            if (B.options.onScrollStart) {
                B.options.onScrollStart.call(B, C)
            }
            B._bind(r);
            B._bind(e);
            B._bind(u)
        },
        _move: function(D) {
            var A = this,
                y = v ? D.touches[0] : D,
                z = y.pageX - A.pointX,
                m = y.pageY - A.pointY,
                E = A.x + z,
                C = A.y + m,
                B = D.timeStamp || Date.now();
            if (A.options.onBeforeScrollMove) {
                A.options.onBeforeScrollMove.call(A, D)
            }
            A.pointX = y.pageX;
            A.pointY = y.pageY;
            if (E > 0 || E < A.maxScrollX) {
                E = A.options.bounce ? A.x + (z / 2) : E >= 0 || A.maxScrollX >= 0 ? 0 : A.maxScrollX
            }
            if (C > 0 || C < A.maxScrollY) {
                C = A.options.bounce ? A.y + (m / 2) : C >= 0 || A.maxScrollY >= 0 ? 0 : A.maxScrollY
            }
            A.distX += z;
            A.distY += m;
            A.absDistX = s.abs(A.distX);
            A.absDistY = s.abs(A.distY);
            if (A.absDistX < 6 && A.absDistY < 6) {
                return
            }
            if (A.options.lockDirection) {
                if (A.absDistX > A.absDistY + 5) {
                    C = A.y;
                    m = 0
                } else {
                    if (A.absDistY > A.absDistX + 5) {
                        E = A.x;
                        z = 0
                    }
                }
            }
            A.moved = true;
            A._pos(E, C);
            A.dirX = z > 0 ? -1 : z < 0 ? 1 : 0;
            A.dirY = m > 0 ? -1 : m < 0 ? 1 : 0;
            if (B - A.startTime > 300) {
                A.startTime = B;
                A.startX = A.x;
                A.startY = A.y
            }
            if (A.options.onScrollMove) {
                A.options.onScrollMove.call(A, D)
            }
        },
        _end: function(D) {
            if (v && D.touches.length != 0) {
                return
            }
            var B = this,
                H = v ? D.changedTouches[0] : D,
                E,
                G,
                z = {
                    dist: 0,
                    time: 0
                },
                m = {
                    dist: 0,
                    time: 0
                },
                A = (D.timeStamp || Date.now()) - B.startTime,
                F = B.x,
                C = B.y,
                y;
            B._unbind(r);
            B._unbind(e);
            B._unbind(u);
            if (B.options.onBeforeScrollEnd) {
                B.options.onBeforeScrollEnd.call(B, D)
            }
            if (!B.moved) {
                if (v) {
                    E = H.target;
                    while (E.nodeType != 1) {
                        E = E.parentNode
                    }
                    if (E.tagName != "SELECT" && E.tagName != "INPUT" && E.tagName != "TEXTAREA") {
                        G = document.createEvent("MouseEvents");
                        G.initMouseEvent("click", true, true, D.view, 1, H.screenX, H.screenY, H.clientX, H.clientY, D.ctrlKey, D.altKey, D.shiftKey, D.metaKey, 0, null);
                        G._fake = true;
                        E.dispatchEvent(G)
                    }
                }
                B._resetPos(200);
                if (B.options.onTouchEnd) {
                    B.options.onTouchEnd.call(B, D)
                }
                return
            }
            if (A < 300 && B.options.momentum) {
                z = F ? B._momentum(F - B.startX, A, -B.x, B.scrollerW - B.wrapperW + B.x, B.options.bounce ? B.wrapperW: 0) : z;
                m = C ? B._momentum(C - B.startY, A, -B.y, (B.maxScrollY < 0 ? B.scrollerH - B.wrapperH + B.y: 0), B.options.bounce ? B.wrapperH: 0) : m;
                F = B.x + z.dist;
                C = B.y + m.dist;
                if ((B.x > 0 && F > 0) || (B.x < B.maxScrollX && F < B.maxScrollX)) {
                    z = {
                        dist: 0,
                        time: 0
                    }
                }
                if ((B.y > 0 && C > 0) || (B.y < B.maxScrollY && C < B.maxScrollY)) {
                    m = {
                        dist: 0,
                        time: 0
                    }
                }
            }
            if (z.dist || m.dist) {
                y = s.max(s.max(z.time, m.time), 10);
                B.scrollTo(d(F), d(C), y);
                if (B.options.onTouchEnd) {
                    B.options.onTouchEnd.call(B, D)
                }
                return
            }
            B._resetPos(200);
            if (B.options.onTouchEnd) {
                B.options.onTouchEnd.call(B, D)
            }
        },
        _resetPos: function(z) {
            var m = this,
                A = m.x >= 0 ? 0 : m.x < m.maxScrollX ? m.maxScrollX: m.x,
                y = m.y >= 0 || m.maxScrollY > 0 ? 0 : m.y < m.maxScrollY ? m.maxScrollY: m.y;
            if (A == m.x && y == m.y) {
                if (m.moved) {
                    if (m.options.onScrollEnd) {
                        m.options.onScrollEnd.call(m)
                    }
                    m.moved = false
                }
                return
            }
            m.scrollTo(A, y, z || 0)
        },
        _mouseout: function(y) {
            var m = y.relatedTarget;
            if (!m) {
                this._end(y);
                return
            }
            while (m = m.parentNode) {
                if (m == this.wrapper) {
                    return
                }
            }
            this._end(y)
        },
        _transitionEnd: function(y) {
            var m = this;
            if (y.target != m.scroller) {
                return
            }
            m._unbind("webkitTransitionEnd");
            m._startAni()
        },
        _startAni: function() {
            var D = this,
                y = D.x,
                m = D.y,
                B = Date.now(),
                C,
                A,
                z;
            if (D.animating) {
                return
            }
            if (!D.steps.length) {
                D._resetPos(400);
                return
            }
            C = D.steps.shift();
            if (C.x == y && C.y == m) {
                C.time = 0
            }
            D.animating = true;
            D.moved = true;
            if (D.options.useTransition) {
                D._transitionTime(C.time);
                D._pos(C.x, C.y);
                D.animating = false;
                if (C.time) {
                    D._bind("webkitTransitionEnd")
                } else {
                    D._resetPos(0)
                }
                return
            }
            z = function() {
                var E = Date.now(),
                    G,
                    F;
                if (E >= B + C.time) {
                    D._pos(C.x, C.y);
                    D.animating = false;
                    if (D.options.onAnimationEnd) {
                        D.options.onAnimationEnd.call(D)
                    }
                    D._startAni();
                    return
                }
                E = (E - B) / C.time - 1;
                A = s.sqrt(1 - E * E);
                G = (C.x - y) * A + y;
                F = (C.y - m) * A + m;
                D._pos(G, F);
                if (D.animating) {
                    D.aniTime = q(z)
                }
            };
            z()
        },
        _transitionTime: function(m) {
            this.scroller.style[w + "TransitionDuration"] = m + "ms"
        },
        _momentum: function(E, y, C, m, G) {
            var D = 0.0006,
                z = s.abs(E) / y,
                A = (z * z) / (2 * D),
                F = 0,
                B = 0;
            if (E > 0 && A > C) {
                B = G / (6 / (A / z * D));
                C = C + B;
                z = z * C / A;
                A = C
            } else {
                if (E < 0 && A > m) {
                    B = G / (6 / (A / z * D));
                    m = m + B;
                    z = z * m / A;
                    A = m
                }
            }
            A = A * (E < 0 ? -1 : 1);
            F = z / D;
            return {
                dist: A,
                time: d(F)
            }
        },
        _offset: function(m) {
            var z = -m.offsetLeft,
                y = -m.offsetTop;
            while (m = m.offsetParent) {
                z -= m.offsetLeft;
                y -= m.offsetTop
            }
            return {
                left: z,
                top: y
            }
        },
        _bind: function(z, y, m) { (y || this.scroller).addEventListener(z, this, !!m)
        },
        _unbind: function(z, y, m) { (y || this.scroller).removeEventListener(z, this, !!m)
        },
        destroy: function() {
            var m = this;
            m.scroller.style[w + "Transform"] = "";
            m._unbind(h, window);
            m._unbind(b);
            m._unbind(r);
            m._unbind(e);
            m._unbind(u);
            m._unbind("mouseout", m.wrapper);
            if (m.options.useTransition) {
                m._unbind("webkitTransitionEnd")
            }
            if (m.options.onDestroy) {
                m.options.onDestroy.call(m)
            }
        },
        refresh: function() {
            var m = this,
                y;
            m.wrapperW = m.wrapper.clientWidth;
            m.wrapperH = m.wrapper.clientHeight;
            m.scrollerW = m.scroller.offsetWidth;
            m.scrollerH = m.scroller.offsetHeight;
            m.maxScrollX = m.wrapperW - m.scrollerW;
            m.maxScrollY = m.wrapperH - m.scrollerH;
            m.dirX = 0;
            m.dirY = 0;
            m.hScroll = m.options.hScroll && m.maxScrollX < 0;
            m.vScroll = m.options.vScroll && (!m.options.bounceLock && !m.hScroll || m.scrollerH > m.wrapperH);
            y = m._offset(m.wrapper);
            m.wrapperOffsetLeft = -y.left;
            m.wrapperOffsetTop = -y.top;
            m.scroller.style[w + "TransitionDuration"] = "0";
            m._resetPos(200)
        },
        scrollTo: function(m, F, E, D) {
            var C = this,
                B = m,
                A, z;
            C.stop();
            if (!B.length) {
                B = [{
                    x: m,
                    y: F,
                    time: E,
                    relative: D
                }]
            }
            for (A = 0, z = B.length; A < z; A++) {
                if (B[A].relative) {
                    B[A].x = C.x - B[A].x;
                    B[A].y = C.y - B[A].y
                }
                C.steps.push({
                    x: B[A].x,
                    y: B[A].y,
                    time: B[A].time || 0
                })
            }
            C._startAni()
        },
        scrollToElement: function(m, z) {
            var y = this,
                A;
            m = m.nodeType ? m: y.scroller.querySelector(m);
            if (!m) {
                return
            }
            A = y._offset(m);
            A.left += y.wrapperOffsetLeft;
            A.top += y.wrapperOffsetTop;
            A.left = A.left > 0 ? 0 : A.left < y.maxScrollX ? y.maxScrollX: A.left;
            A.top = A.top > 0 ? 0 : A.top < y.maxScrollY ? y.maxScrollY: A.top;
            z = z === undefined ? s.max(s.abs(A.left) * 2, s.abs(A.top) * 2) : z;
            y.scrollTo(A.left, A.top, z)
        },
        disable: function() {
            this.stop();
            this._resetPos(0);
            this.enabled = false;
            this._unbind(r);
            this._unbind(e);
            this._unbind(u)
        },
        enable: function() {
            this.enabled = true
        },
        stop: function() {
            o(this.aniTime);
            this.steps = [];
            this.moved = false;
            this.animating = false
        }
    };
    if (typeof exports !== "undefined") {
        exports.iScroll = t
    } else {
        window.iScroll = t
    }
})();
/*!
  * klass: a classical JS OOP façade
  * https://github.com/ded/klass
  * License MIT (c) Dustin Diaz & Jacob Thornton 2012
  */
!
    function(a, c, b) {
        if (typeof define == "function") {
            define(b)
        } else {
            if (typeof module != "undefined") {
                module.exports = b()
            } else {
                c[a] = b()
            }
        }
    } ("klass", this,
        function() {
            var c = this,
                d = c.klass,
                h = "function",
                l = /xyz/.test(function() {
                    xyz
                }) ? /\bsupr\b/: /.*/,
                e = "prototype";
            function i(f) {
                return k.call(g(f) ? f: function() {},
                    f, 1)
            }
            function g(f) {
                return typeof f === h
            }
            function b(m, o, f) {
                return function() {
                    var q = this.supr;
                    this.supr = f[e][m];
                    var r = {}.fabricatedUndefined;
                    var p = r;
                    try {
                        p = o.apply(this, arguments)
                    } finally {
                        this.supr = q
                    }
                    return p
                }
            }
            function a(p, q, f) {
                for (var m in q) {
                    if (q.hasOwnProperty(m)) {
                        p[m] = g(q[m]) && g(f[e][m]) && l.test(q[m]) ? b(m, q[m], f) : q[m]
                    }
                }
            }
            function k(p, r) {
                function v() {}
                v[e] = this[e];
                var s = this,
                    u = new v(),
                    q = g(p),
                    f = q ? p: this,
                    m = q ? {}: p;
                function t() {
                    if (this.initialize) {
                        this.initialize.apply(this, arguments)
                    } else {
                        r || q && s.apply(this, arguments);
                        f.apply(this, arguments)
                    }
                }
                t.methods = function(w) {
                    a(u, w, s);
                    t[e] = u;
                    return this
                };
                t.methods.call(t, m).prototype.constructor = t;
                t.extend = arguments.callee;
                t[e].implement = t.statics = function(x, w) {
                    x = typeof x == "string" ? (function() {
                        var o = {};
                        o[x] = w;
                        return o
                    } ()) : x;
                    a(this, x, s);
                    return this
                };
                return t
            }
            i.noConflict = function() {
                c.klass = d;
                return this
            };
            return i
        }); (function(a) {
    if (!Function.prototype.bind) {
        Function.prototype.bind = function(e) {
            var f = [].slice,
                c = f.call(arguments, 1),
                b = this,
                g = function() {},
                d = function() {
                    return b.apply(this instanceof g ? this: (e || {}), c.concat(f.call(arguments)))
                };
            g.prototype = b.prototype;
            d.prototype = new g();
            return d
        }
    }
    if (typeof a.Code === "undefined") {
        a.Code = {}
    }
    a.Code.Util = {
        registerNamespace: function() {
            var g = arguments,
                e = null,
                f, d, h, c, k, b, l;
            for (f = 0, b = g.length; f < b; f++) {
                h = g[f];
                c = h.split(".");
                k = c[0];
                if (typeof a[k] === "undefined") {
                    a[k] = {}
                }
                e = a[k];
                for (d = 1, l = c.length; d < l; ++d) {
                    e[c[d]] = e[c[d]] || {};
                    e = e[c[d]]
                }
            }
        },
        coalesce: function() {
            var c, b;
            for (c = 0, b = arguments.length; c < b; c++) {
                if (!this.isNothing(arguments[c])) {
                    return arguments[c]
                }
            }
            return null
        },
        extend: function(b, c, d) {
            var e;
            if (this.isNothing(d)) {
                d = true
            }
            if (b && c && this.isObject(c)) {
                for (e in c) {
                    if (this.objectHasProperty(c, e)) {
                        if (d) {
                            b[e] = c[e]
                        } else {
                            if (typeof b[e] === "undefined") {
                                b[e] = c[e]
                            }
                        }
                    }
                }
            }
        },
        clone: function(c) {
            var b = {};
            this.extend(b, c);
            return b
        },
        isObject: function(b) {
            return b instanceof Object
        },
        isFunction: function(b) {
            return ({}).toString.call(b) === "[object Function]"
        },
        isArray: function(b) {
            return b instanceof Array
        },
        isLikeArray: function(b) {
            return typeof b.length === "number"
        },
        isNumber: function(b) {
            return typeof b === "number"
        },
        isString: function(b) {
            return typeof b === "string"
        },
        isNothing: function(b) {
            if (typeof b === "undefined" || b === null) {
                return true
            }
            return false
        },
        swapArrayElements: function(b, e, d) {
            var c = b[e];
            b[e] = b[d];
            b[d] = c
        },
        trim: function(b) {
            return b.replace(/^\s\s*/, "").replace(/\s\s*$/, "")
        },
        toCamelCase: function(b) {
            return b.replace(/(\-[a-z])/g,
                function(c) {
                    return c.toUpperCase().replace("-", "")
                })
        },
        toDashedCase: function(b) {
            return b.replace(/([A-Z])/g,
                function(c) {
                    return "-" + c.toLowerCase()
                })
        },
        arrayIndexOf: function(f, h, g) {
            var e, d, c, b;
            c = -1;
            for (e = 0, d = h.length; e < d; e++) {
                b = h[e];
                if (!this.isNothing(g)) {
                    if (this.objectHasProperty(b, g)) {
                        if (b[g] === f) {
                            c = e;
                            break
                        }
                    }
                } else {
                    if (b === f) {
                        c = e;
                        break
                    }
                }
            }
            return c
        },
        objectHasProperty: function(c, b) {
            if (c.hasOwnProperty) {
                return c.hasOwnProperty(b)
            } else {
                return ("undefined" !== typeof c[b])
            }
        }
    }
} (window)); (function(a, b) {
    b.Browser = {
        ua: null,
        version: null,
        safari: null,
        webkit: null,
        opera: null,
        msie: null,
        chrome: null,
        mozilla: null,
        android: null,
        blackberry: null,
        iPad: null,
        iPhone: null,
        iPod: null,
        iOS: null,
        is3dSupported: null,
        isCSSTransformSupported: null,
        isTouchSupported: null,
        isGestureSupported: null,
        _detect: function() {
            this.ua = a.navigator.userAgent;
            this.version = (this.ua.match(/.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || []);
            this.safari = (/Safari/gi).test(a.navigator.appVersion);
            this.webkit = /webkit/i.test(this.ua);
            this.opera = /opera/i.test(this.ua);
            this.msie = /msie/i.test(this.ua) && !this.opera;
            this.chrome = /Chrome/i.test(this.ua);
            this.firefox = /Firefox/i.test(this.ua);
            this.fennec = /Fennec/i.test(this.ua);
            this.mozilla = /mozilla/i.test(this.ua) && !/(compatible|webkit)/.test(this.ua);
            this.android = /android/i.test(this.ua);
            this.blackberry = /blackberry/i.test(this.ua);
            this.iOS = (/iphone|ipod|ipad/gi).test(a.navigator.platform);
            this.iPad = (/ipad/gi).test(a.navigator.platform);
            this.iPhone = (/iphone/gi).test(a.navigator.platform);
            this.iPod = (/ipod/gi).test(a.navigator.platform);
            var c = document.createElement("div");
            this.is3dSupported = !b.isNothing(c.style.WebkitPerspective);
            this.isCSSTransformSupported = (!b.isNothing(c.style.WebkitTransform) || !b.isNothing(c.style.MozTransform) || !b.isNothing(c.style.transformProperty));
            this.isTouchSupported = this.isEventSupported("touchstart");
            this.isGestureSupported = document.documentElement.ongesturestart !== "undefined" ? true: false
        },
        _eventTagNames: {
            select: "input",
            change: "input",
            submit: "form",
            reset: "form",
            error: "img",
            load: "img",
            abort: "img"
        },
        isEventSupported: function(c) {
            var e = document.createElement(this._eventTagNames[c] || "div"),
                d;
            c = "on" + c;
            d = b.objectHasProperty(e, c);
            if (!d) {
                e.setAttribute(c, "return;");
                d = typeof e[c] === "function"
            }
            e = null;
            return d
        },
        isLandscape: function() {
            return (b.DOM.windowWidth() > b.DOM.windowHeight())
        }
    };
    b.Browser._detect()
} (window, window.Code.Util)); (function(a, b) {
    b.extend(b, {
        Events: {
            add: function(e, d, c) {
                this._checkHandlersProperty(e);
                if (d === "mousewheel") {
                    d = this._normaliseMouseWheelType()
                }
                if (typeof e.__eventHandlers[d] === "undefined") {
                    e.__eventHandlers[d] = []
                }
                e.__eventHandlers[d].push(c);
                if (this._isBrowserObject(e)) {
                    e.addEventListener(d, c, false)
                }
            },
            remove: function(h, g, f) {
                this._checkHandlersProperty(h);
                if (g === "mousewheel") {
                    g = this._normaliseMouseWheelType()
                }
                if (h.__eventHandlers[g] instanceof Array) {
                    var e, d, c = h.__eventHandlers[g];
                    if (b.isNothing(f)) {
                        if (this._isBrowserObject(h)) {
                            for (e = 0, d = c.length; e < d; e++) {
                                h.removeEventListener(g, c[e], false)
                            }
                        }
                        h.__eventHandlers[g] = [];
                        return
                    }
                    for (e = 0, d = c.length; e < d; e++) {
                        if (c[e] === f) {
                            c.splice(e, 1);
                            break
                        }
                    }
                    if (this._isBrowserObject(h)) {
                        h.removeEventListener(g, f, false);
                        return
                    }
                }
            },
            fire: function(g, l) {
                var h, f, d, m, e, k = Array.prototype.slice.call(arguments).splice(2),
                    c;
                if (l === "mousewheel") {
                    l = this._normaliseMouseWheelType()
                }
                if (this._isBrowserObject(g)) {
                    if (typeof l !== "string") {
                        throw "type must be a string for DOM elements"
                    }
                    c = this._NATIVE_EVENTS[l];
                    d = document.createEvent(c ? "HTMLEvents": "UIEvents");
                    d[c ? "initEvent": "initUIEvent"](l, true, true, a, 1);
                    if (k.length < 1) {
                        g.dispatchEvent(d);
                        return
                    }
                }
                this._checkHandlersProperty(g);
                if (typeof l === "string") {
                    d = {
                        type: l
                    }
                } else {
                    d = l
                }
                if (!d.target) {
                    d.target = g
                }
                if (!d.type) {
                    throw new Error("Event object missing 'type' property.")
                }
                if (g.__eventHandlers[d.type] instanceof Array) {
                    m = g.__eventHandlers[d.type];
                    k.unshift(d);
                    for (h = 0, f = m.length; h < f; h++) {
                        e = m[h];
                        if (!b.isNothing(e)) {
                            e.apply(g, k)
                        }
                    }
                }
            },
            getMousePosition: function(d) {
                var c = {
                    x: 0,
                    y: 0
                };
                if (d.pageX) {
                    c.x = d.pageX
                } else {
                    if (d.clientX) {
                        c.x = d.clientX + (document.documentElement.scrollLeft || document.body.scrollLeft)
                    }
                }
                if (d.pageY) {
                    c.y = d.pageY
                } else {
                    if (d.clientY) {
                        c.y = d.clientY + (document.documentElement.scrollTop || document.body.scrollTop)
                    }
                }
                return c
            },
            getTouchEvent: function(c) {
                return c
            },
            getWheelDelta: function(c) {
                var d = 0;
                if (!b.isNothing(c.wheelDelta)) {
                    d = c.wheelDelta / 120
                } else {
                    if (!b.isNothing(c.detail)) {
                        d = -c.detail / 3
                    }
                }
                return d
            },
            domReady: function(c) {
                document.addEventListener("DOMContentLoaded", c, false)
            },
            _checkHandlersProperty: function(c) {
                if (b.isNothing(c.__eventHandlers)) {
                    b.extend(c, {
                        __eventHandlers: {}
                    })
                }
            },
            _isBrowserObject: function(c) {
                if (c === a || c === a.document) {
                    return true
                }
                return this._isElement(c) || this._isNode(c)
            },
            _isElement: function(c) {
                return (typeof a.HTMLElement === "object" ? c instanceof a.HTMLElement: typeof c === "object" && c.nodeType === 1 && typeof c.nodeName === "string")
            },
            _isNode: function(c) {
                return (typeof a.Node === "object" ? c instanceof a.Node: typeof c === "object" && typeof c.nodeType === "number" && typeof c.nodeName === "string")
            },
            _normaliseMouseWheelType: function() {
                if (b.Browser.isEventSupported("mousewheel")) {
                    return "mousewheel"
                }
                return "DOMMouseScroll"
            },
            _NATIVE_EVENTS: {
                click: 1,
                dblclick: 1,
                mouseup: 1,
                mousedown: 1,
                contextmenu: 1,
                mousewheel: 1,
                DOMMouseScroll: 1,
                mouseover: 1,
                mouseout: 1,
                mousemove: 1,
                selectstart: 1,
                selectend: 1,
                keydown: 1,
                keypress: 1,
                keyup: 1,
                orientationchange: 1,
                touchstart: 1,
                touchmove: 1,
                touchend: 1,
                touchcancel: 1,
                gesturestart: 1,
                gesturechange: 1,
                gestureend: 1,
                focus: 1,
                blur: 1,
                change: 1,
                reset: 1,
                select: 1,
                submit: 1,
                load: 1,
                unload: 1,
                beforeunload: 1,
                resize: 1,
                move: 1,
                DOMContentLoaded: 1,
                readystatechange: 1,
                error: 1,
                abort: 1,
                scroll: 1
            }
        }
    })
} (window, window.Code.Util)); (function(a, b) {
    b.extend(b, {
        DOM: {
            setData: function(f, e, g) {
                if (b.isLikeArray(f)) {
                    var d, c;
                    for (d = 0, c = f.length; d < c; d++) {
                        b.DOM._setData(f[d], e, g)
                    }
                } else {
                    b.DOM._setData(f, e, g)
                }
            },
            _setData: function(d, c, e) {
                b.DOM.setAttribute(d, "data-" + c, e)
            },
            getData: function(e, d, c) {
                return b.DOM.getAttribute(e, "data-" + d, c)
            },
            removeData: function(f, e) {
                if (b.isLikeArray(f)) {
                    var d, c;
                    for (d = 0, c = f.length; d < c; d++) {
                        b.DOM._removeData(f[d], e)
                    }
                } else {
                    b.DOM._removeData(f, e)
                }
            },
            _removeData: function(d, c) {
                b.DOM.removeAttribute(d, "data-" + c)
            },
            isChildOf: function(c, d) {
                if (d === c) {
                    return false
                }
                while (c && c !== d) {
                    c = c.parentNode
                }
                return c === d
            },
            find: function(g, h) {
                if (b.isNothing(h)) {
                    h = a.document
                }
                var f = h.querySelectorAll(g),
                    c = [],
                    e,
                    d;
                for (e = 0, d = f.length; e < d; e++) {
                    c.push(f[e])
                }
                return c
            },
            createElement: function(e, d, g) {
                var f, c = document.createElement(e);
                for (f in d) {
                    if (b.objectHasProperty(d, f)) {
                        c.setAttribute(f, d[f])
                    }
                }
                c.innerHTML = g || "";
                return c
            },
            appendChild: function(c, d) {
                d.appendChild(c)
            },
            insertBefore: function(e, c, d) {
                d.insertBefore(e, c)
            },
            appendText: function(d, c) {
                b.DOM.appendChild(document.createTextNode(d), c)
            },
            appendToBody: function(c) {
                this.appendChild(c, document.body)
            },
            removeChild: function(c, d) {
                d.removeChild(c)
            },
            removeChildren: function(c) {
                if (c.hasChildNodes()) {
                    while (c.childNodes.length >= 1) {
                        c.removeChild(c.childNodes[c.childNodes.length - 1])
                    }
                }
            },
            hasAttribute: function(d, c) {
                return ! b.isNothing(d.getAttribute(c))
            },
            getAttribute: function(f, e, d) {
                var c = f.getAttribute(e);
                if (b.isNothing(c) && !b.isNothing(d)) {
                    c = d
                }
                return c
            },
            setAttribute: function(f, d, g) {
                if (b.isLikeArray(f)) {
                    var e, c;
                    for (e = 0, c = f.length; e < c; e++) {
                        b.DOM._setAttribute(f[e], d, g)
                    }
                } else {
                    b.DOM._setAttribute(f, d, g)
                }
            },
            _setAttribute: function(d, c, e) {
                d.setAttribute(c, e)
            },
            removeAttribute: function(f, d) {
                if (b.isLikeArray(f)) {
                    var e, c;
                    for (e = 0, c = f.length; e < c; e++) {
                        b.DOM._removeAttribute(f[e], d)
                    }
                } else {
                    b.DOM._removeAttribute(f, d)
                }
            },
            _removeAttribute: function(d, c) {
                if (this.hasAttribute(d, c)) {
                    d.removeAttribute(c)
                }
            },
            addClass: function(f, e) {
                if (b.isLikeArray(f)) {
                    var d, c;
                    for (d = 0, c = f.length; d < c; d++) {
                        b.DOM._addClass(f[d], e)
                    }
                } else {
                    b.DOM._addClass(f, e)
                }
            },
            _addClass: function(f, e) {
                var c = b.DOM.getAttribute(f, "class", ""),
                    d = new RegExp("(?:^|\\s+)" + e + "(?:\\s+|$)");
                if (!d.test(c)) {
                    if (c !== "") {
                        c = c + " "
                    }
                    c = c + e;
                    b.DOM.setAttribute(f, "class", c)
                }
            },
            removeClass: function(f, e) {
                if (b.isLikeArray(f)) {
                    var d, c;
                    for (d = 0, c = f.length; d < c; d++) {
                        b.DOM._removeClass(f[d], e)
                    }
                } else {
                    b.DOM._removeClass(f, e)
                }
            },
            _removeClass: function(h, g) {
                var c = b.DOM.getAttribute(h, "class", ""),
                    f = b.trim(c).split(" "),
                    k = "",
                    e,
                    d;
                for (e = 0, d = f.length; e < d; e++) {
                    if (f[e] !== g) {
                        if (k !== "") {
                            k += " "
                        }
                        k += f[e]
                    }
                }
                if (k === "") {
                    b.DOM.removeAttribute(h, "class")
                } else {
                    b.DOM.setAttribute(h, "class", k)
                }
            },
            hasClass: function(e, d) {
                var c = new RegExp("(?:^|\\s+)" + d + "(?:\\s+|$)");
                return c.test(b.DOM.getAttribute(e, "class", ""))
            },
            setStyle: function(f, e, g) {
                if (b.isLikeArray(f)) {
                    var d, c;
                    for (d = 0, c = f.length; d < c; d++) {
                        b.DOM._setStyle(f[d], e, g)
                    }
                } else {
                    b.DOM._setStyle(f, e, g)
                }
            },
            _setStyle: function(d, c, e) {
                var g, f;
                if (b.isObject(c)) {
                    for (g in c) {
                        if (b.objectHasProperty(c, g)) {
                            if (g === "width") {
                                b.DOM.width(d, c[g])
                            } else {
                                if (g === "height") {
                                    b.DOM.height(d, c[g])
                                } else {
                                    d.style[g] = c[g]
                                }
                            }
                        }
                    }
                } else {
                    d.style[c] = e
                }
            },
            getStyle: function(e, d) {
                var c = a.getComputedStyle(e, "").getPropertyValue(d);
                if (c === "") {
                    c = e.style[d]
                }
                return c
            },
            hide: function(e) {
                if (b.isLikeArray(e)) {
                    var d, c;
                    for (d = 0, c = e.length; d < c; d++) {
                        b.DOM._hide(e[d])
                    }
                } else {
                    b.DOM._hide(e)
                }
            },
            _hide: function(c) {
                b.DOM.setData(c, "ccl-disp", b.DOM.getStyle(c, "display"));
                b.DOM.setStyle(c, "display", "none")
            },
            show: function(e) {
                if (b.isLikeArray(e)) {
                    var d, c;
                    for (d = 0, c = e.length; d < c; d++) {
                        b.DOM._show(e[d])
                    }
                } else {
                    b.DOM._show(e)
                }
            },
            _show: function(c) {
                if (b.DOM.getStyle(c, "display") === "none") {
                    var d = b.DOM.getData(c, "ccl-disp", "block");
                    if (d === "none" || d === "") {
                        d = "block"
                    }
                    b.DOM.setStyle(c, "display", d)
                }
            },
            width: function(c, d) {
                if (!b.isNothing(d)) {
                    if (b.isNumber(d)) {
                        d = d + "px"
                    }
                    c.style.width = d
                }
                return this._getDimension(c, "width")
            },
            outerWidth: function(d) {
                var c = b.DOM.width(d);
                c += parseInt(b.DOM.getStyle(d, "padding-left"), 10) + parseInt(b.DOM.getStyle(d, "padding-right"), 10);
                c += parseInt(b.DOM.getStyle(d, "margin-left"), 10) + parseInt(b.DOM.getStyle(d, "margin-right"), 10);
                c += parseInt(b.DOM.getStyle(d, "border-left-width"), 10) + parseInt(b.DOM.getStyle(d, "border-right-width"), 10);
                return c
            },
            height: function(c, d) {
                if (!b.isNothing(d)) {
                    if (b.isNumber(d)) {
                        d = d + "px"
                    }
                    c.style.height = d
                }
                return this._getDimension(c, "height")
            },
            _getDimension: function(e, f) {
                var c = a.parseInt(a.getComputedStyle(e, "").getPropertyValue(f)),
                    d;
                if (isNaN(c)) {
                    d = {
                        display: e.style.display,
                        left: e.style.left
                    };
                    e.style.display = "block";
                    e.style.left = "-1000000px";
                    c = a.parseInt(a.getComputedStyle(e, "").getPropertyValue(f));
                    e.style.display = d.display;
                    e.style.left = d.left
                }
                return c
            },
            outerHeight: function(d) {
                var c = b.DOM.height(d);
                c += parseInt(b.DOM.getStyle(d, "padding-top"), 10) + parseInt(b.DOM.getStyle(d, "padding-bottom"), 10);
                c += parseInt(b.DOM.getStyle(d, "margin-top"), 10) + parseInt(b.DOM.getStyle(d, "margin-bottom"), 10);
                c += parseInt(b.DOM.getStyle(d, "border-top-width"), 10) + parseInt(b.DOM.getStyle(d, "border-bottom-width"), 10);
                return c
            },
            documentWidth: function() {
                return b.DOM.width(document.documentElement)
            },
            documentHeight: function() {
                return b.DOM.height(document.documentElement)
            },
            documentOuterWidth: function() {
                return b.DOM.width(document.documentElement)
            },
            documentOuterHeight: function() {
                return b.DOM.outerHeight(document.documentElement)
            },
            bodyWidth: function() {
                return b.DOM.width(document.body)
            },
            bodyHeight: function() {
                return b.DOM.height(document.body)
            },
            bodyOuterWidth: function() {
                return b.DOM.outerWidth(document.body)
            },
            bodyOuterHeight: function() {
                return b.DOM.outerHeight(document.body)
            },
            windowWidth: function() {
                return a.innerWidth
            },
            windowHeight: function() {
                return a.innerHeight
            },
            windowScrollLeft: function() {
                return a.pageXOffset
            },
            windowScrollTop: function() {
                return a.pageYOffset
            }
        }
    })
} (window, window.Code.Util)); (function(a, b) {
    b.extend(b, {
        Animation: {
            _applyTransitionDelay: 50,
            _transitionEndLabel: (a.document.documentElement.style.webkitTransition !== undefined) ? "webkitTransitionEnd": "transitionend",
            _transitionEndHandler: null,
            _transitionPrefix: (a.document.documentElement.style.webkitTransition !== undefined) ? "webkitTransition": (a.document.documentElement.style.MozTransition !== undefined) ? "MozTransition": "transition",
            _transformLabel: (a.document.documentElement.style.webkitTransform !== undefined) ? "webkitTransform": (a.document.documentElement.style.MozTransition !== undefined) ? "MozTransform": "transform",
            _getTransitionEndHandler: function() {
                if (b.isNothing(this._transitionEndHandler)) {
                    this._transitionEndHandler = this._onTransitionEnd.bind(this)
                }
                return this._transitionEndHandler
            },
            stop: function(d) {
                if (b.Browser.isCSSTransformSupported) {
                    var e = d.style[this._transitionPrefix + "Property"],
                        f = (e !== "") ? "ccl" + e + "callback": "cclallcallback",
                        c = {};
                    b.Events.remove(d, this._transitionEndLabel, this._getTransitionEndHandler());
                    if (b.isNothing(d.callbackLabel)) {
                        delete d.callbackLabel
                    }
                    c[this._transitionPrefix + "Property"] = "";
                    c[this._transitionPrefix + "Duration"] = "";
                    c[this._transitionPrefix + "TimingFunction"] = "";
                    c[this._transitionPrefix + "Delay"] = "";
                    c[this._transformLabel] = "";
                    b.DOM.setStyle(d, c)
                } else {
                    if (!b.isNothing(a.jQuery)) {
                        a.jQuery(d).stop(true, true)
                    }
                }
            },
            fadeIn: function(e, g, h, f, d) {
                d = b.coalesce(d, 1);
                if (d <= 0) {
                    d = 1
                }
                if (g <= 0) {
                    b.DOM.setStyle(e, "opacity", d);
                    if (!b.isNothing(h)) {
                        h(e);
                        return
                    }
                }
                var c = b.DOM.getStyle(e, "opacity");
                if (c >= 1) {
                    b.DOM.setStyle(e, "opacity", 0)
                }
                if (b.Browser.isCSSTransformSupported) {
                    this._applyTransition(e, "opacity", d, g, h, f)
                } else {
                    if (!b.isNothing(a.jQuery)) {
                        a.jQuery(e).fadeTo(g, d, h)
                    }
                }
            },
            fadeTo: function(d, c, f, g, e) {
                this.fadeIn(d, f, g, e, c)
            },
            fadeOut: function(c, e, f, d) {
                if (e <= 0) {
                    b.DOM.setStyle(c, "opacity", 0);
                    if (!b.isNothing(f)) {
                        f(c);
                        return
                    }
                }
                if (b.Browser.isCSSTransformSupported) {
                    this._applyTransition(c, "opacity", 0, e, f, d)
                } else {
                    a.jQuery(c).fadeTo(e, 0, f)
                }
            },
            slideBy: function(e, c, i, g, h, f) {
                var d = {};
                c = b.coalesce(c, 0);
                i = b.coalesce(i, 0);
                f = b.coalesce(f, "ease-out");
                d[this._transitionPrefix + "Property"] = "all";
                d[this._transitionPrefix + "Delay"] = "0";
                if (g === 0) {
                    d[this._transitionPrefix + "Duration"] = "";
                    d[this._transitionPrefix + "TimingFunction"] = ""
                } else {
                    d[this._transitionPrefix + "Duration"] = g + "ms";
                    d[this._transitionPrefix + "TimingFunction"] = b.coalesce(f, "ease-out");
                    b.Events.add(e, this._transitionEndLabel, this._getTransitionEndHandler())
                }
                d[this._transformLabel] = (b.Browser.is3dSupported) ? "translate3d(" + c + "px, " + i + "px, 0px)": "translate(" + c + "px, " + i + "px)";
                if (!b.isNothing(h)) {
                    e.cclallcallback = h
                }
                b.DOM.setStyle(e, d);
                if (g === 0) {
                    a.setTimeout(function() {
                        this._leaveTransforms(e)
                    }.bind(this), this._applyTransitionDelay)
                }
            },
            resetTranslate: function(e) {
                var f = e.nodeName || "",
                    d = (f.toLowerCase() === "img") ? true: false,
                    c = {};
                if (!d) {
                    c[this._transformLabel] = c[this._transformLabel] = (b.Browser.is3dSupported) ? "translate3d(0px, 0px, 0px)": "translate(0px, 0px)";
                    b.DOM.setStyle(e, c)
                }
            },
            _applyTransition: function(d, g, h, f, i, e) {
                var c = {};
                e = b.coalesce(e, "ease-in");
                c[this._transitionPrefix + "Property"] = g;
                c[this._transitionPrefix + "Duration"] = f + "ms";
                c[this._transitionPrefix + "TimingFunction"] = e;
                c[this._transitionPrefix + "Delay"] = "0";
                b.Events.add(d, this._transitionEndLabel, this._getTransitionEndHandler());
                b.DOM.setStyle(d, c);
                if (!b.isNothing(i)) {
                    d["ccl" + g + "callback"] = i
                }
                a.setTimeout(function() {
                        b.DOM.setStyle(d, g, h)
                    },
                    this._applyTransitionDelay)
            },
            _onTransitionEnd: function(c) {
                b.Events.remove(c.currentTarget, this._transitionEndLabel, this._getTransitionEndHandler());
                this._leaveTransforms(c.currentTarget)
            },
            _leaveTransforms: function(f) {
                var m = f.style[this._transitionPrefix + "Property"],
                    l = (m !== "") ? "ccl" + m + "callback": "cclallcallback",
                    p,
                    g = b.coalesce(f.style.webkitTransform, f.style.MozTransform, f.style.transform),
                    i,
                    o,
                    k = a.parseInt(b.DOM.getStyle(f, "left"), 0),
                    h = a.parseInt(b.DOM.getStyle(f, "top"), 0),
                    e,
                    d,
                    c = {};
                if (g !== "") {
                    if (b.Browser.is3dSupported) {
                        i = g.match(/translate3d\((.*?)\)/)
                    } else {
                        i = g.match(/translate\((.*?)\)/)
                    }
                    if (!b.isNothing(i)) {
                        o = i[1].split(", ");
                        e = a.parseInt(o[0], 0);
                        d = a.parseInt(o[1], 0)
                    }
                }
                c[this._transitionPrefix + "Property"] = "";
                c[this._transitionPrefix + "Duration"] = "";
                c[this._transitionPrefix + "TimingFunction"] = "";
                c[this._transitionPrefix + "Delay"] = "";
                b.DOM.setStyle(f, c);
                a.setTimeout(function() {
                    if (!b.isNothing(o)) {
                        c = {};
                        c[this._transformLabel] = "";
                        c.left = (k + e) + "px";
                        c.top = (h + d) + "px";
                        b.DOM.setStyle(f, c)
                    }
                    if (!b.isNothing(f[l])) {
                        p = f[l];
                        delete f[l];
                        p(f)
                    }
                }.bind(this), this._applyTransitionDelay)
            }
        }
    })
} (window, window.Code.Util)); (function(b, a, c) {
    c.registerNamespace("Code.Util.TouchElement");
    c.TouchElement.EventTypes = {
        onTouch: "CodeUtilTouchElementOnTouch"
    };
    c.TouchElement.ActionTypes = {
        touchStart: "touchStart",
        touchMove: "touchMove",
        touchEnd: "touchEnd",
        touchMoveEnd: "touchMoveEnd",
        tap: "tap",
        doubleTap: "doubleTap",
        swipeLeft: "swipeLeft",
        swipeRight: "swipeRight",
        swipeUp: "swipeUp",
        swipeDown: "swipeDown",
        gestureStart: "gestureStart",
        gestureChange: "gestureChange",
        gestureEnd: "gestureEnd"
    }
} (window, window.klass, window.Code.Util)); (function(b, a, c) {
    c.registerNamespace("Code.Util.TouchElement");
    c.TouchElement.TouchElementClass = a({
        el: null,
        captureSettings: null,
        touchStartPoint: null,
        touchEndPoint: null,
        touchStartTime: null,
        doubleTapTimeout: null,
        touchStartHandler: null,
        touchMoveHandler: null,
        touchEndHandler: null,
        mouseDownHandler: null,
        mouseMoveHandler: null,
        mouseUpHandler: null,
        mouseOutHandler: null,
        gestureStartHandler: null,
        gestureChangeHandler: null,
        gestureEndHandler: null,
        swipeThreshold: null,
        swipeTimeThreshold: null,
        doubleTapSpeed: null,
        dispose: function() {
            var d;
            this.removeEventHandlers();
            for (d in this) {
                if (c.objectHasProperty(this, d)) {
                    this[d] = null
                }
            }
        },
        initialize: function(d, e) {
            this.el = d;
            this.captureSettings = {
                swipe: false,
                move: false,
                gesture: false,
                doubleTap: false,
                preventDefaultTouchEvents: true
            };
            c.extend(this.captureSettings, e);
            this.swipeThreshold = 50;
            this.swipeTimeThreshold = 250;
            this.doubleTapSpeed = 250;
            this.touchStartPoint = {
                x: 0,
                y: 0
            };
            this.touchEndPoint = {
                x: 0,
                y: 0
            };
            this.longTouchTimer
        },
        addEventHandlers: function() {
            if (c.isNothing(this.touchStartHandler)) {
                this.touchStartHandler = this.onTouchStart.bind(this);
                this.touchMoveHandler = this.onTouchMove.bind(this);
                this.touchEndHandler = this.onTouchEnd.bind(this);
                this.mouseDownHandler = this.onMouseDown.bind(this);
                this.mouseMoveHandler = this.onMouseMove.bind(this);
                this.mouseUpHandler = this.onMouseUp.bind(this);
                this.mouseOutHandler = this.onMouseOut.bind(this);
                this.gestureStartHandler = this.onGestureStart.bind(this);
                this.gestureChangeHandler = this.onGestureChange.bind(this);
                this.gestureEndHandler = this.onGestureEnd.bind(this)
            }
            c.Events.add(this.el, "touchstart", this.touchStartHandler);
            if (this.captureSettings.move) {
                c.Events.add(this.el, "touchmove", this.touchMoveHandler)
            }
            c.Events.add(this.el, "touchend", this.touchEndHandler);
            c.Events.add(this.el, "mousedown", this.mouseDownHandler);
            if (c.Browser.isGestureSupported && this.captureSettings.gesture) {
                c.Events.add(this.el, "gesturestart", this.gestureStartHandler);
                c.Events.add(this.el, "gesturechange", this.gestureChangeHandler);
                c.Events.add(this.el, "gestureend", this.gestureEndHandler)
            }
        },
        removeEventHandlers: function() {
            c.Events.remove(this.el, "touchstart", this.touchStartHandler);
            if (this.captureSettings.move) {
                c.Events.remove(this.el, "touchmove", this.touchMoveHandler)
            }
            c.Events.remove(this.el, "touchend", this.touchEndHandler);
            c.Events.remove(this.el, "mousedown", this.mouseDownHandler);
            if (c.Browser.isGestureSupported && this.captureSettings.gesture) {
                c.Events.remove(this.el, "gesturestart", this.gestureStartHandler);
                c.Events.remove(this.el, "gesturechange", this.gestureChangeHandler);
                c.Events.remove(this.el, "gestureend", this.gestureEndHandler)
            }
        },
        getTouchPoint: function(d) {
            return {
                x: d[0].pageX,
                y: d[0].pageY
            }
        },
        fireTouchEvent: function(l) {
            var k, f = 0,
                d = 0,
                m = 0,
                h, g, i;
            f = this.touchEndPoint.x - this.touchStartPoint.x;
            d = this.touchEndPoint.y - this.touchStartPoint.y;
            m = Math.sqrt((f * f) + (d * d));
            if (this.captureSettings.swipe) {
                g = new Date();
                i = g - this.touchStartTime;
                if (i <= this.swipeTimeThreshold) {
                    if (b.Math.abs(f) >= this.swipeThreshold) {
                        c.Events.fire(this, {
                            type: c.TouchElement.EventTypes.onTouch,
                            target: this,
                            point: this.touchEndPoint,
                            action: (f < 0) ? c.TouchElement.ActionTypes.swipeLeft: c.TouchElement.ActionTypes.swipeRight,
                            targetEl: l.target,
                            currentTargetEl: l.currentTarget
                        });
                        return
                    }
                    if (b.Math.abs(d) >= this.swipeThreshold) {
                        c.Events.fire(this, {
                            type: c.TouchElement.EventTypes.onTouch,
                            target: this,
                            point: this.touchEndPoint,
                            action: (d < 0) ? c.TouchElement.ActionTypes.swipeUp: c.TouchElement.ActionTypes.swipeDown,
                            targetEl: l.target,
                            currentTargetEl: l.currentTarget
                        });
                        return
                    }
                }
            }
            if (m > 1) {
                c.Events.fire(this, {
                    type: c.TouchElement.EventTypes.onTouch,
                    target: this,
                    action: c.TouchElement.ActionTypes.touchMoveEnd,
                    point: this.touchEndPoint,
                    targetEl: l.target,
                    currentTargetEl: l.currentTarget
                });
                return
            }
            if (!this.captureSettings.doubleTap) {
                c.Events.fire(this, {
                    type: c.TouchElement.EventTypes.onTouch,
                    target: this,
                    point: this.touchEndPoint,
                    action: c.TouchElement.ActionTypes.tap,
                    targetEl: l.target,
                    currentTargetEl: l.currentTarget
                });
                return
            }
            if (c.isNothing(this.doubleTapTimeout)) {
                this.doubleTapTimeout = b.setTimeout(function() {
                    this.doubleTapTimeout = null;
                    c.Events.fire(this, {
                        type: c.TouchElement.EventTypes.onTouch,
                        target: this,
                        point: this.touchEndPoint,
                        action: c.TouchElement.ActionTypes.tap,
                        targetEl: l.target,
                        currentTargetEl: l.currentTarget
                    })
                }.bind(this), this.doubleTapSpeed);
                return
            } else {
                b.clearTimeout(this.doubleTapTimeout);
                this.doubleTapTimeout = null;
                c.Events.fire(this, {
                    type: c.TouchElement.EventTypes.onTouch,
                    target: this,
                    point: this.touchEndPoint,
                    action: c.TouchElement.ActionTypes.doubleTap,
                    targetEl: l.target,
                    currentTargetEl: l.currentTarget
                })
            }
        },
        onTouchStart: function(g) {
            if (this.captureSettings.preventDefaultTouchEvents) {
                g.preventDefault()
            }
            c.Events.remove(this.el, "mousedown", this.mouseDownHandler);
            var d = c.Events.getTouchEvent(g),
                f = d.touches;
            if (f.length > 1 && this.captureSettings.gesture) {
                this.isGesture = true;
                return
            }
            this.touchStartTime = new Date();
            this.isGesture = false;
            this.touchStartPoint = this.getTouchPoint(f);
            c.Events.fire(this, {
                type: c.TouchElement.EventTypes.onTouch,
                target: this,
                action: c.TouchElement.ActionTypes.touchStart,
                point: this.touchStartPoint,
                targetEl: g.target,
                currentTargetEl: g.currentTarget
            })
        },
        onTouchMove: function(g) {
            if (this.captureSettings.preventDefaultTouchEvents) {
                g.preventDefault()
            }
            if (this.isGesture && this.captureSettings.gesture) {
                return
            }
            var d = c.Events.getTouchEvent(g),
                f = d.touches;
            c.Events.fire(this, {
                type: c.TouchElement.EventTypes.onTouch,
                target: this,
                action: c.TouchElement.ActionTypes.touchMove,
                point: this.getTouchPoint(f),
                targetEl: g.target,
                currentTargetEl: g.currentTarget
            })
        },
        onTouchEnd: function(g) {
            if (this.isGesture && this.captureSettings.gesture) {
                return
            }
            if (this.captureSettings.preventDefaultTouchEvents) {
                g.preventDefault()
            }
            var d = c.Events.getTouchEvent(g),
                f = (!c.isNothing(d.changedTouches)) ? d.changedTouches: d.touches;
            this.touchEndPoint = this.getTouchPoint(f);
            c.Events.fire(this, {
                type: c.TouchElement.EventTypes.onTouch,
                target: this,
                action: c.TouchElement.ActionTypes.touchEnd,
                point: this.touchEndPoint,
                targetEl: g.target,
                currentTargetEl: g.currentTarget
            });
            this.fireTouchEvent(g)
        },
        onMouseDown: function(d) {
            d.preventDefault();
            c.Events.remove(this.el, "touchstart", this.mouseDownHandler);
            c.Events.remove(this.el, "touchmove", this.touchMoveHandler);
            c.Events.remove(this.el, "touchend", this.touchEndHandler);
            if (this.captureSettings.move) {
                c.Events.add(this.el, "mousemove", this.mouseMoveHandler)
            }
            c.Events.add(this.el, "mouseup", this.mouseUpHandler);
            c.Events.add(this.el, "mouseout", this.mouseOutHandler);
            this.touchStartTime = new Date();
            this.isGesture = false;
            this.touchStartPoint = c.Events.getMousePosition(d);
            c.Events.fire(this, {
                type: c.TouchElement.EventTypes.onTouch,
                target: this,
                action: c.TouchElement.ActionTypes.touchStart,
                point: this.touchStartPoint,
                targetEl: d.target,
                currentTargetEl: d.currentTarget
            })
        },
        onMouseMove: function(d) {
            d.preventDefault();
            c.Events.fire(this, {
                type: c.TouchElement.EventTypes.onTouch,
                target: this,
                action: c.TouchElement.ActionTypes.touchMove,
                point: c.Events.getMousePosition(d),
                targetEl: d.target,
                currentTargetEl: d.currentTarget
            })
        },
        onMouseUp: function(d) {
            d.preventDefault();
            if (this.captureSettings.move) {
                c.Events.remove(this.el, "mousemove", this.mouseMoveHandler)
            }
            c.Events.remove(this.el, "mouseup", this.mouseUpHandler);
            c.Events.remove(this.el, "mouseout", this.mouseOutHandler);
            this.touchEndPoint = c.Events.getMousePosition(d);
            c.Events.fire(this, {
                type: c.TouchElement.EventTypes.onTouch,
                target: this,
                action: c.TouchElement.ActionTypes.touchEnd,
                point: this.touchEndPoint,
                targetEl: d.target,
                currentTargetEl: d.currentTarget
            });
            this.fireTouchEvent(d)
        },
        onMouseOut: function(f) {
            var d = f.relatedTarget;
            if (this.el === d || c.DOM.isChildOf(d, this.el)) {
                return
            }
            f.preventDefault();
            if (this.captureSettings.move) {
                c.Events.remove(this.el, "mousemove", this.mouseMoveHandler)
            }
            c.Events.remove(this.el, "mouseup", this.mouseUpHandler);
            c.Events.remove(this.el, "mouseout", this.mouseOutHandler);
            this.touchEndPoint = c.Events.getMousePosition(f);
            c.Events.fire(this, {
                type: c.TouchElement.EventTypes.onTouch,
                target: this,
                action: c.TouchElement.ActionTypes.touchEnd,
                point: this.touchEndPoint,
                targetEl: f.target,
                currentTargetEl: f.currentTarget
            });
            this.fireTouchEvent(f)
        },
        onGestureStart: function(f) {
            f.preventDefault();
            var d = c.Events.getTouchEvent(f);
            c.Events.fire(this, {
                type: c.TouchElement.EventTypes.onTouch,
                target: this,
                action: c.TouchElement.ActionTypes.gestureStart,
                scale: d.scale,
                rotation: d.rotation,
                targetEl: f.target,
                currentTargetEl: f.currentTarget
            })
        },
        onGestureChange: function(f) {
            f.preventDefault();
            var d = c.Events.getTouchEvent(f);
            c.Events.fire(this, {
                type: c.TouchElement.EventTypes.onTouch,
                target: this,
                action: c.TouchElement.ActionTypes.gestureChange,
                scale: d.scale,
                rotation: d.rotation,
                targetEl: f.target,
                currentTargetEl: f.currentTarget
            })
        },
        onGestureEnd: function(f) {
            f.preventDefault();
            var d = c.Events.getTouchEvent(f);
            c.Events.fire(this, {
                type: c.TouchElement.EventTypes.onTouch,
                target: this,
                action: c.TouchElement.ActionTypes.gestureEnd,
                scale: d.scale,
                rotation: d.rotation,
                targetEl: f.target,
                currentTargetEl: f.currentTarget
            })
        }
    })
} (window, window.klass, window.Code.Util)); (function(c, a, d) {
    d.registerNamespace("Code.PhotoSwipe.Image");
    var b = c.Code.PhotoSwipe;
    b.Image.EventTypes = {
        onLoad: "onLoad",
        onError: "onError"
    }
} (window, window.klass, window.Code.Util)); (function(c, a, d) {
    d.registerNamespace("Code.PhotoSwipe.Image");
    var b = c.Code.PhotoSwipe;
    b.Image.ImageClass = a({
        refObj: null,
        imageEl: null,
        src: null,
        caption: null,
        metaData: null,
        imageLoadHandler: null,
        imageErrorHandler: null,
        dispose: function() {
            var f, e;
            this.shrinkImage();
            for (f in this) {
                if (d.objectHasProperty(this, f)) {
                    this[f] = null
                }
            }
        },
        initialize: function(g, h, f, e) {
            this.refObj = g;
            this.originalSrc = h;
            this.src = h;
            this.caption = f;
            this.metaData = e;
            this.imageEl = new c.Image();
            this.imageLoadHandler = this.onImageLoad.bind(this);
            this.imageErrorHandler = this.onImageError.bind(this)
        },
        load: function() {
            this.imageEl.originalSrc = d.coalesce(this.imageEl.originalSrc, "");
            var e = this.refObj.getAttribute("data-source") ? this.refObj.getAttribute("data-source") : this.refObj.src;
            if (this.imageEl.originalSrc === e) {
                if (this.imageEl.isError) {
                    d.Events.fire(this, {
                        type: b.Image.EventTypes.onError,
                        target: this
                    })
                } else {
                    d.Events.fire(this, {
                        type: b.Image.EventTypes.onLoad,
                        target: this
                    })
                }
                return
            }
            this.imageEl.isError = false;
            this.imageEl.isLoading = true;
            this.imageEl.naturalWidth = null;
            this.imageEl.naturalHeight = null;
            this.imageEl.isLandscape = false;
            this.imageEl.onload = this.imageLoadHandler;
            this.imageEl.onerror = this.imageErrorHandler;
            this.imageEl.onabort = this.imageErrorHandler;
            this.imageEl.originalSrc = e;
            this.imageEl.src = e
        },
        shrinkImage: function() {
            if (d.isNothing(this.imageEl)) {
                return
            }
            if (this.imageEl.src.indexOf(this.src) > -1) {
                this.imageEl.src = "data:image/gif;base64,R0lGODlhAQABAAD/ACwAAAAAAQABAAACADs=";
                if (!d.isNothing(this.imageEl.parentNode)) {
                    d.DOM.removeChild(this.imageEl, this.imageEl.parentNode)
                }
            }
        },
        onImageLoad: function(f) {
            this.imageEl.onload = null;
            this.imageEl.naturalWidth = d.coalesce(this.imageEl.naturalWidth, this.imageEl.width);
            this.imageEl.naturalHeight = d.coalesce(this.imageEl.naturalHeight, this.imageEl.height);
            this.imageEl.isLandscape = (this.imageEl.naturalWidth > this.imageEl.naturalHeight);
            this.imageEl.isLoading = false;
            d.Events.fire(this, {
                type: b.Image.EventTypes.onLoad,
                target: this
            })
        },
        onImageError: function(f) {
            this.imageEl.onload = null;
            this.imageEl.onerror = null;
            this.imageEl.onabort = null;
            this.imageEl.isLoading = false;
            this.imageEl.isError = true;
            d.Events.fire(this, {
                type: b.Image.EventTypes.onError,
                target: this
            })
        }
    })
} (window, window.klass, window.Code.Util)); (function(c, a, d) {
    d.registerNamespace("Code.PhotoSwipe.Cache");
    var b = c.Code.PhotoSwipe;
    b.Cache.Mode = {
        normal: "normal",
        aggressive: "aggressive"
    };
    b.Cache.Functions = {
        getImageSource: function(e) {
            return e.href
        },
        getImageCaption: function(h) {
            if (h.nodeName === "IMG") {
                return d.DOM.getAttribute(h, "alt")
            }
            var g, f, e;
            for (g = 0, f = h.childNodes.length; g < f; g++) {
                e = h.childNodes[g];
                if (h.childNodes[g].nodeName === "IMG") {
                    return d.DOM.getAttribute(e, "alt")
                }
            }
        },
        getImageMetaData: function(e) {
            return {}
        }
    }
} (window, window.klass, window.Code.Util)); (function(c, a, d) {
    d.registerNamespace("Code.PhotoSwipe.Cache");
    var b = c.Code.PhotoSwipe;
    b.Cache.CacheClass = a({
        images: null,
        settings: null,
        dispose: function() {
            var g, f, e;
            if (!d.isNothing(this.images)) {
                for (f = 0, e = this.images.length; f < e; f++) {
                    this.images[f].dispose()
                }
                this.images.length = 0
            }
            for (g in this) {
                if (d.objectHasProperty(this, g)) {
                    this[g] = null
                }
            }
        },
        initialize: function(m, p) {
            var k, h, l, g, e, o, f;
            this.settings = p;
            this.images = [];
            for (k = 0, h = m.length; k < h; k++) {
                g = m[k];
                e = this.settings.getImageSource(g);
                o = this.settings.getImageCaption(k + 1, h);
                f = this.settings.getImageMetaData(g);
                this.images.push(new b.Image.ImageClass(g, e, o, f))
            }
        },
        getImages: function(h) {
            var k, g, f = [],
                e;
            for (k = 0, g = h.length; k < g; k++) {
                e = this.images[h[k]];
                if (this.settings.cacheMode === b.Cache.Mode.aggressive) {
                    e.cacheDoNotShrink = true
                }
                f.push(e)
            }
            if (this.settings.cacheMode === b.Cache.Mode.aggressive) {
                for (k = 0, g = this.images.length; k < g; k++) {
                    e = this.images[k];
                    if (!d.objectHasProperty(e, "cacheDoNotShrink")) {
                        e.shrinkImage()
                    } else {
                        delete e.cacheDoNotShrink
                    }
                }
            }
            return f
        }
    })
} (window, window.klass, window.Code.Util, window.Code.PhotoSwipe.Image)); (function(c, a, d) {
    d.registerNamespace("Code.PhotoSwipe.DocumentOverlay");
    var b = c.Code.PhotoSwipe;
    b.DocumentOverlay.CssClasses = {
        documentOverlay: "ps-document-overlay"
    }
} (window, window.klass, window.Code.Util)); (function(c, a, d) {
    d.registerNamespace("Code.PhotoSwipe.DocumentOverlay");
    var b = c.Code.PhotoSwipe;
    b.DocumentOverlay.DocumentOverlayClass = a({
        el: null,
        settings: null,
        initialBodyHeight: null,
        dispose: function() {
            var e;
            d.Animation.stop(this.el);
            d.DOM.removeChild(this.el, this.el.parentNode);
            for (e in this) {
                if (d.objectHasProperty(this, e)) {
                    this[e] = null
                }
            }
        },
        initialize: function(e) {
            this.settings = e;
            this.el = d.DOM.createElement("div", {
                    "class": b.DocumentOverlay.CssClasses.documentOverlay
                },
                "");
            d.DOM.setStyle(this.el, {
                display: "block",
                position: "absolute",
                left: 0,
                top: 0,
                zIndex: this.settings.zIndex
            });
            d.DOM.hide(this.el);
            if (this.settings.target === c) {
                d.DOM.appendToBody(this.el)
            } else {
                d.DOM.appendChild(this.el, this.settings.target)
            }
            d.Animation.resetTranslate(this.el);
            this.initialBodyHeight = d.DOM.bodyOuterHeight()
        },
        resetPosition: function() {
            var f, e, g;
            if (this.settings.target === c) {
                f = d.DOM.windowWidth();
                e = document.body.scrollHeight;
                g = (this.settings.jQueryMobile) ? d.DOM.windowScrollTop() + "px": "0px";
                if (e < 1) {
                    e = this.initialBodyHeight
                }
                if (d.DOM.windowHeight() > e) {
                    e = d.DOM.windowHeight()
                }
            } else {
                f = d.DOM.width(this.settings.target);
                e = d.DOM.height(this.settings.target);
                g = "0px"
            }
            d.DOM.setStyle(this.el, {
                width: f,
                height: e,
                top: g
            })
        },
        fadeIn: function(e, f) {
            this.resetPosition();
            d.DOM.setStyle(this.el, "opacity", 0);
            d.DOM.show(this.el);
            d.Animation.fadeIn(this.el, e, f)
        }
    })
} (window, window.klass, window.Code.Util)); (function(c, a, d) {
    d.registerNamespace("Code.PhotoSwipe.Carousel");
    var b = c.Code.PhotoSwipe;
    b.Carousel.EventTypes = {
        onSlideByEnd: "PhotoSwipeCarouselOnSlideByEnd",
        onSlideshowStart: "PhotoSwipeCarouselOnSlideshowStart",
        onSlideshowStop: "PhotoSwipeCarouselOnSlideshowStop"
    };
    b.Carousel.CssClasses = {
        carousel: "ps-carousel",
        content: "ps-carousel-content",
        item: "ps-carousel-item",
        itemLoading: "ps-carousel-item-loading",
        itemError: "ps-carousel-item-error"
    };
    b.Carousel.SlideByAction = {
        previous: "previous",
        current: "current",
        next: "next"
    }
} (window, window.klass, window.Code.Util)); (function(c, a, d) {
    d.registerNamespace("Code.PhotoSwipe.Carousel");
    var b = c.Code.PhotoSwipe;
    b.Carousel.CarouselClass = a({
        el: null,
        contentEl: null,
        settings: null,
        cache: null,
        slideByEndHandler: null,
        currentCacheIndex: null,
        isSliding: null,
        isSlideshowActive: null,
        lastSlideByAction: null,
        touchStartPoint: null,
        touchStartPosition: null,
        imageLoadHandler: null,
        imageErrorHandler: null,
        slideshowTimeout: null,
        dispose: function() {
            var g, f, e;
            for (f = 0, e = this.cache.images.length; f < e; f++) {
                d.Events.remove(this.cache.images[f], b.Image.EventTypes.onLoad, this.imageLoadHandler);
                d.Events.remove(this.cache.images[f], b.Image.EventTypes.onError, this.imageErrorHandler)
            }
            this.stopSlideshow();
            d.Animation.stop(this.el);
            d.DOM.removeChild(this.el, this.el.parentNode);
            for (g in this) {
                if (d.objectHasProperty(this, g)) {
                    this[g] = null
                }
            }
        },
        initialize: function(e, f) {
            var h, k, g;
            this.cache = e;
            this.settings = f;
            this.slideByEndHandler = this.onSlideByEnd.bind(this);
            this.imageLoadHandler = this.onImageLoad.bind(this);
            this.imageErrorHandler = this.onImageError.bind(this);
            this.currentCacheIndex = 0;
            this.isSliding = false;
            this.isSlideshowActive = false;
            if (this.cache.images.length < 3) {
                this.settings.loop = false
            }
            this.el = d.DOM.createElement("div", {
                    "class": b.Carousel.CssClasses.carousel
                },
                "");
            d.DOM.setStyle(this.el, {
                display: "block",
                position: "absolute",
                left: 0,
                top: 0,
                overflow: "hidden",
                zIndex: this.settings.zIndex
            });
            d.DOM.hide(this.el);
            this.contentEl = d.DOM.createElement("div", {
                    "class": b.Carousel.CssClasses.content
                },
                "");
            d.DOM.setStyle(this.contentEl, {
                display: "block",
                position: "absolute",
                left: 0,
                top: 0
            });
            d.DOM.appendChild(this.contentEl, this.el);
            k = (e.images.length < 3) ? e.images.length: 3;
            for (h = 0; h < k; h++) {
                g = d.DOM.createElement("div", {
                        "class": b.Carousel.CssClasses.item + " " + b.Carousel.CssClasses.item + "-" + h
                    },
                    "");
                d.DOM.setAttribute(g, "style", "float: left;");
                d.DOM.setStyle(g, {
                    display: "block",
                    position: "relative",
                    left: 0,
                    top: 0,
                    overflow: "hidden"
                });
                if (this.settings.margin > 0) {
                    d.DOM.setStyle(g, {
                        marginRight: this.settings.margin + "px"
                    })
                }
                d.DOM.appendChild(g, this.contentEl)
            }
            if (this.settings.target === c) {
                d.DOM.appendToBody(this.el)
            } else {
                d.DOM.appendChild(this.el, this.settings.target)
            }
        },
        resetPosition: function() {
            var f, q, o, l, m, e, k, h, p, g;
            if (this.settings.target === c) {
                f = d.DOM.windowWidth();
                q = d.DOM.windowHeight();
                o = d.DOM.windowScrollTop() + "px"
            } else {
                f = d.DOM.width(this.settings.target);
                q = d.DOM.height(this.settings.target);
                o = "0px"
            }
            l = (this.settings.margin > 0) ? f + this.settings.margin: f;
            m = d.DOM.find("." + b.Carousel.CssClasses.item, this.contentEl);
            e = l * m.length;
            d.DOM.setStyle(this.el, {
                top: o,
                width: f,
                height: q
            });
            d.DOM.setStyle(this.contentEl, {
                width: e,
                height: q
            });
            for (k = 0, h = m.length; k < h; k++) {
                p = m[k];
                d.DOM.setStyle(p, {
                    width: f,
                    height: q
                });
                g = d.DOM.find("img", p)[0];
                if (!d.isNothing(g)) {
                    this.resetImagePosition(g)
                }
            }
            this.setContentLeftPosition()
        },
        resetImagePosition: function(g) {
            if (d.isNothing(g)) {
                return
            }
            var e = d.DOM.getAttribute(g, "src"),
                h,
                i,
                f,
                k,
                l,
                o = d.DOM.width(this.el),
                m = d.DOM.height(this.el);
            if (this.settings.imageScaleMethod === "fitNoUpscale") {
                i = g.naturalWidth;
                f = g.naturalHeight;
                if (i > o) {
                    h = o / i;
                    i = Math.round(i * h);
                    f = Math.round(f * h)
                }
                if (f > m) {
                    h = m / f;
                    f = Math.round(f * h);
                    i = Math.round(i * h)
                }
            } else {
                if (g.isLandscape) {
                    h = o / g.naturalWidth
                } else {
                    h = m / g.naturalHeight
                }
                i = Math.round(g.naturalWidth * h);
                f = Math.round(g.naturalHeight * h);
                if (this.settings.imageScaleMethod === "zoom") {
                    h = 1;
                    if (f < m) {
                        h = m / f
                    } else {
                        if (i < o) {
                            h = o / i
                        }
                    }
                    if (h !== 1) {
                        i = Math.round(i * h);
                        f = Math.round(f * h)
                    }
                } else {
                    if (this.settings.imageScaleMethod === "fit") {
                        h = 1;
                        if (i > o) {
                            h = o / i
                        } else {
                            if (f > m) {
                                h = m / f
                            }
                        }
                        if (h !== 1) {
                            i = Math.round(i * h);
                            f = Math.round(f * h)
                        }
                    }
                }
            }
            k = Math.round(((m - f) / 2)) + "px";
            l = Math.round(((o - i) / 2)) + "px";
            d.DOM.setStyle(g, {
                position: "absolute",
                width: i,
                height: f,
                top: k,
                left: l,
                display: "block"
            })
        },
        setContentLeftPosition: function() {
            var e, g, f;
            if (this.settings.target === c) {
                e = d.DOM.windowWidth()
            } else {
                e = d.DOM.width(this.settings.target)
            }
            g = this.getItemEls();
            f = 0;
            if (this.settings.loop) {
                f = (e + this.settings.margin) * -1
            } else {
                if (this.currentCacheIndex === this.cache.images.length - 1) {
                    f = ((g.length - 1) * (e + this.settings.margin)) * -1
                } else {
                    if (this.currentCacheIndex > 0) {
                        f = (e + this.settings.margin) * -1
                    }
                }
            }
            d.DOM.setStyle(this.contentEl, {
                left: f + "px"
            })
        },
        show: function(f) {
            this.currentCacheIndex = f;
            this.resetPosition();
            this.setImages(false);
            d.DOM.show(this.el);
            d.Animation.resetTranslate(this.contentEl);
            var h = this.getItemEls(),
                g,
                e;
            for (g = 0, e = h.length; g < e; g++) {
                d.Animation.resetTranslate(h[g])
            }
            d.Events.fire(this, {
                type: b.Carousel.EventTypes.onSlideByEnd,
                target: this,
                action: b.Carousel.SlideByAction.current,
                cacheIndex: this.currentCacheIndex
            })
        },
        setImages: function(f) {
            var g, i = this.getItemEls(),
                e = this.currentCacheIndex + 1,
                h = this.currentCacheIndex - 1;
            if (this.settings.loop) {
                if (e > this.cache.images.length - 1) {
                    e = 0
                }
                if (h < 0) {
                    h = this.cache.images.length - 1
                }
                g = this.cache.getImages([h, this.currentCacheIndex, e]);
                if (!f) {
                    this.addCacheImageToItemEl(g[1], i[1])
                }
                this.addCacheImageToItemEl(g[2], i[2]);
                this.addCacheImageToItemEl(g[0], i[0])
            } else {
                if (i.length === 1) {
                    if (!f) {
                        g = this.cache.getImages([this.currentCacheIndex]);
                        this.addCacheImageToItemEl(g[0], i[0])
                    }
                } else {
                    if (i.length === 2) {
                        if (this.currentCacheIndex === 0) {
                            g = this.cache.getImages([this.currentCacheIndex, this.currentCacheIndex + 1]);
                            if (!f) {
                                this.addCacheImageToItemEl(g[0], i[0])
                            }
                            this.addCacheImageToItemEl(g[1], i[1])
                        } else {
                            g = this.cache.getImages([this.currentCacheIndex - 1, this.currentCacheIndex]);
                            if (!f) {
                                this.addCacheImageToItemEl(g[1], i[1])
                            }
                            this.addCacheImageToItemEl(g[0], i[0])
                        }
                    } else {
                        if (this.currentCacheIndex === 0) {
                            g = this.cache.getImages([this.currentCacheIndex, this.currentCacheIndex + 1, this.currentCacheIndex + 2]);
                            if (!f) {
                                this.addCacheImageToItemEl(g[0], i[0])
                            }
                            this.addCacheImageToItemEl(g[1], i[1]);
                            this.addCacheImageToItemEl(g[2], i[2])
                        } else {
                            if (this.currentCacheIndex === this.cache.images.length - 1) {
                                g = this.cache.getImages([this.currentCacheIndex - 2, this.currentCacheIndex - 1, this.currentCacheIndex]);
                                if (!f) {
                                    this.addCacheImageToItemEl(g[2], i[2])
                                }
                                this.addCacheImageToItemEl(g[1], i[1]);
                                this.addCacheImageToItemEl(g[0], i[0])
                            } else {
                                g = this.cache.getImages([this.currentCacheIndex - 1, this.currentCacheIndex, this.currentCacheIndex + 1]);
                                if (!f) {
                                    this.addCacheImageToItemEl(g[1], i[1])
                                }
                                this.addCacheImageToItemEl(g[2], i[2]);
                                this.addCacheImageToItemEl(g[0], i[0])
                            }
                        }
                    }
                }
            }
        },
        addCacheImageToItemEl: function(e, f) {
            d.DOM.removeClass(f, b.Carousel.CssClasses.itemError);
            d.DOM.addClass(f, b.Carousel.CssClasses.itemLoading);
            d.DOM.removeChildren(f);
            d.DOM.setStyle(e.imageEl, {
                display: "none"
            });
            d.DOM.appendChild(e.imageEl, f);
            d.Animation.resetTranslate(e.imageEl);
            d.Events.add(e, b.Image.EventTypes.onLoad, this.imageLoadHandler);
            d.Events.add(e, b.Image.EventTypes.onError, this.imageErrorHandler);
            e.load()
        },
        slideCarousel: function(e, k, i) {
            if (this.isSliding) {
                return
            }
            var h, g, f;
            if (this.settings.target === c) {
                h = d.DOM.windowWidth() + this.settings.margin
            } else {
                h = d.DOM.width(this.settings.target) + this.settings.margin
            }
            i = d.coalesce(i, this.settings.slideSpeed);
            if (c.Math.abs(g) < 1) {
                return
            }
            switch (k) {
                case d.TouchElement.ActionTypes.swipeLeft:
                    f = h * -1;
                    break;
                case d.TouchElement.ActionTypes.swipeRight:
                    f = h;
                    break;
                default:
                    g = e.x - this.touchStartPoint.x;
                    if (c.Math.abs(g) > h / 2) {
                        f = (g > 0) ? h: h * -1
                    } else {
                        f = 0
                    }
                    break
            }
            if (f < 0) {
                this.lastSlideByAction = b.Carousel.SlideByAction.next
            } else {
                if (f > 0) {
                    this.lastSlideByAction = b.Carousel.SlideByAction.previous
                } else {
                    this.lastSlideByAction = b.Carousel.SlideByAction.current
                }
            }
            if (!this.settings.loop) {
                if ((this.lastSlideByAction === b.Carousel.SlideByAction.previous && this.currentCacheIndex === 0) || (this.lastSlideByAction === b.Carousel.SlideByAction.next && this.currentCacheIndex === this.cache.images.length - 1)) {
                    f = 0;
                    this.lastSlideByAction = b.Carousel.SlideByAction.current
                }
            }
            this.isSliding = true;
            this.doSlideCarousel(f, i)
        },
        moveCarousel: function(e) {
            if (this.isSliding) {
                return
            }
            if (!this.settings.enableDrag) {
                return
            }
            this.doMoveCarousel(e.x - this.touchStartPoint.x)
        },
        getItemEls: function() {
            return d.DOM.find("." + b.Carousel.CssClasses.item, this.contentEl)
        },
        previous: function() {
            this.stopSlideshow();
            this.slideCarousel({
                    x: 0,
                    y: 0
                },
                d.TouchElement.ActionTypes.swipeRight, this.settings.nextPreviousSlideSpeed)
        },
        next: function() {
            this.stopSlideshow();
            this.slideCarousel({
                    x: 0,
                    y: 0
                },
                d.TouchElement.ActionTypes.swipeLeft, this.settings.nextPreviousSlideSpeed)
        },
        slideshowNext: function() {
            this.slideCarousel({
                    x: 0,
                    y: 0
                },
                d.TouchElement.ActionTypes.swipeLeft)
        },
        startSlideshow: function() {
            this.stopSlideshow();
            this.isSlideshowActive = true;
            this.slideshowTimeout = c.setTimeout(this.slideshowNext.bind(this), this.settings.slideshowDelay);
            d.Events.fire(this, {
                type: b.Carousel.EventTypes.onSlideshowStart,
                target: this
            })
        },
        stopSlideshow: function() {
            if (!d.isNothing(this.slideshowTimeout)) {
                c.clearTimeout(this.slideshowTimeout);
                this.slideshowTimeout = null;
                this.isSlideshowActive = false;
                d.Events.fire(this, {
                    type: b.Carousel.EventTypes.onSlideshowStop,
                    target: this
                })
            }
        },
        onSlideByEnd: function(f) {
            if (d.isNothing(this.isSliding)) {
                return
            }
            var g = this.getItemEls();
            this.isSliding = false;
            if (this.lastSlideByAction === b.Carousel.SlideByAction.next) {
                this.currentCacheIndex = this.currentCacheIndex + 1
            } else {
                if (this.lastSlideByAction === b.Carousel.SlideByAction.previous) {
                    this.currentCacheIndex = this.currentCacheIndex - 1
                }
            }
            if (this.settings.loop) {
                if (this.lastSlideByAction === b.Carousel.SlideByAction.next) {
                    d.DOM.appendChild(g[0], this.contentEl)
                } else {
                    if (this.lastSlideByAction === b.Carousel.SlideByAction.previous) {
                        d.DOM.insertBefore(g[g.length - 1], g[0], this.contentEl)
                    }
                }
                if (this.currentCacheIndex < 0) {
                    this.currentCacheIndex = this.cache.images.length - 1
                } else {
                    if (this.currentCacheIndex === this.cache.images.length) {
                        this.currentCacheIndex = 0
                    }
                }
            } else {
                if (this.cache.images.length > 3) {
                    if (this.currentCacheIndex > 1 && this.currentCacheIndex < this.cache.images.length - 2) {
                        if (this.lastSlideByAction === b.Carousel.SlideByAction.next) {
                            d.DOM.appendChild(g[0], this.contentEl)
                        } else {
                            if (this.lastSlideByAction === b.Carousel.SlideByAction.previous) {
                                d.DOM.insertBefore(g[g.length - 1], g[0], this.contentEl)
                            }
                        }
                    } else {
                        if (this.currentCacheIndex === 1) {
                            if (this.lastSlideByAction === b.Carousel.SlideByAction.previous) {
                                d.DOM.insertBefore(g[g.length - 1], g[0], this.contentEl)
                            }
                        } else {
                            if (this.currentCacheIndex === this.cache.images.length - 2) {
                                if (this.lastSlideByAction === b.Carousel.SlideByAction.next) {
                                    d.DOM.appendChild(g[0], this.contentEl)
                                }
                            }
                        }
                    }
                }
            }
            if (this.lastSlideByAction !== b.Carousel.SlideByAction.current) {
                this.setContentLeftPosition();
                this.setImages(true)
            }
            d.Events.fire(this, {
                type: b.Carousel.EventTypes.onSlideByEnd,
                target: this,
                action: this.lastSlideByAction,
                cacheIndex: this.currentCacheIndex
            });
            if (this.isSlideshowActive) {
                if (this.lastSlideByAction !== b.Carousel.SlideByAction.current) {
                    this.startSlideshow()
                } else {
                    this.stopSlideshow()
                }
            }
        },
        onTouch: function(f, e) {
            this.stopSlideshow();
            switch (f) {
                case d.TouchElement.ActionTypes.touchStart:
                    this.touchStartPoint = e;
                    this.touchStartPosition = {
                        x: c.parseInt(d.DOM.getStyle(this.contentEl, "left"), 0),
                        y: c.parseInt(d.DOM.getStyle(this.contentEl, "top"), 0)
                    };
                    this.longTouchTimer = setTimeout(function() {
                            c.location.href = b.instances[0].originalImages[b.instances[0].currentIndex].src
                        },
                        2000);
                    break;
                case d.TouchElement.ActionTypes.touchEnd:
                    clearTimeout(this.longTouchTimer);
                    break;
                case d.TouchElement.ActionTypes.touchMove:
                    this.moveCarousel(e);
                    break;
                case d.TouchElement.ActionTypes.touchMoveEnd:
                case d.TouchElement.ActionTypes.swipeLeft:
                case d.TouchElement.ActionTypes.swipeRight:
                    this.slideCarousel(e, f);
                    break;
                case d.TouchElement.ActionTypes.tap:
                    break;
                case d.TouchElement.ActionTypes.doubleTap:
                    break
            }
        },
        onImageLoad: function(g) {
            var f = g.target;
            if (!d.isNothing(f.imageEl.parentNode)) {
                d.DOM.removeClass(f.imageEl.parentNode, b.Carousel.CssClasses.itemLoading);
                this.resetImagePosition(f.imageEl)
            }
            d.Events.remove(f, b.Image.EventTypes.onLoad, this.imageLoadHandler);
            d.Events.remove(f, b.Image.EventTypes.onError, this.imageErrorHandler)
        },
        onImageError: function(g) {
            var f = g.target;
            if (!d.isNothing(f.imageEl.parentNode)) {
                d.DOM.removeClass(f.imageEl.parentNode, b.Carousel.CssClasses.itemLoading);
                d.DOM.addClass(f.imageEl.parentNode, b.Carousel.CssClasses.itemError)
            }
            d.Events.remove(f, b.Image.EventTypes.onLoad, this.imageLoadHandler);
            d.Events.remove(f, b.Image.EventTypes.onError, this.imageErrorHandler)
        }
    })
} (window, window.klass, window.Code.Util)); (function(d, a, e, b) {
    e.registerNamespace("Code.PhotoSwipe.Carousel");
    var c = d.Code.PhotoSwipe;
    c.Carousel.CarouselClass = c.Carousel.CarouselClass.extend({
        getStartingPos: function() {
            var f = this.touchStartPosition;
            if (e.isNothing(f)) {
                f = {
                    x: d.parseInt(e.DOM.getStyle(this.contentEl, "left"), 0),
                    y: d.parseInt(e.DOM.getStyle(this.contentEl, "top"), 0)
                }
            }
            return f
        },
        doMoveCarousel: function(g) {
            var f;
            if (e.Browser.isCSSTransformSupported) {
                f = {};
                f[e.Animation._transitionPrefix + "Property"] = "all";
                f[e.Animation._transitionPrefix + "Duration"] = "";
                f[e.Animation._transitionPrefix + "TimingFunction"] = "";
                f[e.Animation._transitionPrefix + "Delay"] = "0";
                f[e.Animation._transformLabel] = (e.Browser.is3dSupported) ? "translate3d(" + g + "px, 0px, 0px)": "translate(" + g + "px, 0px)";
                e.DOM.setStyle(this.contentEl, f)
            } else {
                if (!e.isNothing(d.jQuery)) {
                    d.jQuery(this.contentEl).stop().css("left", this.getStartingPos().x + g + "px")
                }
            }
        },
        doSlideCarousel: function(i, h) {
            var f, g;
            if (h <= 0) {
                this.slideByEndHandler();
                return
            }
            if (e.Browser.isCSSTransformSupported) {
                g = e.coalesce(this.contentEl.style.webkitTransform, this.contentEl.style.MozTransform, this.contentEl.style.transform, "");
                if (g.indexOf("translate3d(" + i) === 0) {
                    this.slideByEndHandler();
                    return
                } else {
                    if (g.indexOf("translate(" + i) === 0) {
                        this.slideByEndHandler();
                        return
                    }
                }
                e.Animation.slideBy(this.contentEl, i, 0, h, this.slideByEndHandler, this.settings.slideTimingFunction)
            } else {
                if (!e.isNothing(d.jQuery)) {
                    f = {
                        left: this.getStartingPos().x + i + "px"
                    };
                    if (this.settings.animationTimingFunction === "ease-out") {
                        this.settings.animationTimingFunction = "easeOutQuad"
                    }
                    if (e.isNothing(d.jQuery.easing[this.settings.animationTimingFunction])) {
                        this.settings.animationTimingFunction = "linear"
                    }
                    d.jQuery(this.contentEl).animate(f, this.settings.slideSpeed, this.settings.animationTimingFunction, this.slideByEndHandler)
                }
            }
        }
    })
} (window, window.klass, window.Code.Util, window.Code.PhotoSwipe.TouchElement)); (function(c, a, d) {
    d.registerNamespace("Code.PhotoSwipe.Toolbar");
    var b = c.Code.PhotoSwipe;
    b.Toolbar.CssClasses = {
        toolbar: "ps-toolbar",
        toolbarContent: "ps-toolbar-content",
        toolbarTop: "ps-toolbar-top",
        caption: "ps-caption",
        captionBottom: "ps-caption-bottom",
        captionContent: "ps-caption-content",
        close: "ps-toolbar-close",
        play: "ps-toolbar-play",
        previous: "ps-toolbar-previous",
        previousDisabled: "ps-toolbar-previous-disabled",
        next: "ps-toolbar-next",
        showDetailEl: "ps-toolbar-base-detail",
        nextDisabled: "ps-toolbar-next-disabled"
    };
    b.Toolbar.ToolbarAction = {
        close: "close",
        play: "play",
        next: "next",
        previous: "previous",
        none: "none",
        showDetail: "showDetail"
    };
    b.Toolbar.EventTypes = {
        onTap: "PhotoSwipeToolbarOnClick",
        onBeforeShow: "PhotoSwipeToolbarOnBeforeShow",
        onShow: "PhotoSwipeToolbarOnShow",
        onBeforeHide: "PhotoSwipeToolbarOnBeforeHide",
        onHide: "PhotoSwipeToolbarOnHide"
    };
    b.Toolbar.getToolbar = function() {
        return '<div class="' + b.Toolbar.CssClasses.close + '"><div class="' + b.Toolbar.CssClasses.toolbarContent + '"></div></div><div class="' + b.Toolbar.CssClasses.play + '"><div class="' + b.Toolbar.CssClasses.toolbarContent + '"></div></div><div class="' + b.Toolbar.CssClasses.previous + '"><div class="' + b.Toolbar.CssClasses.toolbarContent + '"></div></div><div class="' + b.Toolbar.CssClasses.next + '"><div class="' + b.Toolbar.CssClasses.toolbarContent + '"></div></div>'
    }
} (window, window.klass, window.Code.Util)); (function(c, a, d) {
    d.registerNamespace("Code.PhotoSwipe.Toolbar");
    var b = c.Code.PhotoSwipe;
    b.Toolbar.ToolbarClass = a({
        toolbarEl: null,
        closeEl: null,
        playEl: null,
        previousEl: null,
        nextEl: null,
        showDetailEl: null,
        captionEl: null,
        captionContentEl: null,
        currentCaption: null,
        settings: null,
        cache: null,
        timeout: null,
        isVisible: null,
        fadeOutHandler: null,
        touchStartHandler: null,
        touchMoveHandler: null,
        clickHandler: null,
        dispose: function() {
            var e;
            this.clearTimeout();
            this.removeEventHandlers();
            d.Animation.stop(this.toolbarEl);
            d.Animation.stop(this.captionEl);
            d.DOM.removeChild(this.toolbarEl, this.toolbarEl.parentNode);
            d.DOM.removeChild(this.captionEl, this.captionEl.parentNode);
            for (e in this) {
                if (d.objectHasProperty(this, e)) {
                    this[e] = null
                }
            }
        },
        initialize: function(f, g) {
            var e;
            this.settings = g;
            this.cache = f;
            this.isVisible = false;
            this.fadeOutHandler = this.onFadeOut.bind(this);
            this.touchStartHandler = this.onTouchStart.bind(this);
            this.touchMoveHandler = this.onTouchMove.bind(this);
            this.clickHandler = this.onClick.bind(this);
            e = b.Toolbar.CssClasses.toolbar;
            if (this.settings.captionAndToolbarFlipPosition) {
                e = e + " " + b.Toolbar.CssClasses.toolbarTop
            }
            this.toolbarEl = d.DOM.createElement("div", {
                    "class": e
                },
                this.settings.getToolbar());
            d.DOM.setStyle(this.toolbarEl, {
                left: 0,
                position: "absolute",
                overflow: "hidden",
                zIndex: this.settings.zIndex
            });
            if (this.settings.target === c) {
                d.DOM.appendToBody(this.toolbarEl)
            } else {
                d.DOM.appendChild(this.toolbarEl, this.settings.target)
            }
            d.DOM.hide(this.toolbarEl);
            this.closeEl = d.DOM.find("." + b.Toolbar.CssClasses.close, this.toolbarEl)[0];
            if (this.settings.preventHide && !d.isNothing(this.closeEl)) {
                d.DOM.hide(this.closeEl)
            }
            this.playEl = d.DOM.find("." + b.Toolbar.CssClasses.play, this.toolbarEl)[0];
            if (this.settings.preventSlideshow && !d.isNothing(this.playEl)) {
                d.DOM.hide(this.playEl)
            }
            this.nextEl = d.DOM.find("." + b.Toolbar.CssClasses.next, this.toolbarEl)[0];
            this.previousEl = d.DOM.find("." + b.Toolbar.CssClasses.previous, this.toolbarEl)[0];
            this.showDetailEl = d.DOM.find("." + b.Toolbar.CssClasses.showDetailEl, this.toolbarEl)[0];
            e = b.Toolbar.CssClasses.caption;
            if (this.settings.captionAndToolbarFlipPosition) {
                e = e + " " + b.Toolbar.CssClasses.captionBottom
            }
            this.captionEl = d.DOM.createElement("div", {
                    "class": e
                },
                "");
            d.DOM.setStyle(this.captionEl, {
                left: 0,
                position: "absolute",
                overflow: "hidden",
                zIndex: this.settings.zIndex
            });
            if (this.settings.target === c) {
                d.DOM.appendToBody(this.captionEl)
            } else {
                d.DOM.appendChild(this.captionEl, this.settings.target)
            }
            d.DOM.hide(this.captionEl);
            this.captionContentEl = d.DOM.createElement("div", {
                    "class": b.Toolbar.CssClasses.captionContent
                },
                "");
            d.DOM.appendChild(this.captionContentEl, this.captionEl);
            this.addEventHandlers()
        },
        insertToolbarText: function(i) {
            if (!i) {
                i = {
                    name: "",
                    basic: ""
                }
            }
            if (!i.name) {
                i.name = ""
            }
            if (!i.basic) {
                i.basic = ""
            }
            var g = document.getElementById("wrapperTitle");
            var h = document.getElementById("scrollerBasic");
            var f = i.mobiDetail === 0 ? false: true;
            if (!i.name.trim()) {
                g.style.display = "none"
            } else {
                g.style.display = "";
                g.innerHTML = i.name
            }
            if (!i.basic.trim()) {
                h.style.display = "none"
            } else {
                h.style.display = "";
                h.innerHTML = i.basic
            }
            var e = document.getElementById("wrapperTitleDetail");
            if (!f) {
                e.style.display = "none"
            } else {
                e.style.display = ""
            }
            if (i.aid > 14240000) {
                g.style.display = "none"
            }
        },
        resetPosition: function() {
            var f, e, g;
            if (this.settings.target === c) {
                if (this.settings.captionAndToolbarFlipPosition) {
                    e = d.DOM.windowScrollTop();
                    g = (d.DOM.windowScrollTop() + d.DOM.windowHeight()) - d.DOM.height(this.captionEl)
                } else {
                    e = (d.DOM.windowScrollTop() + d.DOM.windowHeight()) - d.DOM.height(this.toolbarEl);
                    g = d.DOM.windowScrollTop()
                }
                f = d.DOM.windowWidth()
            } else {
                if (this.settings.captionAndToolbarFlipPosition) {
                    e = "0";
                    g = d.DOM.height(this.settings.target) - d.DOM.height(this.captionEl)
                } else {
                    e = d.DOM.height(this.settings.target) - d.DOM.height(this.toolbarEl);
                    g = 0
                }
                f = d.DOM.width(this.settings.target)
            }
            d.DOM.setStyle(this.toolbarEl, {
                top: e + "px",
                width: f
            });
            d.DOM.setStyle(this.captionEl, {
                top: g + "px",
                width: f
            })
        },
        toggleVisibility: function(e) {
            if (this.isVisible) {
                this.fadeOut()
            } else {
                this.show(e)
            }
        },
        show: function(e) {
            d.Animation.stop(this.toolbarEl);
            d.Animation.stop(this.captionEl);
            this.resetPosition();
            this.setToolbarStatus(e);
            d.Events.fire(this, {
                type: b.Toolbar.EventTypes.onBeforeShow,
                target: this
            });
            this.showToolbar();
            this.setCaption(e);
            this.showCaption();
            this.isVisible = true;
            this.setTimeout();
            d.Events.fire(this, {
                type: b.Toolbar.EventTypes.onShow,
                target: this
            })
        },
        setTimeout: function() {
            if (this.settings.captionAndToolbarAutoHideDelay > 0) {
                this.clearTimeout();
                this.timeout = c.setTimeout(this.fadeOut.bind(this), this.settings.captionAndToolbarAutoHideDelay)
            }
        },
        clearTimeout: function() {
            if (!d.isNothing(this.timeout)) {
                c.clearTimeout(this.timeout);
                this.timeout = null
            }
        },
        fadeOut: function() {
            this.clearTimeout();
            d.Events.fire(this, {
                type: b.Toolbar.EventTypes.onBeforeHide,
                target: this
            });
            d.Animation.fadeOut(this.toolbarEl, this.settings.fadeOutSpeed);
            d.Animation.fadeOut(this.captionEl, this.settings.fadeOutSpeed, this.fadeOutHandler);
            this.isVisible = false
        },
        addEventHandlers: function() {
            if (d.Browser.isTouchSupported) {
                if (!d.Browser.blackberry) {
                    d.Events.add(this.toolbarEl, "touchstart", this.touchStartHandler)
                }
                d.Events.add(this.toolbarEl, "touchmove", this.touchMoveHandler);
                d.Events.add(this.captionEl, "touchmove", this.touchMoveHandler)
            }
            d.Events.add(this.toolbarEl, "click", this.clickHandler)
        },
        removeEventHandlers: function() {
            if (d.Browser.isTouchSupported) {
                if (!d.Browser.blackberry) {
                    d.Events.remove(this.toolbarEl, "touchstart", this.touchStartHandler)
                }
                d.Events.remove(this.toolbarEl, "touchmove", this.touchMoveHandler);
                d.Events.remove(this.captionEl, "touchmove", this.touchMoveHandler)
            }
            d.Events.remove(this.toolbarEl, "click", this.clickHandler)
        },
        handleTap: function(g) {
            this.clearTimeout();
            var f;
            if (g.target === this.nextEl || d.DOM.isChildOf(g.target, this.nextEl)) {
                f = b.Toolbar.ToolbarAction.next
            } else {
                if (g.target === this.previousEl || d.DOM.isChildOf(g.target, this.previousEl)) {
                    f = b.Toolbar.ToolbarAction.previous
                } else {
                    if (g.target === this.closeEl || d.DOM.isChildOf(g.target, this.closeEl)) {
                        f = b.Toolbar.ToolbarAction.close
                    } else {
                        if (g.target === this.playEl || d.DOM.isChildOf(g.target, this.playEl)) {
                            f = b.Toolbar.ToolbarAction.play
                        } else {
                            if (g.target === this.showDetailEl || d.DOM.isChildOf(g.target, this.showDetailEl)) {
                                f = b.Toolbar.ToolbarAction.showDetail
                            }
                        }
                    }
                }
            }
            this.setTimeout();
            if (d.isNothing(f)) {
                f = b.Toolbar.ToolbarAction.none
            }
            d.Events.fire(this, {
                type: b.Toolbar.EventTypes.onTap,
                target: this,
                action: f,
                tapTarget: g.target
            })
        },
        setCaption: function(e) {
            d.DOM.removeChildren(this.captionContentEl);
            this.currentCaption = d.coalesce(this.cache.images[e].caption, "\u00A0");
            if (d.isObject(this.currentCaption)) {
                d.DOM.appendChild(this.currentCaption, this.captionContentEl)
            } else {
                if (this.currentCaption === "") {
                    this.currentCaption = "\u00A0"
                }
                d.DOM.appendText(this.currentCaption, this.captionContentEl)
            }
            this.currentCaption = (this.currentCaption === "\u00A0") ? "": this.currentCaption;
            this.resetPosition()
        },
        showToolbar: function() {
            d.DOM.setStyle(this.toolbarEl, {
                opacity: this.settings.captionAndToolbarOpacity
            });
            d.DOM.show(this.toolbarEl)
        },
        showCaption: function() {
            if (this.currentCaption === "" || this.captionContentEl.childNodes.length < 1) {
                if (!this.settings.captionAndToolbarShowEmptyCaptions) {
                    d.DOM.hide(this.captionEl);
                    return
                }
            }
            d.DOM.setStyle(this.captionEl, {
                opacity: this.settings.captionAndToolbarOpacity
            });
            d.DOM.show(this.captionEl)
        },
        setToolbarStatus: function(e) {
            if (this.settings.loop) {
                return
            }
        },
        onFadeOut: function() {
            d.DOM.hide(this.toolbarEl);
            d.DOM.hide(this.captionEl);
            d.Events.fire(this, {
                type: b.Toolbar.EventTypes.onHide,
                target: this
            })
        },
        onTouchStart: function(f) {
            f.preventDefault();
            d.Events.remove(this.toolbarEl, "click", this.clickHandler);
            this.handleTap(f)
        },
        onTouchMove: function(f) {
            f.preventDefault()
        },
        onClick: function(f) {
            f.preventDefault();
            this.handleTap(f);
            event.stopPropagation()
        }
    })
} (window, window.klass, window.Code.Util)); (function(c, a, d) {
    d.registerNamespace("Code.PhotoSwipe.UILayer");
    var b = c.Code.PhotoSwipe;
    b.UILayer.CssClasses = {
        uiLayer: "ps-uilayer"
    }
} (window, window.klass, window.Code.Util)); (function(c, a, d) {
    d.registerNamespace("Code.PhotoSwipe.UILayer");
    var b = c.Code.PhotoSwipe;
    b.UILayer.UILayerClass = d.TouchElement.TouchElementClass.extend({
        el: null,
        settings: null,
        dispose: function() {
            var e;
            this.removeEventHandlers();
            d.DOM.removeChild(this.el, this.el.parentNode);
            for (e in this) {
                if (d.objectHasProperty(this, e)) {
                    this[e] = null
                }
            }
        },
        initialize: function(e) {
            this.settings = e;
            this.el = d.DOM.createElement("div", {
                    "class": b.UILayer.CssClasses.uiLayer
                },
                "");
            d.DOM.setStyle(this.el, {
                display: "block",
                position: "absolute",
                left: 0,
                top: 0,
                overflow: "hidden",
                zIndex: this.settings.zIndex,
                opacity: 0
            });
            d.DOM.hide(this.el);
            if (this.settings.target === c) {
                d.DOM.appendToBody(this.el)
            } else {
                d.DOM.appendChild(this.el, this.settings.target)
            }
            this.supr(this.el, {
                swipe: true,
                move: true,
                gesture: true,
                doubleTap: true,
                preventDefaultTouchEvents: this.settings.preventDefaultTouchEvents
            })
        },
        resetPosition: function() {
            if (this.settings.target === c) {
                d.DOM.setStyle(this.el, {
                    top: d.DOM.windowScrollTop() + "px",
                    width: d.DOM.windowWidth(),
                    height: d.DOM.windowHeight()
                })
            } else {
                d.DOM.setStyle(this.el, {
                    top: "0px",
                    width: d.DOM.width(this.settings.target),
                    height: d.DOM.height(this.settings.target)
                })
            }
        },
        show: function() {
            this.resetPosition();
            d.DOM.show(this.el);
            this.addEventHandlers()
        },
        hide: function() {
            d.DOM.setStyle(this.el, {
                display: "none"
            });
            this.removeEventHandlers()
        },
        addEventHandlers: function() {
            this.supr()
        },
        removeEventHandlers: function() {
            this.supr()
        }
    })
} (window, window.klass, window.Code.Util)); (function(c, a, d) {
    d.registerNamespace("Code.PhotoSwipe.UIDetail");
    var b = c.Code.PhotoSwipe;
    b.UIDetail.CssClasses = {
        uiDetail: "ps-uidetail"
    };
    b.UIDetail.Id = {
        id: "ps-uidetail-id"
    }
} (window, window.klass, window.Code.Util)); (function(c, a, d) {
    d.registerNamespace("Code.PhotoSwipe.UIDetail");
    var b = c.Code.PhotoSwipe;
    b.UIDetail.UIDetailClass = d.TouchElement.TouchElementClass.extend({
        el: null,
        settings: null,
        dispose: function() {
            var e;
            this.removeEventHandlers();
            d.DOM.removeChild(this.el, this.el.parentNode);
            for (e in this) {
                if (d.objectHasProperty(this, e)) {
                    this[e] = null
                }
            }
        },
        initialize: function(e) {
            this.settings = e;
            this.el = d.DOM.createElement("div", {
                    id: b.UIDetail.Id.id,
                    "class": b.UIDetail.CssClasses.uiDetail
                },
                "");
            this.elChlid = d.DOM.createElement("div", {
                    id: "ui-detail-child-id",
                    "class": "uiDetailChild ps-carousel-item-loading"
                },
                "");
            this.elChlidContent = d.DOM.createElement("div", {
                    id: "ui-detail-content-id",
                    "class": "uiDetailContent"
                },
                "");
            this.elChlidTitlePanel = d.DOM.createElement("div", {
                    id: "ui-detail-title-panel-id",
                    "class": "uiDetailTitlePanel"
                },
                "");
            this.elChlidTitleName = d.DOM.createElement("div", {
                    id: "ui-detail-title-name-id",
                    "class": "ps-toolbar-base-title"
                },
                "");
            this.elChlidTitle = d.DOM.createElement("div", {
                    id: "ui-detail-title-id",
                    "class": "uiDetailTitle icon-uiDetailTitle"
                },
                "");
            d.DOM.appendChild(this.elChlid, this.el);
            d.DOM.appendChild(this.elChlidTitlePanel, this.el);
            d.DOM.appendChild(this.elChlidTitleName, this.elChlidTitlePanel);
            d.DOM.appendChild(this.elChlidTitle, this.elChlidTitlePanel);
            d.DOM.appendChild(this.elChlidContent, this.elChlid);
            d.DOM.setStyle(this.el, {
                display: "none",
                position: "absolute",
                left: 0,
                top: "100%",
                overflow: "hidden",
                zIndex: this.settings.zIndex
            });
            if (this.settings.target === c) {
                d.DOM.appendToBody(this.el)
            } else {
                d.DOM.appendChild(this.el, this.settings.target)
            }
            this.supr(this.el, {
                swipe: true,
                move: true,
                gesture: true,
                doubleTap: true,
                preventDefaultTouchEvents: this.settings.preventDefaultTouchEvents
            })
        },
        resetPosition: function() {
            if (this.settings.target === c) {
                d.DOM.setStyle(this.el, {
                    top: d.DOM.windowScrollTop() + "px",
                    width: d.DOM.windowWidth(),
                    height: d.DOM.windowHeight()
                })
            } else {
                d.DOM.setStyle(this.el, {
                    top: "0px",
                    width: d.DOM.width(this.settings.target),
                    height: d.DOM.height(this.settings.target)
                })
            }
        },
        hide: function() {
            d.DOM.setStyle(this.el, {
                display: "none"
            });
            this.removeEventHandlers()
        },
        show: function() {
            this.resetPosition();
            if (Object.prototype.toString.call(this.settings.uiDetailCallBack) === "[object Function]") {
                this.settings.uiDetailCallBack(this.elChlidContent, this.elChlid, this.elChlidTitleName)
            }
            d.DOM.show(this.el);
            this.addEventHandlers()
        },
        addEventHandlers: function() {
            var e = this;
            if (jm.os.supportsTouch) {
                this.elChlidTitle.ontouchstart = function() {
                    e.hide()
                };
                this.elChlidTitle.ontouchmove = function() {
                    return false
                };
                this.elChlidTitle.ontouchend = function() {
                    return false
                }
            } else {
                this.elChlidTitle.onclick = function() {
                    e.hide()
                }
            }
            this.supr()
        },
        removeEventHandlers: function() {
            if (jm.os.supportsTouch) {
                this.elChlidTitle.ontouchstart = null;
                this.elChlidTitle.ontouchmove = null;
                this.elChlidTitle.ontouchend = null
            } else {
                this.elChlidTitle.onclick = null
            }
            this.supr()
        }
    })
} (window, window.klass, window.Code.Util)); (function(c, a, d) {
    d.registerNamespace("Code.PhotoSwipe.ZoomPanRotate");
    var b = c.Code.PhotoSwipe;
    b.ZoomPanRotate.CssClasses = {
        zoomPanRotate: "ps-zoom-pan-rotate"
    };
    b.ZoomPanRotate.EventTypes = {
        onTransform: "PhotoSwipeZoomPanRotateOnTransform"
    }
} (window, window.klass, window.Code.Util)); (function(c, a, d) {
    d.registerNamespace("Code.PhotoSwipe.ZoomPanRotate");
    var b = c.Code.PhotoSwipe;
    b.ZoomPanRotate.ZoomPanRotateClass = a({
        el: null,
        settings: null,
        containerEl: null,
        imageEl: null,
        transformSettings: null,
        panStartingPoint: null,
        transformEl: null,
        dispose: function() {
            var e;
            d.DOM.removeChild(this.el, this.el.parentNode);
            for (e in this) {
                if (d.objectHasProperty(this, e)) {
                    this[e] = null
                }
            }
        },
        initialize: function(g, f, k) {
            var i, h, e, l;
            this.settings = g;
            if (this.settings.target === c) {
                i = document.body;
                h = d.DOM.windowWidth();
                e = d.DOM.windowHeight();
                l = d.DOM.windowScrollTop() + "px"
            } else {
                i = this.settings.target;
                h = d.DOM.width(i);
                e = d.DOM.height(i);
                l = "0px"
            }
            this.imageEl = f.imageEl.cloneNode(false);
            d.DOM.setStyle(this.imageEl, {
                zIndex: 1
            });
            this.transformSettings = {
                startingScale: 1,
                scale: 1,
                startingRotation: 0,
                rotation: 0,
                startingTranslateX: 0,
                startingTranslateY: 0,
                translateX: 0,
                translateY: 0
            };
            this.el = d.DOM.createElement("div", {
                    "class": b.ZoomPanRotate.CssClasses.zoomPanRotate
                },
                "");
            d.DOM.setStyle(this.el, {
                left: 0,
                top: l,
                position: "absolute",
                width: h,
                height: e,
                zIndex: this.settings.zIndex,
                display: "block"
            });
            d.DOM.insertBefore(this.el, k.el, i);
            if (d.Browser.iOS) {
                this.containerEl = d.DOM.createElement("div", "", "");
                d.DOM.setStyle(this.containerEl, {
                    left: 0,
                    top: 0,
                    width: h,
                    height: e,
                    position: "absolute",
                    zIndex: 1
                });
                d.DOM.appendChild(this.imageEl, this.containerEl);
                d.DOM.appendChild(this.containerEl, this.el);
                d.Animation.resetTranslate(this.containerEl);
                d.Animation.resetTranslate(this.imageEl);
                this.transformEl = this.containerEl
            } else {
                d.DOM.appendChild(this.imageEl, this.el);
                this.transformEl = this.imageEl
            }
        },
        setStartingTranslateFromCurrentTransform: function() {
            var e = d.coalesce(this.transformEl.style.webkitTransform, this.transformEl.style.MozTransform, this.transformEl.style.transform),
                f;
            if (!d.isNothing(e)) {
                f = e.match(/translate\((.*?)\)/);
                if (!d.isNothing(f)) {
                    f = f[1].split(", ");
                    this.transformSettings.startingTranslateX = c.parseInt(f[0], 10);
                    this.transformSettings.startingTranslateY = c.parseInt(f[1], 10)
                }
            }
        },
        getScale: function(f) {
            var e = this.transformSettings.startingScale * f;
            if (this.settings.minUserZoom !== 0 && e < this.settings.minUserZoom) {
                e = this.settings.minUserZoom
            } else {
                if (this.settings.maxUserZoom !== 0 && e > this.settings.maxUserZoom) {
                    e = this.settings.maxUserZoom
                }
            }
            return e
        },
        setStartingScaleAndRotation: function(f, e) {
            this.transformSettings.startingScale = this.getScale(f);
            this.transformSettings.startingRotation = (this.transformSettings.startingRotation + e) % 360
        },
        zoomRotate: function(f, e) {
            this.transformSettings.scale = this.getScale(f);
            this.transformSettings.rotation = this.transformSettings.startingRotation + e;
            this.applyTransform()
        },
        panStart: function(e) {
            this.setStartingTranslateFromCurrentTransform();
            this.panStartingPoint = {
                x: e.x,
                y: e.y
            }
        },
        pan: function(e) {
            var g = e.x - this.panStartingPoint.x,
                f = e.y - this.panStartingPoint.y,
                h = g / this.transformSettings.scale,
                i = f / this.transformSettings.scale;
            this.transformSettings.translateX = this.transformSettings.startingTranslateX + h;
            this.transformSettings.translateY = this.transformSettings.startingTranslateY + i;
            this.applyTransform()
        },
        zoomAndPanToPoint: function(k, e) {
            if (this.settings.target === c) {
                this.panStart({
                    x: d.DOM.windowWidth() / 2,
                    y: d.DOM.windowHeight() / 2
                });
                var g = e.x - this.panStartingPoint.x,
                    f = e.y - this.panStartingPoint.y,
                    h = g / this.transformSettings.scale,
                    i = f / this.transformSettings.scale;
                this.transformSettings.translateX = (this.transformSettings.startingTranslateX + h) * -1;
                this.transformSettings.translateY = (this.transformSettings.startingTranslateY + i) * -1
            }
            this.setStartingScaleAndRotation(k, 0);
            this.transformSettings.scale = this.transformSettings.startingScale;
            this.transformSettings.rotation = 0;
            this.applyTransform()
        },
        applyTransform: function() {
            var h = this.transformSettings.rotation % 360,
                g = c.parseInt(this.transformSettings.translateX, 10),
                f = c.parseInt(this.transformSettings.translateY, 10),
                e = "scale(" + this.transformSettings.scale + ") rotate(" + h + "deg) translate(" + g + "px, " + f + "px)";
            d.DOM.setStyle(this.transformEl, {
                webkitTransform: e,
                MozTransform: e,
                msTransform: e,
                transform: e
            });
            d.Events.fire(this, {
                target: this,
                type: b.ZoomPanRotate.EventTypes.onTransform,
                scale: this.transformSettings.scale,
                rotation: this.transformSettings.rotation,
                rotationDegs: h,
                translateX: g,
                translateY: f
            })
        }
    })
} (window, window.klass, window.Code.Util)); (function(b, c) {
    c.registerNamespace("Code.PhotoSwipe");
    var a = b.Code.PhotoSwipe;
    a.CssClasses = {
        buildingBody: "ps-building",
        activeBody: "ps-active"
    };
    a.EventTypes = {
        onBeforeShow: "PhotoSwipeOnBeforeShow",
        onShow: "PhotoSwipeOnShow",
        onBeforeHide: "PhotoSwipeOnBeforeHide",
        onHide: "PhotoSwipeOnHide",
        onDisplayImage: "PhotoSwipeOnDisplayImage",
        onResetPosition: "PhotoSwipeOnResetPosition",
        onSlideshowStart: "PhotoSwipeOnSlideshowStart",
        onSlideshowStop: "PhotoSwipeOnSlideshowStop",
        onTouch: "PhotoSwipeOnTouch",
        onBeforeCaptionAndToolbarShow: "PhotoSwipeOnBeforeCaptionAndToolbarShow",
        onCaptionAndToolbarShow: "PhotoSwipeOnCaptionAndToolbarShow",
        onBeforeCaptionAndToolbarHide: "PhotoSwipeOnBeforeCaptionAndToolbarHide",
        onCaptionAndToolbarHide: "PhotoSwipeOnCaptionAndToolbarHide",
        onToolbarTap: "PhotoSwipeOnToolbarTap",
        onBeforeZoomPanRotateShow: "PhotoSwipeOnBeforeZoomPanRotateShow",
        onZoomPanRotateShow: "PhotoSwipeOnZoomPanRotateShow",
        onBeforeZoomPanRotateHide: "PhotoSwipeOnBeforeZoomPanRotateHide",
        onZoomPanRotateHide: "PhotoSwipeOnZoomPanRotateHide",
        onZoomPanRotateTransform: "PhotoSwipeOnZoomPanRotateTransform"
    };
    a.instances = [];
    a.activeInstances = [];
    a.setActivateInstance = function(d) {
        var e = c.arrayIndexOf(d.settings.target, a.activeInstances, "target");
        if (e > -1) {
            throw "Code.PhotoSwipe.activateInstance: Unable to active instance as another instance is already active for this target"
        }
        a.activeInstances.push({
            target: d.settings.target,
            instance: d
        })
    };
    a.unsetActivateInstance = function(d) {
        var e = c.arrayIndexOf(d, a.activeInstances, "instance");
        a.activeInstances.splice(e, 1)
    };
    a.attach = function(m, p, e) {
        var k, h, o, g, f, l;
        for (f = 0, l = m.length; f < l; f++) {
            if (!c.DOM.getAttribute(m[f], "src")) {
                var d = c.DOM.getAttribute(m[f], "lazysrc");
                if ( !! d) {
                    c.DOM.setAttribute(m[f], "src", d)
                }
            }
        }
        o = a.createInstance(m, p, e);
        for (k = 0, h = m.length; k < h; k++) {
            g = m[k];
            if (!c.isNothing(g.nodeType)) {
                if (jm(g).parents("#newsDetailPanel a, .richContent  a, .productDetailTabPanel a").length >= 1) {} else {
                    if (g.parentNode && (g.parentNode.nodeName === "A" && g.parentNode.href || g.parentNode.parentNode.nodeName === "A" && g.parentNode.parentNode.href)) {
                        continue
                    }
                    if (g.nodeType === 1) {
                        g.__photoSwipeClickHandler = a.onTriggerElementClick.bind(o);
                        c.Events.remove(g, "click", g.__photoSwipeClickHandler);
                        c.Events.add(g, "click", g.__photoSwipeClickHandler)
                    }
                }
            }
        }
        return o
    };
    if (b.jQuery) {
        b.jQuery.fn.photoSwipe = function(d, e) {
            return a.attach(this, d, e)
        }
    }
    a.detatch = function(d) {
        var f, e, g;
        for (f = 0, e = d.originalImages.length; f < e; f++) {
            g = d.originalImages[f];
            if (!c.isNothing(g.nodeType)) {
                if (g.nodeType === 1) {
                    c.Events.remove(g, "click", g.__photoSwipeClickHandler);
                    delete g.__photoSwipeClickHandler
                }
            }
        }
        a.disposeInstance(d)
    };
    a.createInstance = function(e, f, k) {
        var g, d, h;
        if (c.isNothing(e)) {
            throw "Code.PhotoSwipe.attach: No images passed."
        }
        if (!c.isLikeArray(e)) {
            throw "Code.PhotoSwipe.createInstance: Images must be an array of elements or image urls."
        }
        if (e.length < 1) {
            return null
        }
        f = c.coalesce(f, {});
        d = a.getInstance(k);
        if (c.isNothing(d)) {
            d = new a.PhotoSwipeClass(e, f, k);
            a.instances.push(d)
        } else {
            throw 'Code.PhotoSwipe.createInstance: Instance with id "' + k + ' already exists."'
        }
        return d
    };
    a.disposeInstance = function(d) {
        var e = a.getInstanceIndex(d);
        if (e < 0) {
            throw "Code.PhotoSwipe.disposeInstance: Unable to find instance to dispose."
        }
        d.dispose();
        a.instances.splice(e, 1);
        d = null
    };
    a.onTriggerElementClick = function(f) {
        f.preventDefault();
        var d = this;
        d.show(f.currentTarget)
    };
    a.getInstance = function(g) {
        var f, e, d;
        for (f = 0, e = a.instances.length; f < e; f++) {
            d = a.instances[f];
            if (d.id === g) {
                return d
            }
        }
        return null
    };
    a.getInstanceIndex = function(d) {
        var g, f, e = -1;
        for (g = 0, f = a.instances.length; g < f; g++) {
            if (a.instances[g] === d) {
                e = g;
                break
            }
        }
        return e
    }
} (window, window.Code.Util)); (function(f, g, i, k, e, h, d, l, b, c) {
    i.registerNamespace("Code.PhotoSwipe");
    var a = f.Code.PhotoSwipe;
    a.PhotoSwipeClass = g({
        id: null,
        settings: null,
        isBackEventSupported: null,
        backButtonClicked: null,
        currentIndex: null,
        originalImages: null,
        mouseWheelStartTime: null,
        windowDimensions: null,
        cache: null,
        documentOverlay: null,
        carousel: null,
        uiLayer: null,
        uiDetail: null,
        toolbar: null,
        zoomPanRotate: null,
        windowOrientationChangeHandler: null,
        windowScrollHandler: null,
        windowHashChangeHandler: null,
        keyDownHandler: null,
        windowOrientationEventName: null,
        uiLayerTouchHandler: null,
        carouselSlideByEndHandler: null,
        carouselSlideshowStartHandler: null,
        carouselSlideshowStopHandler: null,
        toolbarTapHandler: null,
        toolbarBeforeShowHandler: null,
        toolbarShowHandler: null,
        toolbarBeforeHideHandler: null,
        toolbarHideHandler: null,
        mouseWheelHandler: null,
        zoomPanRotateTransformHandler: null,
        _isResettingPosition: null,
        _uiWebViewResetPositionTimeout: null,
        dispose: function() {
            var m;
            i.Events.remove(this, a.EventTypes.onBeforeShow);
            i.Events.remove(this, a.EventTypes.onShow);
            i.Events.remove(this, a.EventTypes.onBeforeHide);
            i.Events.remove(this, a.EventTypes.onHide);
            i.Events.remove(this, a.EventTypes.onDisplayImage);
            i.Events.remove(this, a.EventTypes.onResetPosition);
            i.Events.remove(this, a.EventTypes.onSlideshowStart);
            i.Events.remove(this, a.EventTypes.onSlideshowStop);
            i.Events.remove(this, a.EventTypes.onTouch);
            i.Events.remove(this, a.EventTypes.onBeforeCaptionAndToolbarShow);
            i.Events.remove(this, a.EventTypes.onCaptionAndToolbarShow);
            i.Events.remove(this, a.EventTypes.onBeforeCaptionAndToolbarHide);
            i.Events.remove(this, a.EventTypes.onCaptionAndToolbarHide);
            i.Events.remove(this, a.EventTypes.onZoomPanRotateTransform);
            this.removeEventHandlers();
            if (!i.isNothing(this.documentOverlay)) {
                this.documentOverlay.dispose()
            }
            if (!i.isNothing(this.carousel)) {
                this.carousel.dispose()
            }
            if (!i.isNothing(this.uiLayer)) {
                this.uiLayer.dispose()
            }
            if (!i.isNothing(this.uiDetail)) {
                this.uiDetail.dispose()
            }
            if (!i.isNothing(this.toolbar)) {
                this.toolbar.dispose()
            }
            this.destroyZoomPanRotate();
            if (!i.isNothing(this.cache)) {
                this.cache.dispose()
            }
            for (m in this) {
                if (i.objectHasProperty(this, m)) {
                    this[m] = null
                }
            }
        },
        initialize: function(m, o, q) {
            var p;
            if (i.isNothing(q)) {
                this.id = "PhotoSwipe" + new Date().getTime().toString()
            } else {
                this.id = q
            }
            this.originalImages = m;
            if (i.Browser.android && !i.Browser.firefox) {
                if (f.navigator.userAgent.match(/Android (\d+.\d+)/).toString().replace(/^.*\,/, "") >= 2.1) {
                    this.isBackEventSupported = true
                }
            }
            if (!this.isBackEventSupported) {
                this.isBackEventSupported = i.objectHasProperty(f, "onhashchange")
            }
            this.settings = {
                fadeInSpeed: 250,
                fadeOutSpeed: 250,
                preventHide: false,
                preventSlideshow: false,
                zIndex: 1000,
                backButtonHideEnabled: true,
                enableKeyboard: true,
                enableMouseWheel: true,
                mouseWheelSpeed: 350,
                autoStartSlideshow: false,
                jQueryMobile: (!i.isNothing(f.jQuery) && !i.isNothing(f.jQuery.mobile)),
                jQueryMobileDialogHash: "&ui-state=dialog",
                enableUIWebViewRepositionTimeout: false,
                uiWebViewResetPositionDelay: 500,
                target: f,
                preventDefaultTouchEvents: true,
                loop: true,
                slideSpeed: 250,
                nextPreviousSlideSpeed: 0,
                enableDrag: true,
                swipeThreshold: 50,
                swipeTimeThreshold: 250,
                slideTimingFunction: "ease-out",
                slideshowDelay: 3000,
                doubleTapSpeed: 250,
                margin: 20,
                imageScaleMethod: "fit",
                resetToolbarPosition: false,
                photoJson: null,
                captionAndToolbarHide: false,
                captionAndToolbarFlipPosition: false,
                captionAndToolbarAutoHideDelay: 5000,
                captionAndToolbarOpacity: 0.9,
                captionAndToolbarShowEmptyCaptions: true,
                getToolbar: a.Toolbar.getToolbar,
                allowUserZoom: true,
                allowRotationOnUserZoom: false,
                maxUserZoom: 5,
                minUserZoom: 1,
                doubleTapZoomLevel: 2.5,
                uiDetailCallBack: null,
                getImageSource: a.Cache.Functions.getImageSource,
                getImageCaption: a.Cache.Functions.getImageCaption,
                getImageMetaData: a.Cache.Functions.getImageMetaData,
                cacheMode: a.Cache.Mode.normal
            };
            i.extend(this.settings, o);
            if (this.settings.target !== f) {
                p = i.DOM.getStyle(this.settings.target, "position");
                if (p !== "relative" || p !== "absolute") {
                    i.DOM.setStyle(this.settings.target, "position", "relative")
                }
            }
            if (this.settings.target !== f) {
                this.isBackEventSupported = false;
                this.settings.backButtonHideEnabled = false
            } else {
                if (this.settings.preventHide) {
                    this.settings.backButtonHideEnabled = false
                }
            }
            this.cache = new k.CacheClass(m, this.settings)
        },
        show: function(p) {
            var o, m;
            this._isResettingPosition = false;
            this.backButtonClicked = false;
            if (i.isNumber(p)) {
                this.currentIndex = p
            } else {
                this.currentIndex = -1;
                for (o = 0, m = this.originalImages.length; o < m; o++) {
                    if (this.originalImages[o] === p) {
                        this.currentIndex = o;
                        break
                    }
                }
            }
            if (this.currentIndex < 0 || this.currentIndex > this.originalImages.length - 1) {
                throw "Code.PhotoSwipe.PhotoSwipeClass.show: Starting index out of range"
            }
            this.isAlreadyGettingPage = this.getWindowDimensions();
            a.setActivateInstance(this);
            this.windowDimensions = this.getWindowDimensions();
            if (this.settings.target === f) {
                i.DOM.addClass(f.document.body, a.CssClasses.buildingBody)
            } else {
                i.DOM.addClass(this.settings.target, a.CssClasses.buildingBody)
            }
            this.createComponents();
            i.Events.fire(this, {
                type: a.EventTypes.onBeforeShow,
                target: this
            });
            this.documentOverlay.fadeIn(this.settings.fadeInSpeed, this.onDocumentOverlayFadeIn.bind(this))
        },
        getWindowDimensions: function() {
            return {
                width: i.DOM.windowWidth(),
                height: i.DOM.windowHeight()
            }
        },
        createComponents: function() {
            this.documentOverlay = new e.DocumentOverlayClass(this.settings);
            this.carousel = new h.CarouselClass(this.cache, this.settings);
            this.uiLayer = new l.UILayerClass(this.settings);
            this.uiDetail = new b.UIDetailClass(this.settings);
            if (!this.settings.captionAndToolbarHide) {
                this.toolbar = new d.ToolbarClass(this.cache, this.settings)
            }
        },
        resetPosition: function() {
            if (this._isResettingPosition) {
                return
            }
            var m = this.getWindowDimensions();
            if (!i.isNothing(this.windowDimensions)) {
                if (m.width === this.windowDimensions.width && m.height === this.windowDimensions.height) {
                    return
                }
            }
            this._isResettingPosition = true;
            this.windowDimensions = m;
            this.destroyZoomPanRotate();
            this.documentOverlay.resetPosition();
            this.carousel.resetPosition();
            if (!i.isNothing(this.toolbar)) {
                this.toolbar.resetPosition()
            }
            this.uiLayer.resetPosition();
            this._isResettingPosition = false;
            i.Events.fire(this, {
                type: a.EventTypes.onResetPosition,
                target: this
            })
        },
        addEventHandler: function(o, m) {
            i.Events.add(this, o, m)
        },
        addEventHandlers: function() {
            if (i.isNothing(this.windowOrientationChangeHandler)) {
                this.windowOrientationChangeHandler = this.onWindowOrientationChange.bind(this);
                this.windowScrollHandler = this.onWindowScroll.bind(this);
                this.keyDownHandler = this.onKeyDown.bind(this);
                this.windowHashChangeHandler = this.onWindowHashChange.bind(this);
                this.uiLayerTouchHandler = this.onUILayerTouch.bind(this);
                this.carouselSlideByEndHandler = this.onCarouselSlideByEnd.bind(this);
                this.carouselSlideshowStartHandler = this.onCarouselSlideshowStart.bind(this);
                this.carouselSlideshowStopHandler = this.onCarouselSlideshowStop.bind(this);
                this.toolbarTapHandler = this.onToolbarTap.bind(this);
                this.toolbarBeforeShowHandler = this.onToolbarBeforeShow.bind(this);
                this.toolbarShowHandler = this.onToolbarShow.bind(this);
                this.toolbarBeforeHideHandler = this.onToolbarBeforeHide.bind(this);
                this.toolbarHideHandler = this.onToolbarHide.bind(this);
                this.mouseWheelHandler = this.onMouseWheel.bind(this);
                this.zoomPanRotateTransformHandler = this.onZoomPanRotateTransform.bind(this)
            }
            if (i.Browser.android) {
                this.orientationEventName = "resize"
            } else {
                if (i.Browser.iOS && (!i.Browser.safari)) {
                    i.Events.add(f.document.body, "orientationchange", this.windowOrientationChangeHandler)
                } else {
                    var m = !i.isNothing(f.onorientationchange);
                    this.orientationEventName = m ? "orientationchange": "resize"
                }
            }
            if (!i.isNothing(this.orientationEventName)) {
                i.Events.add(f, this.orientationEventName, this.windowOrientationChangeHandler)
            }
            if (this.settings.target === f) {
                i.Events.add(f, "scroll", this.windowScrollHandler)
            }
            if (this.settings.enableKeyboard) {
                i.Events.add(f.document, "keydown", this.keyDownHandler)
            }
            if (this.isBackEventSupported && this.settings.backButtonHideEnabled) {
                this.windowHashChangeHandler = this.onWindowHashChange.bind(this);
                if (this.settings.jQueryMobile) {
                    f.location.hash = this.settings.jQueryMobileDialogHash
                } else {
                    this.currentHistoryHashValue = "PhotoSwipe" + new Date().getTime().toString();
                    f.location.hash = this.currentHistoryHashValue
                }
                i.Events.add(f, "hashchange", this.windowHashChangeHandler)
            }
            if (this.settings.enableMouseWheel) {
                i.Events.add(f, "mousewheel", this.mouseWheelHandler)
            }
            i.Events.add(this.uiLayer, i.TouchElement.EventTypes.onTouch, this.uiLayerTouchHandler);
            i.Events.add(this.carousel, h.EventTypes.onSlideByEnd, this.carouselSlideByEndHandler);
            i.Events.add(this.carousel, h.EventTypes.onSlideshowStart, this.carouselSlideshowStartHandler);
            i.Events.add(this.carousel, h.EventTypes.onSlideshowStop, this.carouselSlideshowStopHandler);
            if (!i.isNothing(this.toolbar)) {
                i.Events.add(this.toolbar, d.EventTypes.onTap, this.toolbarTapHandler);
                i.Events.add(this.toolbar, d.EventTypes.onBeforeShow, this.toolbarBeforeShowHandler);
                i.Events.add(this.toolbar, d.EventTypes.onShow, this.toolbarShowHandler);
                i.Events.add(this.toolbar, d.EventTypes.onBeforeHide, this.toolbarBeforeHideHandler);
                i.Events.add(this.toolbar, d.EventTypes.onHide, this.toolbarHideHandler)
            }
        },
        removeEventHandlers: function() {
            if (i.Browser.iOS && (!i.Browser.safari)) {
                i.Events.remove(f.document.body, "orientationchange", this.windowOrientationChangeHandler)
            }
            if (!i.isNothing(this.orientationEventName)) {
                i.Events.remove(f, this.orientationEventName, this.windowOrientationChangeHandler)
            }
            i.Events.remove(f, "scroll", this.windowScrollHandler);
            if (this.settings.enableKeyboard) {
                i.Events.remove(f.document, "keydown", this.keyDownHandler)
            }
            if (this.isBackEventSupported && this.settings.backButtonHideEnabled) {
                i.Events.remove(f, "hashchange", this.windowHashChangeHandler)
            }
            if (this.settings.enableMouseWheel) {
                i.Events.remove(f, "mousewheel", this.mouseWheelHandler)
            }
            if (!i.isNothing(this.uiLayer)) {
                i.Events.remove(this.uiLayer, i.TouchElement.EventTypes.onTouch, this.uiLayerTouchHandler)
            }
            if (!i.isNothing(this.toolbar)) {
                i.Events.remove(this.carousel, h.EventTypes.onSlideByEnd, this.carouselSlideByEndHandler);
                i.Events.remove(this.carousel, h.EventTypes.onSlideshowStart, this.carouselSlideshowStartHandler);
                i.Events.remove(this.carousel, h.EventTypes.onSlideshowStop, this.carouselSlideshowStopHandler)
            }
            if (!i.isNothing(this.toolbar)) {
                i.Events.remove(this.toolbar, d.EventTypes.onTap, this.toolbarTapHandler);
                i.Events.remove(this.toolbar, d.EventTypes.onBeforeShow, this.toolbarBeforeShowHandler);
                i.Events.remove(this.toolbar, d.EventTypes.onShow, this.toolbarShowHandler);
                i.Events.remove(this.toolbar, d.EventTypes.onBeforeHide, this.toolbarBeforeHideHandler);
                i.Events.remove(this.toolbar, d.EventTypes.onHide, this.toolbarHideHandler)
            }
        },
        hide: function() {
            if (this.settings.preventHide) {
                return
            }
            if (i.isNothing(this.documentOverlay)) {
                throw "Code.PhotoSwipe.PhotoSwipeClass.hide: PhotoSwipe instance is already hidden"
            }
            if (!i.isNothing(this.hiding)) {
                return
            }
            this.clearUIWebViewResetPositionTimeout();
            this.destroyZoomPanRotate();
            this.removeEventHandlers();
            i.Events.fire(this, {
                type: a.EventTypes.onBeforeHide,
                target: this
            });
            this.uiLayer.dispose();
            this.uiLayer = null;
            if (!i.isNothing(this.uiDetail)) {
                this.uiDetail.dispose();
                this.uiDetail = null
            }
            if (!i.isNothing(this.toolbar)) {
                this.toolbar.dispose();
                this.toolbar = null
            }
            this.carousel.dispose();
            this.carousel = null;
            i.DOM.removeClass(f.document.body, a.CssClasses.activeBody);
            this.documentOverlay.dispose();
            this.documentOverlay = null;
            this._isResettingPosition = false;
            a.unsetActivateInstance(this);
            i.Events.fire(this, {
                type: a.EventTypes.onHide,
                target: this
            });
            this.goBackInHistory()
        },
        goBackInHistory: function() {
            if (this.isBackEventSupported && this.settings.backButtonHideEnabled) {
                if (!this.backButtonClicked) {
                    f.history.back()
                }
            }
        },
        play: function() {
            if (this.isZoomActive()) {
                return
            }
            if (!this.settings.preventSlideshow) {
                if (!i.isNothing(this.carousel)) {
                    if (!i.isNothing(this.toolbar) && this.toolbar.isVisible) {
                        this.toolbar.fadeOut()
                    }
                    this.carousel.startSlideshow()
                }
            }
        },
        stop: function() {
            if (this.isZoomActive()) {
                return
            }
            if (!i.isNothing(this.carousel)) {
                this.carousel.stopSlideshow()
            }
        },
        previous: function() {
            if (this.isZoomActive()) {
                return
            }
            if (!i.isNothing(this.carousel)) {
                this.carousel.previous()
            }
        },
        next: function() {
            if (this.isZoomActive()) {
                return
            }
            if (!i.isNothing(this.carousel)) {
                this.carousel.next()
            }
        },
        showDetail: function() {
            this.toggleToolbar();
            this.uiDetail.show()
        },
        toggleToolbar: function() {
            if (this.isZoomActive()) {
                return
            }
            if (!i.isNothing(this.toolbar)) {
                this.toolbar.toggleVisibility(this.currentIndex)
            }
        },
        fadeOutToolbarIfVisible: function() {
            if (!i.isNothing(this.toolbar) && this.toolbar.isVisible && this.settings.captionAndToolbarAutoHideDelay > 0) {
                this.toolbar.fadeOut()
            }
        },
        createZoomPanRotate: function() {
            this.stop();
            if (this.canUserZoom() && !this.isZoomActive()) {
                i.Events.fire(this, a.EventTypes.onBeforeZoomPanRotateShow);
                this.zoomPanRotate = new c.ZoomPanRotateClass(this.settings, this.cache.images[this.currentIndex], this.uiLayer);
                this.uiLayer.captureSettings.preventDefaultTouchEvents = true;
                i.Events.add(this.zoomPanRotate, a.ZoomPanRotate.EventTypes.onTransform, this.zoomPanRotateTransformHandler);
                i.Events.fire(this, a.EventTypes.onZoomPanRotateShow);
                if (!i.isNothing(this.toolbar) && this.toolbar.isVisible) {
                    this.toolbar.fadeOut()
                }
            }
        },
        destroyZoomPanRotate: function() {
            if (!i.isNothing(this.zoomPanRotate)) {
                i.Events.fire(this, a.EventTypes.onBeforeZoomPanRotateHide);
                i.Events.remove(this.zoomPanRotate, a.ZoomPanRotate.EventTypes.onTransform, this.zoomPanRotateTransformHandler);
                this.zoomPanRotate.dispose();
                this.zoomPanRotate = null;
                this.uiLayer.captureSettings.preventDefaultTouchEvents = this.settings.preventDefaultTouchEvents;
                i.Events.fire(this, a.EventTypes.onZoomPanRotateHide)
            }
        },
        canUserZoom: function() {
            var o, m;
            if (i.Browser.msie) {
                o = document.createElement("div");
                if (i.isNothing(o.style.msTransform)) {
                    return false
                }
            } else {
                if (!i.Browser.isCSSTransformSupported) {
                    return false
                }
            }
            if (!this.settings.allowUserZoom) {
                return false
            }
            if (this.carousel.isSliding) {
                return false
            }
            m = this.cache.images[this.currentIndex];
            if (i.isNothing(m)) {
                return false
            }
            if (m.isLoading) {
                return false
            }
            return true
        },
        isZoomActive: function() {
            return (!i.isNothing(this.zoomPanRotate))
        },
        getCurrentImage: function() {
            return this.cache.images[this.currentIndex]
        },
        onDocumentOverlayFadeIn: function(m) {
            f.setTimeout(function() {
                var o = (this.settings.target === f) ? f.document.body: this.settings.target;
                i.DOM.removeClass(o, a.CssClasses.buildingBody);
                i.DOM.addClass(o, a.CssClasses.activeBody);
                this.addEventHandlers();
                this.carousel.show(this.currentIndex);
                this.uiLayer.show();
                if (this.settings.autoStartSlideshow) {
                    this.play()
                } else {
                    if (!i.isNothing(this.toolbar)) {
                        this.toolbar.show(this.currentIndex)
                    }
                }
                i.Events.fire(this, {
                    type: a.EventTypes.onShow,
                    target: this
                });
                this.setUIWebViewResetPositionTimeout()
            }.bind(this), 250)
        },
        setUIWebViewResetPositionTimeout: function() {
            if (!this.settings.enableUIWebViewRepositionTimeout) {
                return
            }
            if (! (i.Browser.iOS && (!i.Browser.safari))) {
                return
            }
            if (!i.isNothing(this._uiWebViewResetPositionTimeout)) {
                f.clearTimeout(this._uiWebViewResetPositionTimeout)
            }
            this._uiWebViewResetPositionTimeout = f.setTimeout(function() {
                this.resetPosition();
                this.setUIWebViewResetPositionTimeout()
            }.bind(this), this.settings.uiWebViewResetPositionDelay)
        },
        clearUIWebViewResetPositionTimeout: function() {
            if (!i.isNothing(this._uiWebViewResetPositionTimeout)) {
                f.clearTimeout(this._uiWebViewResetPositionTimeout)
            }
        },
        onWindowScroll: function(m) {
            this.resetPosition()
        },
        onWindowOrientationChange: function(m) {
            this.resetPosition()
        },
        onWindowHashChange: function(m) {
            var o = "#" + ((this.settings.jQueryMobile) ? this.settings.jQueryMobileDialogHash: this.currentHistoryHashValue);
            if (f.location.hash !== o) {
                this.backButtonClicked = true;
                this.hide()
            }
        },
        onKeyDown: function(m) {
            if (m.keyCode === 37) {
                m.preventDefault();
                this.previous()
            } else {
                if (m.keyCode === 39) {
                    m.preventDefault();
                    this.next()
                } else {
                    if (m.keyCode === 38 || m.keyCode === 40) {
                        m.preventDefault()
                    } else {
                        if (m.keyCode === 27) {
                            m.preventDefault();
                            this.hide()
                        } else {
                            if (m.keyCode === 32) {
                                if (!this.settings.hideToolbar) {
                                    this.toggleToolbar()
                                } else {
                                    this.hide()
                                }
                                m.preventDefault()
                            } else {
                                if (m.keyCode === 13) {
                                    m.preventDefault();
                                    this.play()
                                }
                            }
                        }
                    }
                }
            }
        },
        onUILayerTouch: function(t) {
            var r;
            if (this.isZoomActive()) {
                switch (t.action) {
                    case i.TouchElement.ActionTypes.gestureChange:
                        this.zoomPanRotate.zoomRotate(t.scale, (this.settings.allowRotationOnUserZoom) ? t.rotation: 0);
                        break;
                    case i.TouchElement.ActionTypes.gestureEnd:
                        this.zoomPanRotate.setStartingScaleAndRotation(t.scale, (this.settings.allowRotationOnUserZoom) ? t.rotation: 0);
                        break;
                    case i.TouchElement.ActionTypes.touchStart:
                        this.zoomPanRotate.panStart(t.point);
                        break;
                    case i.TouchElement.ActionTypes.touchMove:
                        this.zoomPanRotate.pan(t.point);
                        break;
                    case i.TouchElement.ActionTypes.doubleTap:
                        this.destroyZoomPanRotate();
                        this.toggleToolbar();
                        break;
                    case i.TouchElement.ActionTypes.swipeLeft:
                        break;
                    case i.TouchElement.ActionTypes.swipeRight:
                        break
                }
            } else {
                switch (t.action) {
                    case i.TouchElement.ActionTypes.touchMove:
                    case i.TouchElement.ActionTypes.swipeLeft:
                    case i.TouchElement.ActionTypes.swipeRight:
                        this.fadeOutToolbarIfVisible();
                        this.carousel.onTouch(t.action, t.point);
                        break;
                    case i.TouchElement.ActionTypes.touchStart:
                    case i.TouchElement.ActionTypes.touchMoveEnd:
                        this.carousel.onTouch(t.action, t.point);
                        break;
                    case i.TouchElement.ActionTypes.touchEnd:
                        this.carousel.onTouch(t.action, t.point);
                        break;
                    case i.TouchElement.ActionTypes.tap:
                        this.toggleToolbar();
                        break;
                    case i.TouchElement.ActionTypes.doubleTap:
                        if (this.settings.target === f) {
                            t.point.x -= i.DOM.windowScrollLeft();
                            t.point.y -= i.DOM.windowScrollTop()
                        }
                        var q = this.cache.images[this.currentIndex].imageEl,
                            s = f.parseInt(i.DOM.getStyle(q, "top"), 10),
                            m = f.parseInt(i.DOM.getStyle(q, "left"), 10),
                            o = m + i.DOM.width(q),
                            p = s + i.DOM.height(q);
                        if (t.point.x < m) {
                            t.point.x = m
                        } else {
                            if (t.point.x > o) {
                                t.point.x = o
                            }
                        }
                        if (t.point.y < s) {
                            t.point.y = s
                        } else {
                            if (t.point.y > p) {
                                t.point.y = p
                            }
                        }
                        this.createZoomPanRotate();
                        if (this.isZoomActive()) {
                            this.zoomPanRotate.zoomAndPanToPoint(this.settings.doubleTapZoomLevel, t.point)
                        }
                        break;
                    case i.TouchElement.ActionTypes.gestureStart:
                        this.createZoomPanRotate();
                        break
                }
            }
            i.Events.fire(this, {
                type: a.EventTypes.onTouch,
                target: this,
                point: t.point,
                action: t.action
            })
        },
        onCarouselSlideByEnd: function(m) {
            this.currentIndex = m.cacheIndex;
            if (!i.isNothing(this.toolbar)) {
                this.toolbar.setCaption(this.currentIndex);
                this.toolbar.setToolbarStatus(this.currentIndex)
            }
            i.Events.fire(this, {
                type: a.EventTypes.onDisplayImage,
                target: this,
                action: m.action,
                index: m.cacheIndex
            });
            if (this.settings.resetToolbarPosition) {
                if (this.settings.photoJson) {
                    this.toolbar.insertToolbarText(this.settings.photoJson[this.currentIndex])
                }
                this.toolbar.resetPosition()
            }
        },
        onToolbarTap: function(m) {
            switch (m.action) {
                case d.ToolbarAction.next:
                    this.next();
                    break;
                case d.ToolbarAction.previous:
                    this.previous();
                    break;
                case d.ToolbarAction.close:
                    if ("returnIndex" in g) {
                        g.returnIndex()
                    } else {
                        this.hide()
                    }
                    break;
                case d.ToolbarAction.play:
                    this.play();
                    break;
                case d.ToolbarAction.showDetail:
                    this.showDetail();
                    break
            }
            i.Events.fire(this, {
                type: a.EventTypes.onToolbarTap,
                target: this,
                toolbarAction: m.action,
                tapTarget: m.tapTarget
            })
        },
        onMouseWheel: function(o) {
            var p = i.Events.getWheelDelta(o),
                m = o.timeStamp - (this.mouseWheelStartTime || 0);
            if (m < this.settings.mouseWheelSpeed) {
                return
            }
            this.mouseWheelStartTime = o.timeStamp;
            if (this.settings.invertMouseWheel) {
                p = p * -1
            }
            if (p < 0) {
                this.next()
            } else {
                if (p > 0) {
                    this.previous()
                }
            }
        },
        onCarouselSlideshowStart: function(m) {
            i.Events.fire(this, {
                type: a.EventTypes.onSlideshowStart,
                target: this
            })
        },
        onCarouselSlideshowStop: function(m) {
            i.Events.fire(this, {
                type: a.EventTypes.onSlideshowStop,
                target: this
            })
        },
        onToolbarBeforeShow: function(m) {
            i.Events.fire(this, {
                type: a.EventTypes.onBeforeCaptionAndToolbarShow,
                target: this
            })
        },
        onToolbarShow: function(m) {
            i.Events.fire(this, {
                type: a.EventTypes.onCaptionAndToolbarShow,
                target: this
            })
        },
        onToolbarBeforeHide: function(m) {
            i.Events.fire(this, {
                type: a.EventTypes.onBeforeCaptionAndToolbarHide,
                target: this
            })
        },
        onToolbarHide: function(m) {
            i.Events.fire(this, {
                type: a.EventTypes.onCaptionAndToolbarHide,
                target: this
            })
        },
        onZoomPanRotateTransform: function(m) {
            i.Events.fire(this, {
                target: this,
                type: a.EventTypes.onZoomPanRotateTransform,
                scale: m.scale,
                rotation: m.rotation,
                rotationDegs: m.rotationDegs,
                translateX: m.translateX,
                translateY: m.translateY
            })
        }
    })
} (window, window.klass, window.Code.Util, window.Code.PhotoSwipe.Cache, window.Code.PhotoSwipe.DocumentOverlay, window.Code.PhotoSwipe.Carousel, window.Code.PhotoSwipe.Toolbar, window.Code.PhotoSwipe.UILayer, window.Code.PhotoSwipe.UIDetail, window.Code.PhotoSwipe.ZoomPanRotate));
/*!
 * Lazy Load - jQuery plugin for lazy loading images
 *
 * Copyright (c) 2007-2015 Mika Tuupola
 *
 * Licensed under the MIT license:
 *   http://www.opensource.org/licenses/mit-license.php
 *
 * Project home:
 *   http://www.appelsiini.net/projects/lazyload
 *
 * Version:  1.9.7
 *
 */
(function(c, b, a, e) {
    var d = c(b);
    c.fn.lazyload = function(f) {
        var h = this;
        var k;
        var g = {
            threshold: 0,
            w_threshold: 0,
            failure_limit: 0,
            event: "scroll.lazyload",
            load_all: false,
            otherEvent: "",
            load_in_viewport: false,
            loadingClass: "loading-for-lazyload",
            container: b,
            data_attribute: "original",
            skip_invisible: false,
            appear: null,
            load: null,
            placeholder: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+ip1sAAAAASUVORK5CYII="
        };
        if ( !! h && h.length < 1) {
            return
        }
        function l() {
            var m = 0;
            h.each(function() {
                var o = c(this);
                if (g.skip_invisible && !o.is(":visible")) {
                    return
                }
                if (c.abovethetop(this, g) || c.leftofbegin(this, g)) {} else {
                    if (!c.belowthefold(this, g) && !c.rightoffold(this, g)) {
                        o.trigger("appear");
                        m = 0;
                        if ( !! g.load_all) {
                            h.trigger("appear")
                        }
                    } else {
                        if (++m > g.failure_limit) {
                            return false
                        }
                    }
                }
            })
        }
        if (f) {
            if (e !== f.failurelimit) {
                f.failure_limit = f.failurelimit;
                delete f.failurelimit
            }
            c.extend(g, f)
        }
        if ( !! g.load_all && !!g.load_in_viewport) {
            g.load_in_viewport = false
        }
        k = (g.container === e || g.container === b) ? d: c(g.container);
        var i = null;
        if (0 === g.event.indexOf("scroll.lazyload")) {
            k.on(g.event,
                function() {
                    if (typeof i == "number") {
                        return
                    }
                    i = setTimeout(function() {
                            i = null;
                            return l()
                        },
                        1000)
                })
        }
        c(h).addClass(g.loadingClass);
        this.each(function() {
            var m = this;
            var o = c(m);
            if (!o.attr("data-" + g.data_attribute)) {
                return
            }
            m.loaded = false;
            if (!o.attr("src")) {
                if (o.is("img")) {
                    o.attr("src", g.placeholder)
                }
            }
            o.one("appear",
                function() {
                    if (!this.loaded) {
                        if (g.appear) {
                            var p = h.length;
                            g.appear.call(m, p, g)
                        }
                        c("<img />").one("load",
                            function() {
                                var r = o.attr("data-" + g.data_attribute);
                                if (Mobi.isSupportWebp() && !Fai.top._webDebug && Fai.top._aid > 14000000 && r.indexOf("materialLib") === -1 && r.indexOf("no-pic") === -1) {
                                    r = r.replace(/(jpg|jpeg|png|gif)$|(jpg|jpeg|png|gif)\?v=/,
                                        function(t, v, u) {
                                            if (v) {
                                                return v + ".webp"
                                            } else {
                                                if (u) {
                                                    return t.replace(u, u + ".webp")
                                                }
                                            }
                                        })
                                }
                                o.hide();
                                if (o.is("img")) {
                                    o.attr("src", r)
                                } else {
                                    o.css("background-image", "url('" + r + "')")
                                }
                                o.show().addClass("fade-in-for-lazyload");
                                m.loaded = true;
                                o.removeClass(g.loadingClass);
                                var q = c.grep(h,
                                    function(t) {
                                        return ! t.loaded
                                    });
                                h = c(q);
                                if (g.load) {
                                    var s = h.length;
                                    g.load.call(m, s, g)
                                }
                            }).attr("src", o.attr("data-" + g.data_attribute))
                    }
                }).one("webkitAnimationStart mozAnimationStart MSAnimationStart oanimationstart animationstart",
                function(p) {
                    if (p.animationName == "fadeIn") {
                        p.stopPropagation()
                    }
                }).one("webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend",
                function(p) {
                    if (p.animationName == "fadeIn") {
                        p.stopPropagation()
                    }
                });
            if (g.otherEvent != "") {
                o.on(g.otherEvent,
                    function() {
                        if (!g.load_in_viewport) {
                            if (!m.loaded) {
                                o.trigger("appear")
                            }
                        } else {
                            if (c.abovethetop(m, g) || c.leftofbegin(m, g)) {} else {
                                if (!c.belowthefold(m, g) && !c.rightoffold(m, g)) {
                                    o.trigger("appear")
                                }
                            }
                        }
                    })
            }
        });
        d.on("resize.lazyload",
            function() {
                l()
            });
        if ((/(?:iphone|ipod|ipad).*os 5/gi).test(navigator.appVersion)) {
            d.on("pageshow",
                function(m) {
                    if (m.originalEvent && m.originalEvent.persisted) {
                        h.each(function() {
                            c(this).trigger("appear")
                        })
                    }
                })
        }
        c(a).ready(function() {
            l()
        });
        return this
    };
    c.belowthefold = function(g, h) {
        var f, i;
        if (h.container === e || h.container === b) {
            f = (b.innerHeight ? b.innerHeight: d.height()) + (a.documentElement.scrollTop || b.pageYOffset || a.body.scrollTop)
        } else {
            i = c(h.container).offset();
            f = i.top + i.height
        }
        return f <= c(g).offset().top - h.threshold
    };
    c.rightoffold = function(g, h) {
        var f, i;
        if (h.container === e || h.container === b) {
            f = d.width() + (a.documentElement.scrollLeft || b.pageXOffset || a.body.scrollLeft)
        } else {
            i = c(h.container).offset();
            f = i.left + i.width
        }
        return f <= c(g).offset().left - h.w_threshold
    };
    c.abovethetop = function(g, h) {
        var f, i;
        if (h.container === e || h.container === b) {
            f = a.documentElement.scrollTop || b.pageYOffset || a.body.scrollTop
        } else {
            f = c(h.container).offset().top
        }
        i = c(g).offset();
        return f >= i.top + h.threshold + i.height
    };
    c.leftofbegin = function(g, h) {
        var f, i;
        if (h.container === e || h.container === b) {
            f = (a.documentElement.scrollLeft || b.pageXOffset || a.body.scrollLeft)
        } else {
            f = c(h.container).offset().left
        }
        i = c(g).offset();
        return f >= i.left + h.w_threshold + i.width
    };
    c.inviewport = function(f, g) {
        return ! c.rightoffold(f, g) && !c.leftofbegin(f, g) && !c.belowthefold(f, g) && !c.abovethetop(f, g)
    }
})(jm, window, document);