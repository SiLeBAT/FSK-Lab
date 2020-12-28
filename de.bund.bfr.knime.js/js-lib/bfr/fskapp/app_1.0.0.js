/*
 Copyright (C) Federico Zivolo 2018
 Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */(function(e,t){'object'==typeof exports&&'undefined'!=typeof module?module.exports=t():'function'==typeof define&&define.amd?define(t):e.Popper=t()})(this,function(){'use strict';function e(e){return e&&'[object Function]'==={}.toString.call(e)}function t(e,t){if(1!==e.nodeType)return[];var o=getComputedStyle(e,null);return t?o[t]:o}function o(e){return'HTML'===e.nodeName?e:e.parentNode||e.host}function n(e){if(!e)return document.body;switch(e.nodeName){case'HTML':case'BODY':return e.ownerDocument.body;case'#document':return e.body;}var i=t(e),r=i.overflow,p=i.overflowX,s=i.overflowY;return /(auto|scroll|overlay)/.test(r+s+p)?e:n(o(e))}function r(e){return 11===e?re:10===e?pe:re||pe}function p(e){if(!e)return document.documentElement;for(var o=r(10)?document.body:null,n=e.offsetParent;n===o&&e.nextElementSibling;)n=(e=e.nextElementSibling).offsetParent;var i=n&&n.nodeName;return i&&'BODY'!==i&&'HTML'!==i?-1!==['TD','TABLE'].indexOf(n.nodeName)&&'static'===t(n,'position')?p(n):n:e?e.ownerDocument.documentElement:document.documentElement}function s(e){var t=e.nodeName;return'BODY'!==t&&('HTML'===t||p(e.firstElementChild)===e)}function d(e){return null===e.parentNode?e:d(e.parentNode)}function a(e,t){if(!e||!e.nodeType||!t||!t.nodeType)return document.documentElement;var o=e.compareDocumentPosition(t)&Node.DOCUMENT_POSITION_FOLLOWING,n=o?e:t,i=o?t:e,r=document.createRange();r.setStart(n,0),r.setEnd(i,0);var l=r.commonAncestorContainer;if(e!==l&&t!==l||n.contains(i))return s(l)?l:p(l);var f=d(e);return f.host?a(f.host,t):a(e,d(t).host)}function l(e){var t=1<arguments.length&&void 0!==arguments[1]?arguments[1]:'top',o='top'===t?'scrollTop':'scrollLeft',n=e.nodeName;if('BODY'===n||'HTML'===n){var i=e.ownerDocument.documentElement,r=e.ownerDocument.scrollingElement||i;return r[o]}return e[o]}function f(e,t){var o=2<arguments.length&&void 0!==arguments[2]&&arguments[2],n=l(t,'top'),i=l(t,'left'),r=o?-1:1;return e.top+=n*r,e.bottom+=n*r,e.left+=i*r,e.right+=i*r,e}function m(e,t){var o='x'===t?'Left':'Top',n='Left'==o?'Right':'Bottom';return parseFloat(e['border'+o+'Width'],10)+parseFloat(e['border'+n+'Width'],10)}function h(e,t,o,n){return $(t['offset'+e],t['scroll'+e],o['client'+e],o['offset'+e],o['scroll'+e],r(10)?o['offset'+e]+n['margin'+('Height'===e?'Top':'Left')]+n['margin'+('Height'===e?'Bottom':'Right')]:0)}function c(){var e=document.body,t=document.documentElement,o=r(10)&&getComputedStyle(t);return{height:h('Height',e,t,o),width:h('Width',e,t,o)}}function g(e){return le({},e,{right:e.left+e.width,bottom:e.top+e.height})}function u(e){var o={};try{if(r(10)){o=e.getBoundingClientRect();var n=l(e,'top'),i=l(e,'left');o.top+=n,o.left+=i,o.bottom+=n,o.right+=i}else o=e.getBoundingClientRect()}catch(t){}var p={left:o.left,top:o.top,width:o.right-o.left,height:o.bottom-o.top},s='HTML'===e.nodeName?c():{},d=s.width||e.clientWidth||p.right-p.left,a=s.height||e.clientHeight||p.bottom-p.top,f=e.offsetWidth-d,h=e.offsetHeight-a;if(f||h){var u=t(e);f-=m(u,'x'),h-=m(u,'y'),p.width-=f,p.height-=h}return g(p)}function b(e,o){var i=2<arguments.length&&void 0!==arguments[2]&&arguments[2],p=r(10),s='HTML'===o.nodeName,d=u(e),a=u(o),l=n(e),m=t(o),h=parseFloat(m.borderTopWidth,10),c=parseFloat(m.borderLeftWidth,10);i&&'HTML'===o.nodeName&&(a.top=$(a.top,0),a.left=$(a.left,0));var b=g({top:d.top-a.top-h,left:d.left-a.left-c,width:d.width,height:d.height});if(b.marginTop=0,b.marginLeft=0,!p&&s){var y=parseFloat(m.marginTop,10),w=parseFloat(m.marginLeft,10);b.top-=h-y,b.bottom-=h-y,b.left-=c-w,b.right-=c-w,b.marginTop=y,b.marginLeft=w}return(p&&!i?o.contains(l):o===l&&'BODY'!==l.nodeName)&&(b=f(b,o)),b}function y(e){var t=1<arguments.length&&void 0!==arguments[1]&&arguments[1],o=e.ownerDocument.documentElement,n=b(e,o),i=$(o.clientWidth,window.innerWidth||0),r=$(o.clientHeight,window.innerHeight||0),p=t?0:l(o),s=t?0:l(o,'left'),d={top:p-n.top+n.marginTop,left:s-n.left+n.marginLeft,width:i,height:r};return g(d)}function w(e){var n=e.nodeName;return'BODY'===n||'HTML'===n?!1:'fixed'===t(e,'position')||w(o(e))}function E(e){if(!e||!e.parentElement||r())return document.documentElement;for(var o=e.parentElement;o&&'none'===t(o,'transform');)o=o.parentElement;return o||document.documentElement}function v(e,t,i,r){var p=4<arguments.length&&void 0!==arguments[4]&&arguments[4],s={top:0,left:0},d=p?E(e):a(e,t);if('viewport'===r)s=y(d,p);else{var l;'scrollParent'===r?(l=n(o(t)),'BODY'===l.nodeName&&(l=e.ownerDocument.documentElement)):'window'===r?l=e.ownerDocument.documentElement:l=r;var f=b(l,d,p);if('HTML'===l.nodeName&&!w(d)){var m=c(),h=m.height,g=m.width;s.top+=f.top-f.marginTop,s.bottom=h+f.top,s.left+=f.left-f.marginLeft,s.right=g+f.left}else s=f}return s.left+=i,s.top+=i,s.right-=i,s.bottom-=i,s}function x(e){var t=e.width,o=e.height;return t*o}function O(e,t,o,n,i){var r=5<arguments.length&&void 0!==arguments[5]?arguments[5]:0;if(-1===e.indexOf('auto'))return e;var p=v(o,n,r,i),s={top:{width:p.width,height:t.top-p.top},right:{width:p.right-t.right,height:p.height},bottom:{width:p.width,height:p.bottom-t.bottom},left:{width:t.left-p.left,height:p.height}},d=Object.keys(s).map(function(e){return le({key:e},s[e],{area:x(s[e])})}).sort(function(e,t){return t.area-e.area}),a=d.filter(function(e){var t=e.width,n=e.height;return t>=o.clientWidth&&n>=o.clientHeight}),l=0<a.length?a[0].key:d[0].key,f=e.split('-')[1];return l+(f?'-'+f:'')}function L(e,t,o){var n=3<arguments.length&&void 0!==arguments[3]?arguments[3]:null,i=n?E(t):a(t,o);return b(o,i,n)}function S(e){var t=getComputedStyle(e),o=parseFloat(t.marginTop)+parseFloat(t.marginBottom),n=parseFloat(t.marginLeft)+parseFloat(t.marginRight),i={width:e.offsetWidth+n,height:e.offsetHeight+o};return i}function T(e){var t={left:'right',right:'left',bottom:'top',top:'bottom'};return e.replace(/left|right|bottom|top/g,function(e){return t[e]})}function C(e,t,o){o=o.split('-')[0];var n=S(e),i={width:n.width,height:n.height},r=-1!==['right','left'].indexOf(o),p=r?'top':'left',s=r?'left':'top',d=r?'height':'width',a=r?'width':'height';return i[p]=t[p]+t[d]/2-n[d]/2,i[s]=o===s?t[s]-n[a]:t[T(s)],i}function D(e,t){return Array.prototype.find?e.find(t):e.filter(t)[0]}function N(e,t,o){if(Array.prototype.findIndex)return e.findIndex(function(e){return e[t]===o});var n=D(e,function(e){return e[t]===o});return e.indexOf(n)}function P(t,o,n){var i=void 0===n?t:t.slice(0,N(t,'name',n));return i.forEach(function(t){t['function']&&console.warn('`modifier.function` is deprecated, use `modifier.fn`!');var n=t['function']||t.fn;t.enabled&&e(n)&&(o.offsets.popper=g(o.offsets.popper),o.offsets.reference=g(o.offsets.reference),o=n(o,t))}),o}function k(){if(!this.state.isDestroyed){var e={instance:this,styles:{},arrowStyles:{},attributes:{},flipped:!1,offsets:{}};e.offsets.reference=L(this.state,this.popper,this.reference,this.options.positionFixed),e.placement=O(this.options.placement,e.offsets.reference,this.popper,this.reference,this.options.modifiers.flip.boundariesElement,this.options.modifiers.flip.padding),e.originalPlacement=e.placement,e.positionFixed=this.options.positionFixed,e.offsets.popper=C(this.popper,e.offsets.reference,e.placement),e.offsets.popper.position=this.options.positionFixed?'fixed':'absolute',e=P(this.modifiers,e),this.state.isCreated?this.options.onUpdate(e):(this.state.isCreated=!0,this.options.onCreate(e))}}function W(e,t){return e.some(function(e){var o=e.name,n=e.enabled;return n&&o===t})}function B(e){for(var t=[!1,'ms','Webkit','Moz','O'],o=e.charAt(0).toUpperCase()+e.slice(1),n=0;n<t.length;n++){var i=t[n],r=i?''+i+o:e;if('undefined'!=typeof document.body.style[r])return r}return null}function H(){return this.state.isDestroyed=!0,W(this.modifiers,'applyStyle')&&(this.popper.removeAttribute('x-placement'),this.popper.style.position='',this.popper.style.top='',this.popper.style.left='',this.popper.style.right='',this.popper.style.bottom='',this.popper.style.willChange='',this.popper.style[B('transform')]=''),this.disableEventListeners(),this.options.removeOnDestroy&&this.popper.parentNode.removeChild(this.popper),this}function A(e){var t=e.ownerDocument;return t?t.defaultView:window}function M(e,t,o,i){var r='BODY'===e.nodeName,p=r?e.ownerDocument.defaultView:e;p.addEventListener(t,o,{passive:!0}),r||M(n(p.parentNode),t,o,i),i.push(p)}function I(e,t,o,i){o.updateBound=i,A(e).addEventListener('resize',o.updateBound,{passive:!0});var r=n(e);return M(r,'scroll',o.updateBound,o.scrollParents),o.scrollElement=r,o.eventsEnabled=!0,o}function F(){this.state.eventsEnabled||(this.state=I(this.reference,this.options,this.state,this.scheduleUpdate))}function R(e,t){return A(e).removeEventListener('resize',t.updateBound),t.scrollParents.forEach(function(e){e.removeEventListener('scroll',t.updateBound)}),t.updateBound=null,t.scrollParents=[],t.scrollElement=null,t.eventsEnabled=!1,t}function U(){this.state.eventsEnabled&&(cancelAnimationFrame(this.scheduleUpdate),this.state=R(this.reference,this.state))}function Y(e){return''!==e&&!isNaN(parseFloat(e))&&isFinite(e)}function j(e,t){Object.keys(t).forEach(function(o){var n='';-1!==['width','height','top','right','bottom','left'].indexOf(o)&&Y(t[o])&&(n='px'),e.style[o]=t[o]+n})}function K(e,t){Object.keys(t).forEach(function(o){var n=t[o];!1===n?e.removeAttribute(o):e.setAttribute(o,t[o])})}function q(e,t,o){var n=D(e,function(e){var o=e.name;return o===t}),i=!!n&&e.some(function(e){return e.name===o&&e.enabled&&e.order<n.order});if(!i){var r='`'+t+'`';console.warn('`'+o+'`'+' modifier is required by '+r+' modifier in order to work, be sure to include it before '+r+'!')}return i}function G(e){return'end'===e?'start':'start'===e?'end':e}function z(e){var t=1<arguments.length&&void 0!==arguments[1]&&arguments[1],o=me.indexOf(e),n=me.slice(o+1).concat(me.slice(0,o));return t?n.reverse():n}function V(e,t,o,n){var i=e.match(/((?:\-|\+)?\d*\.?\d*)(.*)/),r=+i[1],p=i[2];if(!r)return e;if(0===p.indexOf('%')){var s;switch(p){case'%p':s=o;break;case'%':case'%r':default:s=n;}var d=g(s);return d[t]/100*r}if('vh'===p||'vw'===p){var a;return a='vh'===p?$(document.documentElement.clientHeight,window.innerHeight||0):$(document.documentElement.clientWidth,window.innerWidth||0),a/100*r}return r}function _(e,t,o,n){var i=[0,0],r=-1!==['right','left'].indexOf(n),p=e.split(/(\+|\-)/).map(function(e){return e.trim()}),s=p.indexOf(D(p,function(e){return-1!==e.search(/,|\s/)}));p[s]&&-1===p[s].indexOf(',')&&console.warn('Offsets separated by white space(s) are deprecated, use a comma (,) instead.');var d=/\s*,\s*|\s+/,a=-1===s?[p]:[p.slice(0,s).concat([p[s].split(d)[0]]),[p[s].split(d)[1]].concat(p.slice(s+1))];return a=a.map(function(e,n){var i=(1===n?!r:r)?'height':'width',p=!1;return e.reduce(function(e,t){return''===e[e.length-1]&&-1!==['+','-'].indexOf(t)?(e[e.length-1]=t,p=!0,e):p?(e[e.length-1]+=t,p=!1,e):e.concat(t)},[]).map(function(e){return V(e,i,t,o)})}),a.forEach(function(e,t){e.forEach(function(o,n){Y(o)&&(i[t]+=o*('-'===e[n-1]?-1:1))})}),i}function X(e,t){var o,n=t.offset,i=e.placement,r=e.offsets,p=r.popper,s=r.reference,d=i.split('-')[0];return o=Y(+n)?[+n,0]:_(n,p,s,d),'left'===d?(p.top+=o[0],p.left-=o[1]):'right'===d?(p.top+=o[0],p.left+=o[1]):'top'===d?(p.left+=o[0],p.top-=o[1]):'bottom'===d&&(p.left+=o[0],p.top+=o[1]),e.popper=p,e}for(var J=Math.min,Q=Math.round,Z=Math.floor,$=Math.max,ee='undefined'!=typeof window&&'undefined'!=typeof document,te=['Edge','Trident','Firefox'],oe=0,ne=0;ne<te.length;ne+=1)if(ee&&0<=navigator.userAgent.indexOf(te[ne])){oe=1;break}var i=ee&&window.Promise,ie=i?function(e){var t=!1;return function(){t||(t=!0,window.Promise.resolve().then(function(){t=!1,e()}))}}:function(e){var t=!1;return function(){t||(t=!0,setTimeout(function(){t=!1,e()},oe))}},re=ee&&!!(window.MSInputMethodContext&&document.documentMode),pe=ee&&/MSIE 10/.test(navigator.userAgent),se=function(e,t){if(!(e instanceof t))throw new TypeError('Cannot call a class as a function')},de=function(){function e(e,t){for(var o,n=0;n<t.length;n++)o=t[n],o.enumerable=o.enumerable||!1,o.configurable=!0,'value'in o&&(o.writable=!0),Object.defineProperty(e,o.key,o)}return function(t,o,n){return o&&e(t.prototype,o),n&&e(t,n),t}}(),ae=function(e,t,o){return t in e?Object.defineProperty(e,t,{value:o,enumerable:!0,configurable:!0,writable:!0}):e[t]=o,e},le=Object.assign||function(e){for(var t,o=1;o<arguments.length;o++)for(var n in t=arguments[o],t)Object.prototype.hasOwnProperty.call(t,n)&&(e[n]=t[n]);return e},fe=['auto-start','auto','auto-end','top-start','top','top-end','right-start','right','right-end','bottom-end','bottom','bottom-start','left-end','left','left-start'],me=fe.slice(3),he={FLIP:'flip',CLOCKWISE:'clockwise',COUNTERCLOCKWISE:'counterclockwise'},ce=function(){function t(o,n){var i=this,r=2<arguments.length&&void 0!==arguments[2]?arguments[2]:{};se(this,t),this.scheduleUpdate=function(){return requestAnimationFrame(i.update)},this.update=ie(this.update.bind(this)),this.options=le({},t.Defaults,r),this.state={isDestroyed:!1,isCreated:!1,scrollParents:[]},this.reference=o&&o.jquery?o[0]:o,this.popper=n&&n.jquery?n[0]:n,this.options.modifiers={},Object.keys(le({},t.Defaults.modifiers,r.modifiers)).forEach(function(e){i.options.modifiers[e]=le({},t.Defaults.modifiers[e]||{},r.modifiers?r.modifiers[e]:{})}),this.modifiers=Object.keys(this.options.modifiers).map(function(e){return le({name:e},i.options.modifiers[e])}).sort(function(e,t){return e.order-t.order}),this.modifiers.forEach(function(t){t.enabled&&e(t.onLoad)&&t.onLoad(i.reference,i.popper,i.options,t,i.state)}),this.update();var p=this.options.eventsEnabled;p&&this.enableEventListeners(),this.state.eventsEnabled=p}return de(t,[{key:'update',value:function(){return k.call(this)}},{key:'destroy',value:function(){return H.call(this)}},{key:'enableEventListeners',value:function(){return F.call(this)}},{key:'disableEventListeners',value:function(){return U.call(this)}}]),t}();return ce.Utils=('undefined'==typeof window?global:window).PopperUtils,ce.placements=fe,ce.Defaults={placement:'bottom',positionFixed:!1,eventsEnabled:!0,removeOnDestroy:!1,onCreate:function(){},onUpdate:function(){},modifiers:{shift:{order:100,enabled:!0,fn:function(e){var t=e.placement,o=t.split('-')[0],n=t.split('-')[1];if(n){var i=e.offsets,r=i.reference,p=i.popper,s=-1!==['bottom','top'].indexOf(o),d=s?'left':'top',a=s?'width':'height',l={start:ae({},d,r[d]),end:ae({},d,r[d]+r[a]-p[a])};e.offsets.popper=le({},p,l[n])}return e}},offset:{order:200,enabled:!0,fn:X,offset:0},preventOverflow:{order:300,enabled:!0,fn:function(e,t){var o=t.boundariesElement||p(e.instance.popper);e.instance.reference===o&&(o=p(o));var n=B('transform'),i=e.instance.popper.style,r=i.top,s=i.left,d=i[n];i.top='',i.left='',i[n]='';var a=v(e.instance.popper,e.instance.reference,t.padding,o,e.positionFixed);i.top=r,i.left=s,i[n]=d,t.boundaries=a;var l=t.priority,f=e.offsets.popper,m={primary:function(e){var o=f[e];return f[e]<a[e]&&!t.escapeWithReference&&(o=$(f[e],a[e])),ae({},e,o)},secondary:function(e){var o='right'===e?'left':'top',n=f[o];return f[e]>a[e]&&!t.escapeWithReference&&(n=J(f[o],a[e]-('right'===e?f.width:f.height))),ae({},o,n)}};return l.forEach(function(e){var t=-1===['left','top'].indexOf(e)?'secondary':'primary';f=le({},f,m[t](e))}),e.offsets.popper=f,e},priority:['left','right','top','bottom'],padding:5,boundariesElement:'scrollParent'},keepTogether:{order:400,enabled:!0,fn:function(e){var t=e.offsets,o=t.popper,n=t.reference,i=e.placement.split('-')[0],r=Z,p=-1!==['top','bottom'].indexOf(i),s=p?'right':'bottom',d=p?'left':'top',a=p?'width':'height';return o[s]<r(n[d])&&(e.offsets.popper[d]=r(n[d])-o[a]),o[d]>r(n[s])&&(e.offsets.popper[d]=r(n[s])),e}},arrow:{order:500,enabled:!0,fn:function(e,o){var n;if(!q(e.instance.modifiers,'arrow','keepTogether'))return e;var i=o.element;if('string'==typeof i){if(i=e.instance.popper.querySelector(i),!i)return e;}else if(!e.instance.popper.contains(i))return console.warn('WARNING: `arrow.element` must be child of its popper element!'),e;var r=e.placement.split('-')[0],p=e.offsets,s=p.popper,d=p.reference,a=-1!==['left','right'].indexOf(r),l=a?'height':'width',f=a?'Top':'Left',m=f.toLowerCase(),h=a?'left':'top',c=a?'bottom':'right',u=S(i)[l];d[c]-u<s[m]&&(e.offsets.popper[m]-=s[m]-(d[c]-u)),d[m]+u>s[c]&&(e.offsets.popper[m]+=d[m]+u-s[c]),e.offsets.popper=g(e.offsets.popper);var b=d[m]+d[l]/2-u/2,y=t(e.instance.popper),w=parseFloat(y['margin'+f],10),E=parseFloat(y['border'+f+'Width'],10),v=b-e.offsets.popper[m]-w-E;return v=$(J(s[l]-u,v),0),e.arrowElement=i,e.offsets.arrow=(n={},ae(n,m,Q(v)),ae(n,h,''),n),e},element:'[x-arrow]'},flip:{order:600,enabled:!0,fn:function(e,t){if(W(e.instance.modifiers,'inner'))return e;if(e.flipped&&e.placement===e.originalPlacement)return e;var o=v(e.instance.popper,e.instance.reference,t.padding,t.boundariesElement,e.positionFixed),n=e.placement.split('-')[0],i=T(n),r=e.placement.split('-')[1]||'',p=[];switch(t.behavior){case he.FLIP:p=[n,i];break;case he.CLOCKWISE:p=z(n);break;case he.COUNTERCLOCKWISE:p=z(n,!0);break;default:p=t.behavior;}return p.forEach(function(s,d){if(n!==s||p.length===d+1)return e;n=e.placement.split('-')[0],i=T(n);var a=e.offsets.popper,l=e.offsets.reference,f=Z,m='left'===n&&f(a.right)>f(l.left)||'right'===n&&f(a.left)<f(l.right)||'top'===n&&f(a.bottom)>f(l.top)||'bottom'===n&&f(a.top)<f(l.bottom),h=f(a.left)<f(o.left),c=f(a.right)>f(o.right),g=f(a.top)<f(o.top),u=f(a.bottom)>f(o.bottom),b='left'===n&&h||'right'===n&&c||'top'===n&&g||'bottom'===n&&u,y=-1!==['top','bottom'].indexOf(n),w=!!t.flipVariations&&(y&&'start'===r&&h||y&&'end'===r&&c||!y&&'start'===r&&g||!y&&'end'===r&&u);(m||b||w)&&(e.flipped=!0,(m||b)&&(n=p[d+1]),w&&(r=G(r)),e.placement=n+(r?'-'+r:''),e.offsets.popper=le({},e.offsets.popper,C(e.instance.popper,e.offsets.reference,e.placement)),e=P(e.instance.modifiers,e,'flip'))}),e},behavior:'flip',padding:5,boundariesElement:'viewport'},inner:{order:700,enabled:!1,fn:function(e){var t=e.placement,o=t.split('-')[0],n=e.offsets,i=n.popper,r=n.reference,p=-1!==['left','right'].indexOf(o),s=-1===['top','left'].indexOf(o);return i[p?'left':'top']=r[o]-(s?i[p?'width':'height']:0),e.placement=T(t),e.offsets.popper=g(i),e}},hide:{order:800,enabled:!0,fn:function(e){if(!q(e.instance.modifiers,'hide','preventOverflow'))return e;var t=e.offsets.reference,o=D(e.instance.modifiers,function(e){return'preventOverflow'===e.name}).boundaries;if(t.bottom<o.top||t.left>o.right||t.top>o.bottom||t.right<o.left){if(!0===e.hide)return e;e.hide=!0,e.attributes['x-out-of-boundaries']=''}else{if(!1===e.hide)return e;e.hide=!1,e.attributes['x-out-of-boundaries']=!1}return e}},computeStyle:{order:850,enabled:!0,fn:function(e,t){var o=t.x,n=t.y,i=e.offsets.popper,r=D(e.instance.modifiers,function(e){return'applyStyle'===e.name}).gpuAcceleration;void 0!==r&&console.warn('WARNING: `gpuAcceleration` option moved to `computeStyle` modifier and will not be supported in future versions of Popper.js!');var s,d,a=void 0===r?t.gpuAcceleration:r,l=p(e.instance.popper),f=u(l),m={position:i.position},h={left:Z(i.left),top:Q(i.top),bottom:Q(i.bottom),right:Z(i.right)},c='bottom'===o?'top':'bottom',g='right'===n?'left':'right',b=B('transform');if(d='bottom'==c?-f.height+h.bottom:h.top,s='right'==g?-f.width+h.right:h.left,a&&b)m[b]='translate3d('+s+'px, '+d+'px, 0)',m[c]=0,m[g]=0,m.willChange='transform';else{var y='bottom'==c?-1:1,w='right'==g?-1:1;m[c]=d*y,m[g]=s*w,m.willChange=c+', '+g}var E={"x-placement":e.placement};return e.attributes=le({},E,e.attributes),e.styles=le({},m,e.styles),e.arrowStyles=le({},e.offsets.arrow,e.arrowStyles),e},gpuAcceleration:!0,x:'bottom',y:'right'},applyStyle:{order:900,enabled:!0,fn:function(e){return j(e.instance.popper,e.styles),K(e.instance.popper,e.attributes),e.arrowElement&&Object.keys(e.arrowStyles).length&&j(e.arrowElement,e.arrowStyles),e},onLoad:function(e,t,o,n,i){var r=L(i,t,e,o.positionFixed),p=O(o.placement,r,t,e,o.modifiers.flip.boundariesElement,o.modifiers.flip.padding);return t.setAttribute('x-placement',p),j(t,{position:o.positionFixed?'fixed':'absolute'}),o},gpuAcceleration:void 0}}},ce});


/*!
  * Bootstrap v4.4.1 (https://getbootstrap.com/)
  * Copyright 2011-2019 The Bootstrap Authors (https://github.com/twbs/bootstrap/graphs/contributors)
  * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
  */
!function(e,t){"object"==typeof exports&&"undefined"!=typeof module?t(exports,require("jquery")):"function"==typeof define&&define.amd?define(["exports","jquery"],t):t((e=e||self).bootstrap={},e.jQuery)}(this,function(e,p){"use strict";function i(e,t){for(var n=0;n<t.length;n++){var i=t[n];i.enumerable=i.enumerable||!1,i.configurable=!0,"value"in i&&(i.writable=!0),Object.defineProperty(e,i.key,i)}}function s(e,t,n){return t&&i(e.prototype,t),n&&i(e,n),e}function t(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(t);e&&(i=i.filter(function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable})),n.push.apply(n,i)}return n}function l(o){for(var e=1;e<arguments.length;e++){var r=null!=arguments[e]?arguments[e]:{};e%2?t(Object(r),!0).forEach(function(e){var t,n,i;t=o,i=r[n=e],n in t?Object.defineProperty(t,n,{value:i,enumerable:!0,configurable:!0,writable:!0}):t[n]=i}):Object.getOwnPropertyDescriptors?Object.defineProperties(o,Object.getOwnPropertyDescriptors(r)):t(Object(r)).forEach(function(e){Object.defineProperty(o,e,Object.getOwnPropertyDescriptor(r,e))})}return o}p=p&&p.hasOwnProperty("default")?p.default:p;var n="transitionend";function o(e){var t=this,n=!1;return p(this).one(m.TRANSITION_END,function(){n=!0}),setTimeout(function(){n||m.triggerTransitionEnd(t)},e),this}var m={TRANSITION_END:"bsTransitionEnd",getUID:function(e){for(;e+=~~(1e6*Math.random()),document.getElementById(e););return e},getSelectorFromElement:function(e){var t=e.getAttribute("data-target");if(!t||"#"===t){var n=e.getAttribute("href");t=n&&"#"!==n?n.trim():""}try{return document.querySelector(t)?t:null}catch(e){return null}},getTransitionDurationFromElement:function(e){if(!e)return 0;var t=p(e).css("transition-duration"),n=p(e).css("transition-delay"),i=parseFloat(t),o=parseFloat(n);return i||o?(t=t.split(",")[0],n=n.split(",")[0],1e3*(parseFloat(t)+parseFloat(n))):0},reflow:function(e){return e.offsetHeight},triggerTransitionEnd:function(e){p(e).trigger(n)},supportsTransitionEnd:function(){return Boolean(n)},isElement:function(e){return(e[0]||e).nodeType},typeCheckConfig:function(e,t,n){for(var i in n)if(Object.prototype.hasOwnProperty.call(n,i)){var o=n[i],r=t[i],s=r&&m.isElement(r)?"element":(a=r,{}.toString.call(a).match(/\s([a-z]+)/i)[1].toLowerCase());if(!new RegExp(o).test(s))throw new Error(e.toUpperCase()+': Option "'+i+'" provided type "'+s+'" but expected type "'+o+'".')}var a},findShadowRoot:function(e){if(!document.documentElement.attachShadow)return null;if("function"!=typeof e.getRootNode)return e instanceof ShadowRoot?e:e.parentNode?m.findShadowRoot(e.parentNode):null;var t=e.getRootNode();return t instanceof ShadowRoot?t:null},jQueryDetection:function(){if("undefined"==typeof p)throw new TypeError("Bootstrap's JavaScript requires jQuery. jQuery must be included before Bootstrap's JavaScript.");var e=p.fn.jquery.split(" ")[0].split(".");if(e[0]<2&&e[1]<9||1===e[0]&&9===e[1]&&e[2]<1||4<=e[0])throw new Error("Bootstrap's JavaScript requires at least jQuery v1.9.1 but less than v4.0.0")}};m.jQueryDetection(),p.fn.emulateTransitionEnd=o,p.event.special[m.TRANSITION_END]={bindType:n,delegateType:n,handle:function(e){if(p(e.target).is(this))return e.handleObj.handler.apply(this,arguments)}};var r="alert",a="bs.alert",c="."+a,h=p.fn[r],u={CLOSE:"close"+c,CLOSED:"closed"+c,CLICK_DATA_API:"click"+c+".data-api"},f="alert",d="fade",g="show",_=function(){function i(e){this._element=e}var e=i.prototype;return e.close=function(e){var t=this._element;e&&(t=this._getRootElement(e)),this._triggerCloseEvent(t).isDefaultPrevented()||this._removeElement(t)},e.dispose=function(){p.removeData(this._element,a),this._element=null},e._getRootElement=function(e){var t=m.getSelectorFromElement(e),n=!1;return t&&(n=document.querySelector(t)),n=n||p(e).closest("."+f)[0]},e._triggerCloseEvent=function(e){var t=p.Event(u.CLOSE);return p(e).trigger(t),t},e._removeElement=function(t){var n=this;if(p(t).removeClass(g),p(t).hasClass(d)){var e=m.getTransitionDurationFromElement(t);p(t).one(m.TRANSITION_END,function(e){return n._destroyElement(t,e)}).emulateTransitionEnd(e)}else this._destroyElement(t)},e._destroyElement=function(e){p(e).detach().trigger(u.CLOSED).remove()},i._jQueryInterface=function(n){return this.each(function(){var e=p(this),t=e.data(a);t||(t=new i(this),e.data(a,t)),"close"===n&&t[n](this)})},i._handleDismiss=function(t){return function(e){e&&e.preventDefault(),t.close(this)}},s(i,null,[{key:"VERSION",get:function(){return"4.4.1"}}]),i}();p(document).on(u.CLICK_DATA_API,'[data-dismiss="alert"]',_._handleDismiss(new _)),p.fn[r]=_._jQueryInterface,p.fn[r].Constructor=_,p.fn[r].noConflict=function(){return p.fn[r]=h,_._jQueryInterface};var v="button",y="bs.button",E="."+y,b=".data-api",w=p.fn[v],T="active",C="btn",S="focus",D='[data-toggle^="button"]',I='[data-toggle="buttons"]',A='[data-toggle="button"]',O='[data-toggle="buttons"] .btn',N='input:not([type="hidden"])',k=".active",L=".btn",P={CLICK_DATA_API:"click"+E+b,FOCUS_BLUR_DATA_API:"focus"+E+b+" blur"+E+b,LOAD_DATA_API:"load"+E+b},x=function(){function n(e){this._element=e}var e=n.prototype;return e.toggle=function(){var e=!0,t=!0,n=p(this._element).closest(I)[0];if(n){var i=this._element.querySelector(N);if(i){if("radio"===i.type)if(i.checked&&this._element.classList.contains(T))e=!1;else{var o=n.querySelector(k);o&&p(o).removeClass(T)}else"checkbox"===i.type?"LABEL"===this._element.tagName&&i.checked===this._element.classList.contains(T)&&(e=!1):e=!1;e&&(i.checked=!this._element.classList.contains(T),p(i).trigger("change")),i.focus(),t=!1}}this._element.hasAttribute("disabled")||this._element.classList.contains("disabled")||(t&&this._element.setAttribute("aria-pressed",!this._element.classList.contains(T)),e&&p(this._element).toggleClass(T))},e.dispose=function(){p.removeData(this._element,y),this._element=null},n._jQueryInterface=function(t){return this.each(function(){var e=p(this).data(y);e||(e=new n(this),p(this).data(y,e)),"toggle"===t&&e[t]()})},s(n,null,[{key:"VERSION",get:function(){return"4.4.1"}}]),n}();p(document).on(P.CLICK_DATA_API,D,function(e){var t=e.target;if(p(t).hasClass(C)||(t=p(t).closest(L)[0]),!t||t.hasAttribute("disabled")||t.classList.contains("disabled"))e.preventDefault();else{var n=t.querySelector(N);if(n&&(n.hasAttribute("disabled")||n.classList.contains("disabled")))return void e.preventDefault();x._jQueryInterface.call(p(t),"toggle")}}).on(P.FOCUS_BLUR_DATA_API,D,function(e){var t=p(e.target).closest(L)[0];p(t).toggleClass(S,/^focus(in)?$/.test(e.type))}),p(window).on(P.LOAD_DATA_API,function(){for(var e=[].slice.call(document.querySelectorAll(O)),t=0,n=e.length;t<n;t++){var i=e[t],o=i.querySelector(N);o.checked||o.hasAttribute("checked")?i.classList.add(T):i.classList.remove(T)}for(var r=0,s=(e=[].slice.call(document.querySelectorAll(A))).length;r<s;r++){var a=e[r];"true"===a.getAttribute("aria-pressed")?a.classList.add(T):a.classList.remove(T)}}),p.fn[v]=x._jQueryInterface,p.fn[v].Constructor=x,p.fn[v].noConflict=function(){return p.fn[v]=w,x._jQueryInterface};var j="carousel",H="bs.carousel",R="."+H,F=".data-api",M=p.fn[j],W={interval:5e3,keyboard:!0,slide:!1,pause:"hover",wrap:!0,touch:!0},U={interval:"(number|boolean)",keyboard:"boolean",slide:"(boolean|string)",pause:"(string|boolean)",wrap:"boolean",touch:"boolean"},B="next",q="prev",K="left",Q="right",V={SLIDE:"slide"+R,SLID:"slid"+R,KEYDOWN:"keydown"+R,MOUSEENTER:"mouseenter"+R,MOUSELEAVE:"mouseleave"+R,TOUCHSTART:"touchstart"+R,TOUCHMOVE:"touchmove"+R,TOUCHEND:"touchend"+R,POINTERDOWN:"pointerdown"+R,POINTERUP:"pointerup"+R,DRAG_START:"dragstart"+R,LOAD_DATA_API:"load"+R+F,CLICK_DATA_API:"click"+R+F},Y="carousel",z="active",X="slide",G="carousel-item-right",$="carousel-item-left",J="carousel-item-next",Z="carousel-item-prev",ee="pointer-event",te=".active",ne=".active.carousel-item",ie=".carousel-item",oe=".carousel-item img",re=".carousel-item-next, .carousel-item-prev",se=".carousel-indicators",ae="[data-slide], [data-slide-to]",le='[data-ride="carousel"]',ce={TOUCH:"touch",PEN:"pen"},he=function(){function r(e,t){this._items=null,this._interval=null,this._activeElement=null,this._isPaused=!1,this._isSliding=!1,this.touchTimeout=null,this.touchStartX=0,this.touchDeltaX=0,this._config=this._getConfig(t),this._element=e,this._indicatorsElement=this._element.querySelector(se),this._touchSupported="ontouchstart"in document.documentElement||0<navigator.maxTouchPoints,this._pointerEvent=Boolean(window.PointerEvent||window.MSPointerEvent),this._addEventListeners()}var e=r.prototype;return e.next=function(){this._isSliding||this._slide(B)},e.nextWhenVisible=function(){!document.hidden&&p(this._element).is(":visible")&&"hidden"!==p(this._element).css("visibility")&&this.next()},e.prev=function(){this._isSliding||this._slide(q)},e.pause=function(e){e||(this._isPaused=!0),this._element.querySelector(re)&&(m.triggerTransitionEnd(this._element),this.cycle(!0)),clearInterval(this._interval),this._interval=null},e.cycle=function(e){e||(this._isPaused=!1),this._interval&&(clearInterval(this._interval),this._interval=null),this._config.interval&&!this._isPaused&&(this._interval=setInterval((document.visibilityState?this.nextWhenVisible:this.next).bind(this),this._config.interval))},e.to=function(e){var t=this;this._activeElement=this._element.querySelector(ne);var n=this._getItemIndex(this._activeElement);if(!(e>this._items.length-1||e<0))if(this._isSliding)p(this._element).one(V.SLID,function(){return t.to(e)});else{if(n===e)return this.pause(),void this.cycle();var i=n<e?B:q;this._slide(i,this._items[e])}},e.dispose=function(){p(this._element).off(R),p.removeData(this._element,H),this._items=null,this._config=null,this._element=null,this._interval=null,this._isPaused=null,this._isSliding=null,this._activeElement=null,this._indicatorsElement=null},e._getConfig=function(e){return e=l({},W,{},e),m.typeCheckConfig(j,e,U),e},e._handleSwipe=function(){var e=Math.abs(this.touchDeltaX);if(!(e<=40)){var t=e/this.touchDeltaX;(this.touchDeltaX=0)<t&&this.prev(),t<0&&this.next()}},e._addEventListeners=function(){var t=this;this._config.keyboard&&p(this._element).on(V.KEYDOWN,function(e){return t._keydown(e)}),"hover"===this._config.pause&&p(this._element).on(V.MOUSEENTER,function(e){return t.pause(e)}).on(V.MOUSELEAVE,function(e){return t.cycle(e)}),this._config.touch&&this._addTouchEventListeners()},e._addTouchEventListeners=function(){var t=this;if(this._touchSupported){var n=function(e){t._pointerEvent&&ce[e.originalEvent.pointerType.toUpperCase()]?t.touchStartX=e.originalEvent.clientX:t._pointerEvent||(t.touchStartX=e.originalEvent.touches[0].clientX)},i=function(e){t._pointerEvent&&ce[e.originalEvent.pointerType.toUpperCase()]&&(t.touchDeltaX=e.originalEvent.clientX-t.touchStartX),t._handleSwipe(),"hover"===t._config.pause&&(t.pause(),t.touchTimeout&&clearTimeout(t.touchTimeout),t.touchTimeout=setTimeout(function(e){return t.cycle(e)},500+t._config.interval))};p(this._element.querySelectorAll(oe)).on(V.DRAG_START,function(e){return e.preventDefault()}),this._pointerEvent?(p(this._element).on(V.POINTERDOWN,function(e){return n(e)}),p(this._element).on(V.POINTERUP,function(e){return i(e)}),this._element.classList.add(ee)):(p(this._element).on(V.TOUCHSTART,function(e){return n(e)}),p(this._element).on(V.TOUCHMOVE,function(e){return function(e){e.originalEvent.touches&&1<e.originalEvent.touches.length?t.touchDeltaX=0:t.touchDeltaX=e.originalEvent.touches[0].clientX-t.touchStartX}(e)}),p(this._element).on(V.TOUCHEND,function(e){return i(e)}))}},e._keydown=function(e){if(!/input|textarea/i.test(e.target.tagName))switch(e.which){case 37:e.preventDefault(),this.prev();break;case 39:e.preventDefault(),this.next()}},e._getItemIndex=function(e){return this._items=e&&e.parentNode?[].slice.call(e.parentNode.querySelectorAll(ie)):[],this._items.indexOf(e)},e._getItemByDirection=function(e,t){var n=e===B,i=e===q,o=this._getItemIndex(t),r=this._items.length-1;if((i&&0===o||n&&o===r)&&!this._config.wrap)return t;var s=(o+(e===q?-1:1))%this._items.length;return-1==s?this._items[this._items.length-1]:this._items[s]},e._triggerSlideEvent=function(e,t){var n=this._getItemIndex(e),i=this._getItemIndex(this._element.querySelector(ne)),o=p.Event(V.SLIDE,{relatedTarget:e,direction:t,from:i,to:n});return p(this._element).trigger(o),o},e._setActiveIndicatorElement=function(e){if(this._indicatorsElement){var t=[].slice.call(this._indicatorsElement.querySelectorAll(te));p(t).removeClass(z);var n=this._indicatorsElement.children[this._getItemIndex(e)];n&&p(n).addClass(z)}},e._slide=function(e,t){var n,i,o,r=this,s=this._element.querySelector(ne),a=this._getItemIndex(s),l=t||s&&this._getItemByDirection(e,s),c=this._getItemIndex(l),h=Boolean(this._interval);if(o=e===B?(n=$,i=J,K):(n=G,i=Z,Q),l&&p(l).hasClass(z))this._isSliding=!1;else if(!this._triggerSlideEvent(l,o).isDefaultPrevented()&&s&&l){this._isSliding=!0,h&&this.pause(),this._setActiveIndicatorElement(l);var u=p.Event(V.SLID,{relatedTarget:l,direction:o,from:a,to:c});if(p(this._element).hasClass(X)){p(l).addClass(i),m.reflow(l),p(s).addClass(n),p(l).addClass(n);var f=parseInt(l.getAttribute("data-interval"),10);f?(this._config.defaultInterval=this._config.defaultInterval||this._config.interval,this._config.interval=f):this._config.interval=this._config.defaultInterval||this._config.interval;var d=m.getTransitionDurationFromElement(s);p(s).one(m.TRANSITION_END,function(){p(l).removeClass(n+" "+i).addClass(z),p(s).removeClass(z+" "+i+" "+n),r._isSliding=!1,setTimeout(function(){return p(r._element).trigger(u)},0)}).emulateTransitionEnd(d)}else p(s).removeClass(z),p(l).addClass(z),this._isSliding=!1,p(this._element).trigger(u);h&&this.cycle()}},r._jQueryInterface=function(i){return this.each(function(){var e=p(this).data(H),t=l({},W,{},p(this).data());"object"==typeof i&&(t=l({},t,{},i));var n="string"==typeof i?i:t.slide;if(e||(e=new r(this,t),p(this).data(H,e)),"number"==typeof i)e.to(i);else if("string"==typeof n){if("undefined"==typeof e[n])throw new TypeError('No method named "'+n+'"');e[n]()}else t.interval&&t.ride&&(e.pause(),e.cycle())})},r._dataApiClickHandler=function(e){var t=m.getSelectorFromElement(this);if(t){var n=p(t)[0];if(n&&p(n).hasClass(Y)){var i=l({},p(n).data(),{},p(this).data()),o=this.getAttribute("data-slide-to");o&&(i.interval=!1),r._jQueryInterface.call(p(n),i),o&&p(n).data(H).to(o),e.preventDefault()}}},s(r,null,[{key:"VERSION",get:function(){return"4.4.1"}},{key:"Default",get:function(){return W}}]),r}();p(document).on(V.CLICK_DATA_API,ae,he._dataApiClickHandler),p(window).on(V.LOAD_DATA_API,function(){for(var e=[].slice.call(document.querySelectorAll(le)),t=0,n=e.length;t<n;t++){var i=p(e[t]);he._jQueryInterface.call(i,i.data())}}),p.fn[j]=he._jQueryInterface,p.fn[j].Constructor=he,p.fn[j].noConflict=function(){return p.fn[j]=M,he._jQueryInterface};var ue="collapse",fe="bs.collapse",de="."+fe,pe=p.fn[ue],me={toggle:!0,parent:""},ge={toggle:"boolean",parent:"(string|element)"},_e={SHOW:"show"+de,SHOWN:"shown"+de,HIDE:"hide"+de,HIDDEN:"hidden"+de,CLICK_DATA_API:"click"+de+".data-api"},ve="show",ye="collapse",Ee="collapsing",be="collapsed",we="width",Te="height",Ce=".show, .collapsing",Se='[data-toggle="collapse"]',De=function(){function a(t,e){this._isTransitioning=!1,this._element=t,this._config=this._getConfig(e),this._triggerArray=[].slice.call(document.querySelectorAll('[data-toggle="collapse"][href="#'+t.id+'"],[data-toggle="collapse"][data-target="#'+t.id+'"]'));for(var n=[].slice.call(document.querySelectorAll(Se)),i=0,o=n.length;i<o;i++){var r=n[i],s=m.getSelectorFromElement(r),a=[].slice.call(document.querySelectorAll(s)).filter(function(e){return e===t});null!==s&&0<a.length&&(this._selector=s,this._triggerArray.push(r))}this._parent=this._config.parent?this._getParent():null,this._config.parent||this._addAriaAndCollapsedClass(this._element,this._triggerArray),this._config.toggle&&this.toggle()}var e=a.prototype;return e.toggle=function(){p(this._element).hasClass(ve)?this.hide():this.show()},e.show=function(){var e,t,n=this;if(!this._isTransitioning&&!p(this._element).hasClass(ve)&&(this._parent&&0===(e=[].slice.call(this._parent.querySelectorAll(Ce)).filter(function(e){return"string"==typeof n._config.parent?e.getAttribute("data-parent")===n._config.parent:e.classList.contains(ye)})).length&&(e=null),!(e&&(t=p(e).not(this._selector).data(fe))&&t._isTransitioning))){var i=p.Event(_e.SHOW);if(p(this._element).trigger(i),!i.isDefaultPrevented()){e&&(a._jQueryInterface.call(p(e).not(this._selector),"hide"),t||p(e).data(fe,null));var o=this._getDimension();p(this._element).removeClass(ye).addClass(Ee),this._element.style[o]=0,this._triggerArray.length&&p(this._triggerArray).removeClass(be).attr("aria-expanded",!0),this.setTransitioning(!0);var r="scroll"+(o[0].toUpperCase()+o.slice(1)),s=m.getTransitionDurationFromElement(this._element);p(this._element).one(m.TRANSITION_END,function(){p(n._element).removeClass(Ee).addClass(ye).addClass(ve),n._element.style[o]="",n.setTransitioning(!1),p(n._element).trigger(_e.SHOWN)}).emulateTransitionEnd(s),this._element.style[o]=this._element[r]+"px"}}},e.hide=function(){var e=this;if(!this._isTransitioning&&p(this._element).hasClass(ve)){var t=p.Event(_e.HIDE);if(p(this._element).trigger(t),!t.isDefaultPrevented()){var n=this._getDimension();this._element.style[n]=this._element.getBoundingClientRect()[n]+"px",m.reflow(this._element),p(this._element).addClass(Ee).removeClass(ye).removeClass(ve);var i=this._triggerArray.length;if(0<i)for(var o=0;o<i;o++){var r=this._triggerArray[o],s=m.getSelectorFromElement(r);if(null!==s)p([].slice.call(document.querySelectorAll(s))).hasClass(ve)||p(r).addClass(be).attr("aria-expanded",!1)}this.setTransitioning(!0);this._element.style[n]="";var a=m.getTransitionDurationFromElement(this._element);p(this._element).one(m.TRANSITION_END,function(){e.setTransitioning(!1),p(e._element).removeClass(Ee).addClass(ye).trigger(_e.HIDDEN)}).emulateTransitionEnd(a)}}},e.setTransitioning=function(e){this._isTransitioning=e},e.dispose=function(){p.removeData(this._element,fe),this._config=null,this._parent=null,this._element=null,this._triggerArray=null,this._isTransitioning=null},e._getConfig=function(e){return(e=l({},me,{},e)).toggle=Boolean(e.toggle),m.typeCheckConfig(ue,e,ge),e},e._getDimension=function(){return p(this._element).hasClass(we)?we:Te},e._getParent=function(){var e,n=this;m.isElement(this._config.parent)?(e=this._config.parent,"undefined"!=typeof this._config.parent.jquery&&(e=this._config.parent[0])):e=document.querySelector(this._config.parent);var t='[data-toggle="collapse"][data-parent="'+this._config.parent+'"]',i=[].slice.call(e.querySelectorAll(t));return p(i).each(function(e,t){n._addAriaAndCollapsedClass(a._getTargetFromElement(t),[t])}),e},e._addAriaAndCollapsedClass=function(e,t){var n=p(e).hasClass(ve);t.length&&p(t).toggleClass(be,!n).attr("aria-expanded",n)},a._getTargetFromElement=function(e){var t=m.getSelectorFromElement(e);return t?document.querySelector(t):null},a._jQueryInterface=function(i){return this.each(function(){var e=p(this),t=e.data(fe),n=l({},me,{},e.data(),{},"object"==typeof i&&i?i:{});if(!t&&n.toggle&&/show|hide/.test(i)&&(n.toggle=!1),t||(t=new a(this,n),e.data(fe,t)),"string"==typeof i){if("undefined"==typeof t[i])throw new TypeError('No method named "'+i+'"');t[i]()}})},s(a,null,[{key:"VERSION",get:function(){return"4.4.1"}},{key:"Default",get:function(){return me}}]),a}();p(document).on(_e.CLICK_DATA_API,Se,function(e){"A"===e.currentTarget.tagName&&e.preventDefault();var n=p(this),t=m.getSelectorFromElement(this),i=[].slice.call(document.querySelectorAll(t));p(i).each(function(){var e=p(this),t=e.data(fe)?"toggle":n.data();De._jQueryInterface.call(e,t)})}),p.fn[ue]=De._jQueryInterface,p.fn[ue].Constructor=De,p.fn[ue].noConflict=function(){return p.fn[ue]=pe,De._jQueryInterface};var Ie="undefined"!=typeof window&&"undefined"!=typeof document&&"undefined"!=typeof navigator,Ae=function(){for(var e=["Edge","Trident","Firefox"],t=0;t<e.length;t+=1)if(Ie&&0<=navigator.userAgent.indexOf(e[t]))return 1;return 0}();var Oe=Ie&&window.Promise?function(e){var t=!1;return function(){t||(t=!0,window.Promise.resolve().then(function(){t=!1,e()}))}}:function(e){var t=!1;return function(){t||(t=!0,setTimeout(function(){t=!1,e()},Ae))}};function Ne(e){return e&&"[object Function]"==={}.toString.call(e)}function ke(e,t){if(1!==e.nodeType)return[];var n=e.ownerDocument.defaultView.getComputedStyle(e,null);return t?n[t]:n}function Le(e){return"HTML"===e.nodeName?e:e.parentNode||e.host}function Pe(e){if(!e)return document.body;switch(e.nodeName){case"HTML":case"BODY":return e.ownerDocument.body;case"#document":return e.body}var t=ke(e),n=t.overflow,i=t.overflowX,o=t.overflowY;return/(auto|scroll|overlay)/.test(n+o+i)?e:Pe(Le(e))}function xe(e){return e&&e.referenceNode?e.referenceNode:e}var je=Ie&&!(!window.MSInputMethodContext||!document.documentMode),He=Ie&&/MSIE 10/.test(navigator.userAgent);function Re(e){return 11===e?je:10===e?He:je||He}function Fe(e){if(!e)return document.documentElement;for(var t=Re(10)?document.body:null,n=e.offsetParent||null;n===t&&e.nextElementSibling;)n=(e=e.nextElementSibling).offsetParent;var i=n&&n.nodeName;return i&&"BODY"!==i&&"HTML"!==i?-1!==["TH","TD","TABLE"].indexOf(n.nodeName)&&"static"===ke(n,"position")?Fe(n):n:e?e.ownerDocument.documentElement:document.documentElement}function Me(e){return null!==e.parentNode?Me(e.parentNode):e}function We(e,t){if(!(e&&e.nodeType&&t&&t.nodeType))return document.documentElement;var n=e.compareDocumentPosition(t)&Node.DOCUMENT_POSITION_FOLLOWING,i=n?e:t,o=n?t:e,r=document.createRange();r.setStart(i,0),r.setEnd(o,0);var s=r.commonAncestorContainer;if(e!==s&&t!==s||i.contains(o))return function(e){var t=e.nodeName;return"BODY"!==t&&("HTML"===t||Fe(e.firstElementChild)===e)}(s)?s:Fe(s);var a=Me(e);return a.host?We(a.host,t):We(e,Me(t).host)}function Ue(e,t){var n="top"===(1<arguments.length&&void 0!==t?t:"top")?"scrollTop":"scrollLeft",i=e.nodeName;if("BODY"!==i&&"HTML"!==i)return e[n];var o=e.ownerDocument.documentElement;return(e.ownerDocument.scrollingElement||o)[n]}function Be(e,t){var n="x"===t?"Left":"Top",i="Left"==n?"Right":"Bottom";return parseFloat(e["border"+n+"Width"],10)+parseFloat(e["border"+i+"Width"],10)}function qe(e,t,n,i){return Math.max(t["offset"+e],t["scroll"+e],n["client"+e],n["offset"+e],n["scroll"+e],Re(10)?parseInt(n["offset"+e])+parseInt(i["margin"+("Height"===e?"Top":"Left")])+parseInt(i["margin"+("Height"===e?"Bottom":"Right")]):0)}function Ke(e){var t=e.body,n=e.documentElement,i=Re(10)&&getComputedStyle(n);return{height:qe("Height",t,n,i),width:qe("Width",t,n,i)}}var Qe=function(e,t,n){return t&&Ve(e.prototype,t),n&&Ve(e,n),e};function Ve(e,t){for(var n=0;n<t.length;n++){var i=t[n];i.enumerable=i.enumerable||!1,i.configurable=!0,"value"in i&&(i.writable=!0),Object.defineProperty(e,i.key,i)}}function Ye(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}var ze=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var n=arguments[t];for(var i in n)Object.prototype.hasOwnProperty.call(n,i)&&(e[i]=n[i])}return e};function Xe(e){return ze({},e,{right:e.left+e.width,bottom:e.top+e.height})}function Ge(e){var t={};try{if(Re(10)){t=e.getBoundingClientRect();var n=Ue(e,"top"),i=Ue(e,"left");t.top+=n,t.left+=i,t.bottom+=n,t.right+=i}else t=e.getBoundingClientRect()}catch(e){}var o={left:t.left,top:t.top,width:t.right-t.left,height:t.bottom-t.top},r="HTML"===e.nodeName?Ke(e.ownerDocument):{},s=r.width||e.clientWidth||o.width,a=r.height||e.clientHeight||o.height,l=e.offsetWidth-s,c=e.offsetHeight-a;if(l||c){var h=ke(e);l-=Be(h,"x"),c-=Be(h,"y"),o.width-=l,o.height-=c}return Xe(o)}function $e(e,t,n){var i=2<arguments.length&&void 0!==n&&n,o=Re(10),r="HTML"===t.nodeName,s=Ge(e),a=Ge(t),l=Pe(e),c=ke(t),h=parseFloat(c.borderTopWidth,10),u=parseFloat(c.borderLeftWidth,10);i&&r&&(a.top=Math.max(a.top,0),a.left=Math.max(a.left,0));var f=Xe({top:s.top-a.top-h,left:s.left-a.left-u,width:s.width,height:s.height});if(f.marginTop=0,f.marginLeft=0,!o&&r){var d=parseFloat(c.marginTop,10),p=parseFloat(c.marginLeft,10);f.top-=h-d,f.bottom-=h-d,f.left-=u-p,f.right-=u-p,f.marginTop=d,f.marginLeft=p}return(o&&!i?t.contains(l):t===l&&"BODY"!==l.nodeName)&&(f=function(e,t,n){var i=2<arguments.length&&void 0!==n&&n,o=Ue(t,"top"),r=Ue(t,"left"),s=i?-1:1;return e.top+=o*s,e.bottom+=o*s,e.left+=r*s,e.right+=r*s,e}(f,t)),f}function Je(e){if(!e||!e.parentElement||Re())return document.documentElement;for(var t=e.parentElement;t&&"none"===ke(t,"transform");)t=t.parentElement;return t||document.documentElement}function Ze(e,t,n,i,o){var r=4<arguments.length&&void 0!==o&&o,s={top:0,left:0},a=r?Je(e):We(e,xe(t));if("viewport"===i)s=function(e,t){var n=1<arguments.length&&void 0!==t&&t,i=e.ownerDocument.documentElement,o=$e(e,i),r=Math.max(i.clientWidth,window.innerWidth||0),s=Math.max(i.clientHeight,window.innerHeight||0),a=n?0:Ue(i),l=n?0:Ue(i,"left");return Xe({top:a-o.top+o.marginTop,left:l-o.left+o.marginLeft,width:r,height:s})}(a,r);else{var l=void 0;"scrollParent"===i?"BODY"===(l=Pe(Le(t))).nodeName&&(l=e.ownerDocument.documentElement):l="window"===i?e.ownerDocument.documentElement:i;var c=$e(l,a,r);if("HTML"!==l.nodeName||function e(t){var n=t.nodeName;if("BODY"===n||"HTML"===n)return!1;if("fixed"===ke(t,"position"))return!0;var i=Le(t);return!!i&&e(i)}(a))s=c;else{var h=Ke(e.ownerDocument),u=h.height,f=h.width;s.top+=c.top-c.marginTop,s.bottom=u+c.top,s.left+=c.left-c.marginLeft,s.right=f+c.left}}var d="number"==typeof(n=n||0);return s.left+=d?n:n.left||0,s.top+=d?n:n.top||0,s.right-=d?n:n.right||0,s.bottom-=d?n:n.bottom||0,s}function et(e,t,i,n,o,r){var s=5<arguments.length&&void 0!==r?r:0;if(-1===e.indexOf("auto"))return e;var a=Ze(i,n,s,o),l={top:{width:a.width,height:t.top-a.top},right:{width:a.right-t.right,height:a.height},bottom:{width:a.width,height:a.bottom-t.bottom},left:{width:t.left-a.left,height:a.height}},c=Object.keys(l).map(function(e){return ze({key:e},l[e],{area:function(e){return e.width*e.height}(l[e])})}).sort(function(e,t){return t.area-e.area}),h=c.filter(function(e){var t=e.width,n=e.height;return t>=i.clientWidth&&n>=i.clientHeight}),u=0<h.length?h[0].key:c[0].key,f=e.split("-")[1];return u+(f?"-"+f:"")}function tt(e,t,n,i){var o=3<arguments.length&&void 0!==i?i:null;return $e(n,o?Je(t):We(t,xe(n)),o)}function nt(e){var t=e.ownerDocument.defaultView.getComputedStyle(e),n=parseFloat(t.marginTop||0)+parseFloat(t.marginBottom||0),i=parseFloat(t.marginLeft||0)+parseFloat(t.marginRight||0);return{width:e.offsetWidth+i,height:e.offsetHeight+n}}function it(e){var t={left:"right",right:"left",bottom:"top",top:"bottom"};return e.replace(/left|right|bottom|top/g,function(e){return t[e]})}function ot(e,t,n){n=n.split("-")[0];var i=nt(e),o={width:i.width,height:i.height},r=-1!==["right","left"].indexOf(n),s=r?"top":"left",a=r?"left":"top",l=r?"height":"width",c=r?"width":"height";return o[s]=t[s]+t[l]/2-i[l]/2,o[a]=n===a?t[a]-i[c]:t[it(a)],o}function rt(e,t){return Array.prototype.find?e.find(t):e.filter(t)[0]}function st(e,n,t){return(void 0===t?e:e.slice(0,function(e,t,n){if(Array.prototype.findIndex)return e.findIndex(function(e){return e[t]===n});var i=rt(e,function(e){return e[t]===n});return e.indexOf(i)}(e,"name",t))).forEach(function(e){e.function&&console.warn("`modifier.function` is deprecated, use `modifier.fn`!");var t=e.function||e.fn;e.enabled&&Ne(t)&&(n.offsets.popper=Xe(n.offsets.popper),n.offsets.reference=Xe(n.offsets.reference),n=t(n,e))}),n}function at(e,n){return e.some(function(e){var t=e.name;return e.enabled&&t===n})}function lt(e){for(var t=[!1,"ms","Webkit","Moz","O"],n=e.charAt(0).toUpperCase()+e.slice(1),i=0;i<t.length;i++){var o=t[i],r=o?""+o+n:e;if("undefined"!=typeof document.body.style[r])return r}return null}function ct(e){var t=e.ownerDocument;return t?t.defaultView:window}function ht(e,t,n,i){n.updateBound=i,ct(e).addEventListener("resize",n.updateBound,{passive:!0});var o=Pe(e);return function e(t,n,i,o){var r="BODY"===t.nodeName,s=r?t.ownerDocument.defaultView:t;s.addEventListener(n,i,{passive:!0}),r||e(Pe(s.parentNode),n,i,o),o.push(s)}(o,"scroll",n.updateBound,n.scrollParents),n.scrollElement=o,n.eventsEnabled=!0,n}function ut(){this.state.eventsEnabled&&(cancelAnimationFrame(this.scheduleUpdate),this.state=function(e,t){return ct(e).removeEventListener("resize",t.updateBound),t.scrollParents.forEach(function(e){e.removeEventListener("scroll",t.updateBound)}),t.updateBound=null,t.scrollParents=[],t.scrollElement=null,t.eventsEnabled=!1,t}(this.reference,this.state))}function ft(e){return""!==e&&!isNaN(parseFloat(e))&&isFinite(e)}function dt(n,i){Object.keys(i).forEach(function(e){var t="";-1!==["width","height","top","right","bottom","left"].indexOf(e)&&ft(i[e])&&(t="px"),n.style[e]=i[e]+t})}function pt(e,t){function n(e){return e}var i=e.offsets,o=i.popper,r=i.reference,s=Math.round,a=Math.floor,l=s(r.width),c=s(o.width),h=-1!==["left","right"].indexOf(e.placement),u=-1!==e.placement.indexOf("-"),f=t?h||u||l%2==c%2?s:a:n,d=t?s:n;return{left:f(l%2==1&&c%2==1&&!u&&t?o.left-1:o.left),top:d(o.top),bottom:d(o.bottom),right:f(o.right)}}var mt=Ie&&/Firefox/i.test(navigator.userAgent);function gt(e,t,n){var i=rt(e,function(e){return e.name===t}),o=!!i&&e.some(function(e){return e.name===n&&e.enabled&&e.order<i.order});if(!o){var r="`"+t+"`",s="`"+n+"`";console.warn(s+" modifier is required by "+r+" modifier in order to work, be sure to include it before "+r+"!")}return o}var _t=["auto-start","auto","auto-end","top-start","top","top-end","right-start","right","right-end","bottom-end","bottom","bottom-start","left-end","left","left-start"],vt=_t.slice(3);function yt(e,t){var n=1<arguments.length&&void 0!==t&&t,i=vt.indexOf(e),o=vt.slice(i+1).concat(vt.slice(0,i));return n?o.reverse():o}var Et="flip",bt="clockwise",wt="counterclockwise";function Tt(e,o,r,t){var s=[0,0],a=-1!==["right","left"].indexOf(t),n=e.split(/(\+|\-)/).map(function(e){return e.trim()}),i=n.indexOf(rt(n,function(e){return-1!==e.search(/,|\s/)}));n[i]&&-1===n[i].indexOf(",")&&console.warn("Offsets separated by white space(s) are deprecated, use a comma (,) instead.");var l=/\s*,\s*|\s+/,c=-1!==i?[n.slice(0,i).concat([n[i].split(l)[0]]),[n[i].split(l)[1]].concat(n.slice(i+1))]:[n];return(c=c.map(function(e,t){var n=(1===t?!a:a)?"height":"width",i=!1;return e.reduce(function(e,t){return""===e[e.length-1]&&-1!==["+","-"].indexOf(t)?(e[e.length-1]=t,i=!0,e):i?(e[e.length-1]+=t,i=!1,e):e.concat(t)},[]).map(function(e){return function(e,t,n,i){var o=e.match(/((?:\-|\+)?\d*\.?\d*)(.*)/),r=+o[1],s=o[2];if(!r)return e;if(0!==s.indexOf("%"))return"vh"!==s&&"vw"!==s?r:("vh"===s?Math.max(document.documentElement.clientHeight,window.innerHeight||0):Math.max(document.documentElement.clientWidth,window.innerWidth||0))/100*r;var a=void 0;switch(s){case"%p":a=n;break;case"%":case"%r":default:a=i}return Xe(a)[t]/100*r}(e,n,o,r)})})).forEach(function(n,i){n.forEach(function(e,t){ft(e)&&(s[i]+=e*("-"===n[t-1]?-1:1))})}),s}var Ct={placement:"bottom",positionFixed:!1,eventsEnabled:!0,removeOnDestroy:!1,onCreate:function(){},onUpdate:function(){},modifiers:{shift:{order:100,enabled:!0,fn:function(e){var t=e.placement,n=t.split("-")[0],i=t.split("-")[1];if(i){var o=e.offsets,r=o.reference,s=o.popper,a=-1!==["bottom","top"].indexOf(n),l=a?"left":"top",c=a?"width":"height",h={start:Ye({},l,r[l]),end:Ye({},l,r[l]+r[c]-s[c])};e.offsets.popper=ze({},s,h[i])}return e}},offset:{order:200,enabled:!0,fn:function(e,t){var n=t.offset,i=e.placement,o=e.offsets,r=o.popper,s=o.reference,a=i.split("-")[0],l=void 0;return l=ft(+n)?[+n,0]:Tt(n,r,s,a),"left"===a?(r.top+=l[0],r.left-=l[1]):"right"===a?(r.top+=l[0],r.left+=l[1]):"top"===a?(r.left+=l[0],r.top-=l[1]):"bottom"===a&&(r.left+=l[0],r.top+=l[1]),e.popper=r,e},offset:0},preventOverflow:{order:300,enabled:!0,fn:function(e,i){var t=i.boundariesElement||Fe(e.instance.popper);e.instance.reference===t&&(t=Fe(t));var n=lt("transform"),o=e.instance.popper.style,r=o.top,s=o.left,a=o[n];o.top="",o.left="",o[n]="";var l=Ze(e.instance.popper,e.instance.reference,i.padding,t,e.positionFixed);o.top=r,o.left=s,o[n]=a,i.boundaries=l;var c=i.priority,h=e.offsets.popper,u={primary:function(e){var t=h[e];return h[e]<l[e]&&!i.escapeWithReference&&(t=Math.max(h[e],l[e])),Ye({},e,t)},secondary:function(e){var t="right"===e?"left":"top",n=h[t];return h[e]>l[e]&&!i.escapeWithReference&&(n=Math.min(h[t],l[e]-("right"===e?h.width:h.height))),Ye({},t,n)}};return c.forEach(function(e){var t=-1!==["left","top"].indexOf(e)?"primary":"secondary";h=ze({},h,u[t](e))}),e.offsets.popper=h,e},priority:["left","right","top","bottom"],padding:5,boundariesElement:"scrollParent"},keepTogether:{order:400,enabled:!0,fn:function(e){var t=e.offsets,n=t.popper,i=t.reference,o=e.placement.split("-")[0],r=Math.floor,s=-1!==["top","bottom"].indexOf(o),a=s?"right":"bottom",l=s?"left":"top",c=s?"width":"height";return n[a]<r(i[l])&&(e.offsets.popper[l]=r(i[l])-n[c]),n[l]>r(i[a])&&(e.offsets.popper[l]=r(i[a])),e}},arrow:{order:500,enabled:!0,fn:function(e,t){var n;if(!gt(e.instance.modifiers,"arrow","keepTogether"))return e;var i=t.element;if("string"==typeof i){if(!(i=e.instance.popper.querySelector(i)))return e}else if(!e.instance.popper.contains(i))return console.warn("WARNING: `arrow.element` must be child of its popper element!"),e;var o=e.placement.split("-")[0],r=e.offsets,s=r.popper,a=r.reference,l=-1!==["left","right"].indexOf(o),c=l?"height":"width",h=l?"Top":"Left",u=h.toLowerCase(),f=l?"left":"top",d=l?"bottom":"right",p=nt(i)[c];a[d]-p<s[u]&&(e.offsets.popper[u]-=s[u]-(a[d]-p)),a[u]+p>s[d]&&(e.offsets.popper[u]+=a[u]+p-s[d]),e.offsets.popper=Xe(e.offsets.popper);var m=a[u]+a[c]/2-p/2,g=ke(e.instance.popper),_=parseFloat(g["margin"+h],10),v=parseFloat(g["border"+h+"Width"],10),y=m-e.offsets.popper[u]-_-v;return y=Math.max(Math.min(s[c]-p,y),0),e.arrowElement=i,e.offsets.arrow=(Ye(n={},u,Math.round(y)),Ye(n,f,""),n),e},element:"[x-arrow]"},flip:{order:600,enabled:!0,fn:function(m,g){if(at(m.instance.modifiers,"inner"))return m;if(m.flipped&&m.placement===m.originalPlacement)return m;var _=Ze(m.instance.popper,m.instance.reference,g.padding,g.boundariesElement,m.positionFixed),v=m.placement.split("-")[0],y=it(v),E=m.placement.split("-")[1]||"",b=[];switch(g.behavior){case Et:b=[v,y];break;case bt:b=yt(v);break;case wt:b=yt(v,!0);break;default:b=g.behavior}return b.forEach(function(e,t){if(v!==e||b.length===t+1)return m;v=m.placement.split("-")[0],y=it(v);var n=m.offsets.popper,i=m.offsets.reference,o=Math.floor,r="left"===v&&o(n.right)>o(i.left)||"right"===v&&o(n.left)<o(i.right)||"top"===v&&o(n.bottom)>o(i.top)||"bottom"===v&&o(n.top)<o(i.bottom),s=o(n.left)<o(_.left),a=o(n.right)>o(_.right),l=o(n.top)<o(_.top),c=o(n.bottom)>o(_.bottom),h="left"===v&&s||"right"===v&&a||"top"===v&&l||"bottom"===v&&c,u=-1!==["top","bottom"].indexOf(v),f=!!g.flipVariations&&(u&&"start"===E&&s||u&&"end"===E&&a||!u&&"start"===E&&l||!u&&"end"===E&&c),d=!!g.flipVariationsByContent&&(u&&"start"===E&&a||u&&"end"===E&&s||!u&&"start"===E&&c||!u&&"end"===E&&l),p=f||d;(r||h||p)&&(m.flipped=!0,(r||h)&&(v=b[t+1]),p&&(E=function(e){return"end"===e?"start":"start"===e?"end":e}(E)),m.placement=v+(E?"-"+E:""),m.offsets.popper=ze({},m.offsets.popper,ot(m.instance.popper,m.offsets.reference,m.placement)),m=st(m.instance.modifiers,m,"flip"))}),m},behavior:"flip",padding:5,boundariesElement:"viewport",flipVariations:!1,flipVariationsByContent:!1},inner:{order:700,enabled:!1,fn:function(e){var t=e.placement,n=t.split("-")[0],i=e.offsets,o=i.popper,r=i.reference,s=-1!==["left","right"].indexOf(n),a=-1===["top","left"].indexOf(n);return o[s?"left":"top"]=r[n]-(a?o[s?"width":"height"]:0),e.placement=it(t),e.offsets.popper=Xe(o),e}},hide:{order:800,enabled:!0,fn:function(e){if(!gt(e.instance.modifiers,"hide","preventOverflow"))return e;var t=e.offsets.reference,n=rt(e.instance.modifiers,function(e){return"preventOverflow"===e.name}).boundaries;if(t.bottom<n.top||t.left>n.right||t.top>n.bottom||t.right<n.left){if(!0===e.hide)return e;e.hide=!0,e.attributes["x-out-of-boundaries"]=""}else{if(!1===e.hide)return e;e.hide=!1,e.attributes["x-out-of-boundaries"]=!1}return e}},computeStyle:{order:850,enabled:!0,fn:function(e,t){var n=t.x,i=t.y,o=e.offsets.popper,r=rt(e.instance.modifiers,function(e){return"applyStyle"===e.name}).gpuAcceleration;void 0!==r&&console.warn("WARNING: `gpuAcceleration` option moved to `computeStyle` modifier and will not be supported in future versions of Popper.js!");var s=void 0!==r?r:t.gpuAcceleration,a=Fe(e.instance.popper),l=Ge(a),c={position:o.position},h=pt(e,window.devicePixelRatio<2||!mt),u="bottom"===n?"top":"bottom",f="right"===i?"left":"right",d=lt("transform"),p=void 0,m=void 0;if(m="bottom"==u?"HTML"===a.nodeName?-a.clientHeight+h.bottom:-l.height+h.bottom:h.top,p="right"==f?"HTML"===a.nodeName?-a.clientWidth+h.right:-l.width+h.right:h.left,s&&d)c[d]="translate3d("+p+"px, "+m+"px, 0)",c[u]=0,c[f]=0,c.willChange="transform";else{var g="bottom"==u?-1:1,_="right"==f?-1:1;c[u]=m*g,c[f]=p*_,c.willChange=u+", "+f}var v={"x-placement":e.placement};return e.attributes=ze({},v,e.attributes),e.styles=ze({},c,e.styles),e.arrowStyles=ze({},e.offsets.arrow,e.arrowStyles),e},gpuAcceleration:!0,x:"bottom",y:"right"},applyStyle:{order:900,enabled:!0,fn:function(e){return dt(e.instance.popper,e.styles),function(t,n){Object.keys(n).forEach(function(e){!1!==n[e]?t.setAttribute(e,n[e]):t.removeAttribute(e)})}(e.instance.popper,e.attributes),e.arrowElement&&Object.keys(e.arrowStyles).length&&dt(e.arrowElement,e.arrowStyles),e},onLoad:function(e,t,n,i,o){var r=tt(o,t,e,n.positionFixed),s=et(n.placement,r,t,e,n.modifiers.flip.boundariesElement,n.modifiers.flip.padding);return t.setAttribute("x-placement",s),dt(t,{position:n.positionFixed?"fixed":"absolute"}),n},gpuAcceleration:void 0}}},St=(Qe(Dt,[{key:"update",value:function(){return function(){if(!this.state.isDestroyed){var e={instance:this,styles:{},arrowStyles:{},attributes:{},flipped:!1,offsets:{}};e.offsets.reference=tt(this.state,this.popper,this.reference,this.options.positionFixed),e.placement=et(this.options.placement,e.offsets.reference,this.popper,this.reference,this.options.modifiers.flip.boundariesElement,this.options.modifiers.flip.padding),e.originalPlacement=e.placement,e.positionFixed=this.options.positionFixed,e.offsets.popper=ot(this.popper,e.offsets.reference,e.placement),e.offsets.popper.position=this.options.positionFixed?"fixed":"absolute",e=st(this.modifiers,e),this.state.isCreated?this.options.onUpdate(e):(this.state.isCreated=!0,this.options.onCreate(e))}}.call(this)}},{key:"destroy",value:function(){return function(){return this.state.isDestroyed=!0,at(this.modifiers,"applyStyle")&&(this.popper.removeAttribute("x-placement"),this.popper.style.position="",this.popper.style.top="",this.popper.style.left="",this.popper.style.right="",this.popper.style.bottom="",this.popper.style.willChange="",this.popper.style[lt("transform")]=""),this.disableEventListeners(),this.options.removeOnDestroy&&this.popper.parentNode.removeChild(this.popper),this}.call(this)}},{key:"enableEventListeners",value:function(){return function(){this.state.eventsEnabled||(this.state=ht(this.reference,this.options,this.state,this.scheduleUpdate))}.call(this)}},{key:"disableEventListeners",value:function(){return ut.call(this)}}]),Dt);function Dt(e,t){var n=this,i=2<arguments.length&&void 0!==arguments[2]?arguments[2]:{};!function(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}(this,Dt),this.scheduleUpdate=function(){return requestAnimationFrame(n.update)},this.update=Oe(this.update.bind(this)),this.options=ze({},Dt.Defaults,i),this.state={isDestroyed:!1,isCreated:!1,scrollParents:[]},this.reference=e&&e.jquery?e[0]:e,this.popper=t&&t.jquery?t[0]:t,this.options.modifiers={},Object.keys(ze({},Dt.Defaults.modifiers,i.modifiers)).forEach(function(e){n.options.modifiers[e]=ze({},Dt.Defaults.modifiers[e]||{},i.modifiers?i.modifiers[e]:{})}),this.modifiers=Object.keys(this.options.modifiers).map(function(e){return ze({name:e},n.options.modifiers[e])}).sort(function(e,t){return e.order-t.order}),this.modifiers.forEach(function(e){e.enabled&&Ne(e.onLoad)&&e.onLoad(n.reference,n.popper,n.options,e,n.state)}),this.update();var o=this.options.eventsEnabled;o&&this.enableEventListeners(),this.state.eventsEnabled=o}St.Utils=("undefined"!=typeof window?window:global).PopperUtils,St.placements=_t,St.Defaults=Ct;var It="dropdown",At="bs.dropdown",Ot="."+At,Nt=".data-api",kt=p.fn[It],Lt=new RegExp("38|40|27"),Pt={HIDE:"hide"+Ot,HIDDEN:"hidden"+Ot,SHOW:"show"+Ot,SHOWN:"shown"+Ot,CLICK:"click"+Ot,CLICK_DATA_API:"click"+Ot+Nt,KEYDOWN_DATA_API:"keydown"+Ot+Nt,KEYUP_DATA_API:"keyup"+Ot+Nt},xt="disabled",jt="show",Ht="dropup",Rt="dropright",Ft="dropleft",Mt="dropdown-menu-right",Wt="position-static",Ut='[data-toggle="dropdown"]',Bt=".dropdown form",qt=".dropdown-menu",Kt=".navbar-nav",Qt=".dropdown-menu .dropdown-item:not(.disabled):not(:disabled)",Vt="top-start",Yt="top-end",zt="bottom-start",Xt="bottom-end",Gt="right-start",$t="left-start",Jt={offset:0,flip:!0,boundary:"scrollParent",reference:"toggle",display:"dynamic",popperConfig:null},Zt={offset:"(number|string|function)",flip:"boolean",boundary:"(string|element)",reference:"(string|element)",display:"string",popperConfig:"(null|object)"},en=function(){function c(e,t){this._element=e,this._popper=null,this._config=this._getConfig(t),this._menu=this._getMenuElement(),this._inNavbar=this._detectNavbar(),this._addEventListeners()}var e=c.prototype;return e.toggle=function(){if(!this._element.disabled&&!p(this._element).hasClass(xt)){var e=p(this._menu).hasClass(jt);c._clearMenus(),e||this.show(!0)}},e.show=function(e){if(void 0===e&&(e=!1),!(this._element.disabled||p(this._element).hasClass(xt)||p(this._menu).hasClass(jt))){var t={relatedTarget:this._element},n=p.Event(Pt.SHOW,t),i=c._getParentFromElement(this._element);if(p(i).trigger(n),!n.isDefaultPrevented()){if(!this._inNavbar&&e){if("undefined"==typeof St)throw new TypeError("Bootstrap's dropdowns require Popper.js (https://popper.js.org/)");var o=this._element;"parent"===this._config.reference?o=i:m.isElement(this._config.reference)&&(o=this._config.reference,"undefined"!=typeof this._config.reference.jquery&&(o=this._config.reference[0])),"scrollParent"!==this._config.boundary&&p(i).addClass(Wt),this._popper=new St(o,this._menu,this._getPopperConfig())}"ontouchstart"in document.documentElement&&0===p(i).closest(Kt).length&&p(document.body).children().on("mouseover",null,p.noop),this._element.focus(),this._element.setAttribute("aria-expanded",!0),p(this._menu).toggleClass(jt),p(i).toggleClass(jt).trigger(p.Event(Pt.SHOWN,t))}}},e.hide=function(){if(!this._element.disabled&&!p(this._element).hasClass(xt)&&p(this._menu).hasClass(jt)){var e={relatedTarget:this._element},t=p.Event(Pt.HIDE,e),n=c._getParentFromElement(this._element);p(n).trigger(t),t.isDefaultPrevented()||(this._popper&&this._popper.destroy(),p(this._menu).toggleClass(jt),p(n).toggleClass(jt).trigger(p.Event(Pt.HIDDEN,e)))}},e.dispose=function(){p.removeData(this._element,At),p(this._element).off(Ot),this._element=null,(this._menu=null)!==this._popper&&(this._popper.destroy(),this._popper=null)},e.update=function(){this._inNavbar=this._detectNavbar(),null!==this._popper&&this._popper.scheduleUpdate()},e._addEventListeners=function(){var t=this;p(this._element).on(Pt.CLICK,function(e){e.preventDefault(),e.stopPropagation(),t.toggle()})},e._getConfig=function(e){return e=l({},this.constructor.Default,{},p(this._element).data(),{},e),m.typeCheckConfig(It,e,this.constructor.DefaultType),e},e._getMenuElement=function(){if(!this._menu){var e=c._getParentFromElement(this._element);e&&(this._menu=e.querySelector(qt))}return this._menu},e._getPlacement=function(){var e=p(this._element.parentNode),t=zt;return e.hasClass(Ht)?(t=Vt,p(this._menu).hasClass(Mt)&&(t=Yt)):e.hasClass(Rt)?t=Gt:e.hasClass(Ft)?t=$t:p(this._menu).hasClass(Mt)&&(t=Xt),t},e._detectNavbar=function(){return 0<p(this._element).closest(".navbar").length},e._getOffset=function(){var t=this,e={};return"function"==typeof this._config.offset?e.fn=function(e){return e.offsets=l({},e.offsets,{},t._config.offset(e.offsets,t._element)||{}),e}:e.offset=this._config.offset,e},e._getPopperConfig=function(){var e={placement:this._getPlacement(),modifiers:{offset:this._getOffset(),flip:{enabled:this._config.flip},preventOverflow:{boundariesElement:this._config.boundary}}};return"static"===this._config.display&&(e.modifiers.applyStyle={enabled:!1}),l({},e,{},this._config.popperConfig)},c._jQueryInterface=function(t){return this.each(function(){var e=p(this).data(At);if(e||(e=new c(this,"object"==typeof t?t:null),p(this).data(At,e)),"string"==typeof t){if("undefined"==typeof e[t])throw new TypeError('No method named "'+t+'"');e[t]()}})},c._clearMenus=function(e){if(!e||3!==e.which&&("keyup"!==e.type||9===e.which))for(var t=[].slice.call(document.querySelectorAll(Ut)),n=0,i=t.length;n<i;n++){var o=c._getParentFromElement(t[n]),r=p(t[n]).data(At),s={relatedTarget:t[n]};if(e&&"click"===e.type&&(s.clickEvent=e),r){var a=r._menu;if(p(o).hasClass(jt)&&!(e&&("click"===e.type&&/input|textarea/i.test(e.target.tagName)||"keyup"===e.type&&9===e.which)&&p.contains(o,e.target))){var l=p.Event(Pt.HIDE,s);p(o).trigger(l),l.isDefaultPrevented()||("ontouchstart"in document.documentElement&&p(document.body).children().off("mouseover",null,p.noop),t[n].setAttribute("aria-expanded","false"),r._popper&&r._popper.destroy(),p(a).removeClass(jt),p(o).removeClass(jt).trigger(p.Event(Pt.HIDDEN,s)))}}}},c._getParentFromElement=function(e){var t,n=m.getSelectorFromElement(e);return n&&(t=document.querySelector(n)),t||e.parentNode},c._dataApiKeydownHandler=function(e){if((/input|textarea/i.test(e.target.tagName)?!(32===e.which||27!==e.which&&(40!==e.which&&38!==e.which||p(e.target).closest(qt).length)):Lt.test(e.which))&&(e.preventDefault(),e.stopPropagation(),!this.disabled&&!p(this).hasClass(xt))){var t=c._getParentFromElement(this),n=p(t).hasClass(jt);if(n||27!==e.which)if(n&&(!n||27!==e.which&&32!==e.which)){var i=[].slice.call(t.querySelectorAll(Qt)).filter(function(e){return p(e).is(":visible")});if(0!==i.length){var o=i.indexOf(e.target);38===e.which&&0<o&&o--,40===e.which&&o<i.length-1&&o++,o<0&&(o=0),i[o].focus()}}else{if(27===e.which){var r=t.querySelector(Ut);p(r).trigger("focus")}p(this).trigger("click")}}},s(c,null,[{key:"VERSION",get:function(){return"4.4.1"}},{key:"Default",get:function(){return Jt}},{key:"DefaultType",get:function(){return Zt}}]),c}();p(document).on(Pt.KEYDOWN_DATA_API,Ut,en._dataApiKeydownHandler).on(Pt.KEYDOWN_DATA_API,qt,en._dataApiKeydownHandler).on(Pt.CLICK_DATA_API+" "+Pt.KEYUP_DATA_API,en._clearMenus).on(Pt.CLICK_DATA_API,Ut,function(e){e.preventDefault(),e.stopPropagation(),en._jQueryInterface.call(p(this),"toggle")}).on(Pt.CLICK_DATA_API,Bt,function(e){e.stopPropagation()}),p.fn[It]=en._jQueryInterface,p.fn[It].Constructor=en,p.fn[It].noConflict=function(){return p.fn[It]=kt,en._jQueryInterface};var tn="modal",nn="bs.modal",on="."+nn,rn=p.fn[tn],sn={backdrop:!0,keyboard:!0,focus:!0,show:!0},an={backdrop:"(boolean|string)",keyboard:"boolean",focus:"boolean",show:"boolean"},ln={HIDE:"hide"+on,HIDE_PREVENTED:"hidePrevented"+on,HIDDEN:"hidden"+on,SHOW:"show"+on,SHOWN:"shown"+on,FOCUSIN:"focusin"+on,RESIZE:"resize"+on,CLICK_DISMISS:"click.dismiss"+on,KEYDOWN_DISMISS:"keydown.dismiss"+on,MOUSEUP_DISMISS:"mouseup.dismiss"+on,MOUSEDOWN_DISMISS:"mousedown.dismiss"+on,CLICK_DATA_API:"click"+on+".data-api"},cn="modal-dialog-scrollable",hn="modal-scrollbar-measure",un="modal-backdrop",fn="modal-open",dn="fade",pn="show",mn="modal-static",gn=".modal-dialog",_n=".modal-body",vn='[data-toggle="modal"]',yn='[data-dismiss="modal"]',En=".fixed-top, .fixed-bottom, .is-fixed, .sticky-top",bn=".sticky-top",wn=function(){function o(e,t){this._config=this._getConfig(t),this._element=e,this._dialog=e.querySelector(gn),this._backdrop=null,this._isShown=!1,this._isBodyOverflowing=!1,this._ignoreBackdropClick=!1,this._isTransitioning=!1,this._scrollbarWidth=0}var e=o.prototype;return e.toggle=function(e){return this._isShown?this.hide():this.show(e)},e.show=function(e){var t=this;if(!this._isShown&&!this._isTransitioning){p(this._element).hasClass(dn)&&(this._isTransitioning=!0);var n=p.Event(ln.SHOW,{relatedTarget:e});p(this._element).trigger(n),this._isShown||n.isDefaultPrevented()||(this._isShown=!0,this._checkScrollbar(),this._setScrollbar(),this._adjustDialog(),this._setEscapeEvent(),this._setResizeEvent(),p(this._element).on(ln.CLICK_DISMISS,yn,function(e){return t.hide(e)}),p(this._dialog).on(ln.MOUSEDOWN_DISMISS,function(){p(t._element).one(ln.MOUSEUP_DISMISS,function(e){p(e.target).is(t._element)&&(t._ignoreBackdropClick=!0)})}),this._showBackdrop(function(){return t._showElement(e)}))}},e.hide=function(e){var t=this;if(e&&e.preventDefault(),this._isShown&&!this._isTransitioning){var n=p.Event(ln.HIDE);if(p(this._element).trigger(n),this._isShown&&!n.isDefaultPrevented()){this._isShown=!1;var i=p(this._element).hasClass(dn);if(i&&(this._isTransitioning=!0),this._setEscapeEvent(),this._setResizeEvent(),p(document).off(ln.FOCUSIN),p(this._element).removeClass(pn),p(this._element).off(ln.CLICK_DISMISS),p(this._dialog).off(ln.MOUSEDOWN_DISMISS),i){var o=m.getTransitionDurationFromElement(this._element);p(this._element).one(m.TRANSITION_END,function(e){return t._hideModal(e)}).emulateTransitionEnd(o)}else this._hideModal()}}},e.dispose=function(){[window,this._element,this._dialog].forEach(function(e){return p(e).off(on)}),p(document).off(ln.FOCUSIN),p.removeData(this._element,nn),this._config=null,this._element=null,this._dialog=null,this._backdrop=null,this._isShown=null,this._isBodyOverflowing=null,this._ignoreBackdropClick=null,this._isTransitioning=null,this._scrollbarWidth=null},e.handleUpdate=function(){this._adjustDialog()},e._getConfig=function(e){return e=l({},sn,{},e),m.typeCheckConfig(tn,e,an),e},e._triggerBackdropTransition=function(){var e=this;if("static"===this._config.backdrop){var t=p.Event(ln.HIDE_PREVENTED);if(p(this._element).trigger(t),t.defaultPrevented)return;this._element.classList.add(mn);var n=m.getTransitionDurationFromElement(this._element);p(this._element).one(m.TRANSITION_END,function(){e._element.classList.remove(mn)}).emulateTransitionEnd(n),this._element.focus()}else this.hide()},e._showElement=function(e){var t=this,n=p(this._element).hasClass(dn),i=this._dialog?this._dialog.querySelector(_n):null;this._element.parentNode&&this._element.parentNode.nodeType===Node.ELEMENT_NODE||document.body.appendChild(this._element),this._element.style.display="block",this._element.removeAttribute("aria-hidden"),this._element.setAttribute("aria-modal",!0),p(this._dialog).hasClass(cn)&&i?i.scrollTop=0:this._element.scrollTop=0,n&&m.reflow(this._element),p(this._element).addClass(pn),this._config.focus&&this._enforceFocus();function o(){t._config.focus&&t._element.focus(),t._isTransitioning=!1,p(t._element).trigger(r)}var r=p.Event(ln.SHOWN,{relatedTarget:e});if(n){var s=m.getTransitionDurationFromElement(this._dialog);p(this._dialog).one(m.TRANSITION_END,o).emulateTransitionEnd(s)}else o()},e._enforceFocus=function(){var t=this;p(document).off(ln.FOCUSIN).on(ln.FOCUSIN,function(e){document!==e.target&&t._element!==e.target&&0===p(t._element).has(e.target).length&&t._element.focus()})},e._setEscapeEvent=function(){var t=this;this._isShown&&this._config.keyboard?p(this._element).on(ln.KEYDOWN_DISMISS,function(e){27===e.which&&t._triggerBackdropTransition()}):this._isShown||p(this._element).off(ln.KEYDOWN_DISMISS)},e._setResizeEvent=function(){var t=this;this._isShown?p(window).on(ln.RESIZE,function(e){return t.handleUpdate(e)}):p(window).off(ln.RESIZE)},e._hideModal=function(){var e=this;this._element.style.display="none",this._element.setAttribute("aria-hidden",!0),this._element.removeAttribute("aria-modal"),this._isTransitioning=!1,this._showBackdrop(function(){p(document.body).removeClass(fn),e._resetAdjustments(),e._resetScrollbar(),p(e._element).trigger(ln.HIDDEN)})},e._removeBackdrop=function(){this._backdrop&&(p(this._backdrop).remove(),this._backdrop=null)},e._showBackdrop=function(e){var t=this,n=p(this._element).hasClass(dn)?dn:"";if(this._isShown&&this._config.backdrop){if(this._backdrop=document.createElement("div"),this._backdrop.className=un,n&&this._backdrop.classList.add(n),p(this._backdrop).appendTo(document.body),p(this._element).on(ln.CLICK_DISMISS,function(e){t._ignoreBackdropClick?t._ignoreBackdropClick=!1:e.target===e.currentTarget&&t._triggerBackdropTransition()}),n&&m.reflow(this._backdrop),p(this._backdrop).addClass(pn),!e)return;if(!n)return void e();var i=m.getTransitionDurationFromElement(this._backdrop);p(this._backdrop).one(m.TRANSITION_END,e).emulateTransitionEnd(i)}else if(!this._isShown&&this._backdrop){p(this._backdrop).removeClass(pn);var o=function(){t._removeBackdrop(),e&&e()};if(p(this._element).hasClass(dn)){var r=m.getTransitionDurationFromElement(this._backdrop);p(this._backdrop).one(m.TRANSITION_END,o).emulateTransitionEnd(r)}else o()}else e&&e()},e._adjustDialog=function(){var e=this._element.scrollHeight>document.documentElement.clientHeight;!this._isBodyOverflowing&&e&&(this._element.style.paddingLeft=this._scrollbarWidth+"px"),this._isBodyOverflowing&&!e&&(this._element.style.paddingRight=this._scrollbarWidth+"px")},e._resetAdjustments=function(){this._element.style.paddingLeft="",this._element.style.paddingRight=""},e._checkScrollbar=function(){var e=document.body.getBoundingClientRect();this._isBodyOverflowing=e.left+e.right<window.innerWidth,this._scrollbarWidth=this._getScrollbarWidth()},e._setScrollbar=function(){var o=this;if(this._isBodyOverflowing){var e=[].slice.call(document.querySelectorAll(En)),t=[].slice.call(document.querySelectorAll(bn));p(e).each(function(e,t){var n=t.style.paddingRight,i=p(t).css("padding-right");p(t).data("padding-right",n).css("padding-right",parseFloat(i)+o._scrollbarWidth+"px")}),p(t).each(function(e,t){var n=t.style.marginRight,i=p(t).css("margin-right");p(t).data("margin-right",n).css("margin-right",parseFloat(i)-o._scrollbarWidth+"px")});var n=document.body.style.paddingRight,i=p(document.body).css("padding-right");p(document.body).data("padding-right",n).css("padding-right",parseFloat(i)+this._scrollbarWidth+"px")}p(document.body).addClass(fn)},e._resetScrollbar=function(){var e=[].slice.call(document.querySelectorAll(En));p(e).each(function(e,t){var n=p(t).data("padding-right");p(t).removeData("padding-right"),t.style.paddingRight=n||""});var t=[].slice.call(document.querySelectorAll(""+bn));p(t).each(function(e,t){var n=p(t).data("margin-right");"undefined"!=typeof n&&p(t).css("margin-right",n).removeData("margin-right")});var n=p(document.body).data("padding-right");p(document.body).removeData("padding-right"),document.body.style.paddingRight=n||""},e._getScrollbarWidth=function(){var e=document.createElement("div");e.className=hn,document.body.appendChild(e);var t=e.getBoundingClientRect().width-e.clientWidth;return document.body.removeChild(e),t},o._jQueryInterface=function(n,i){return this.each(function(){var e=p(this).data(nn),t=l({},sn,{},p(this).data(),{},"object"==typeof n&&n?n:{});if(e||(e=new o(this,t),p(this).data(nn,e)),"string"==typeof n){if("undefined"==typeof e[n])throw new TypeError('No method named "'+n+'"');e[n](i)}else t.show&&e.show(i)})},s(o,null,[{key:"VERSION",get:function(){return"4.4.1"}},{key:"Default",get:function(){return sn}}]),o}();p(document).on(ln.CLICK_DATA_API,vn,function(e){var t,n=this,i=m.getSelectorFromElement(this);i&&(t=document.querySelector(i));var o=p(t).data(nn)?"toggle":l({},p(t).data(),{},p(this).data());"A"!==this.tagName&&"AREA"!==this.tagName||e.preventDefault();var r=p(t).one(ln.SHOW,function(e){e.isDefaultPrevented()||r.one(ln.HIDDEN,function(){p(n).is(":visible")&&n.focus()})});wn._jQueryInterface.call(p(t),o,this)}),p.fn[tn]=wn._jQueryInterface,p.fn[tn].Constructor=wn,p.fn[tn].noConflict=function(){return p.fn[tn]=rn,wn._jQueryInterface};var Tn=["background","cite","href","itemtype","longdesc","poster","src","xlink:href"],Cn={"*":["class","dir","id","lang","role",/^aria-[\w-]*$/i],a:["target","href","title","rel"],area:[],b:[],br:[],col:[],code:[],div:[],em:[],hr:[],h1:[],h2:[],h3:[],h4:[],h5:[],h6:[],i:[],img:["src","alt","title","width","height"],li:[],ol:[],p:[],pre:[],s:[],small:[],span:[],sub:[],sup:[],strong:[],u:[],ul:[]},Sn=/^(?:(?:https?|mailto|ftp|tel|file):|[^&:/?#]*(?:[/?#]|$))/gi,Dn=/^data:(?:image\/(?:bmp|gif|jpeg|jpg|png|tiff|webp)|video\/(?:mpeg|mp4|ogg|webm)|audio\/(?:mp3|oga|ogg|opus));base64,[a-z0-9+/]+=*$/i;function In(e,r,t){if(0===e.length)return e;if(t&&"function"==typeof t)return t(e);for(var n=(new window.DOMParser).parseFromString(e,"text/html"),s=Object.keys(r),a=[].slice.call(n.body.querySelectorAll("*")),i=function(e){var t=a[e],n=t.nodeName.toLowerCase();if(-1===s.indexOf(t.nodeName.toLowerCase()))return t.parentNode.removeChild(t),"continue";var i=[].slice.call(t.attributes),o=[].concat(r["*"]||[],r[n]||[]);i.forEach(function(e){!function(e,t){var n=e.nodeName.toLowerCase();if(-1!==t.indexOf(n))return-1===Tn.indexOf(n)||Boolean(e.nodeValue.match(Sn)||e.nodeValue.match(Dn));for(var i=t.filter(function(e){return e instanceof RegExp}),o=0,r=i.length;o<r;o++)if(n.match(i[o]))return!0;return!1}(e,o)&&t.removeAttribute(e.nodeName)})},o=0,l=a.length;o<l;o++)i(o);return n.body.innerHTML}var An="tooltip",On="bs.tooltip",Nn="."+On,kn=p.fn[An],Ln="bs-tooltip",Pn=new RegExp("(^|\\s)"+Ln+"\\S+","g"),xn=["sanitize","whiteList","sanitizeFn"],jn={animation:"boolean",template:"string",title:"(string|element|function)",trigger:"string",delay:"(number|object)",html:"boolean",selector:"(string|boolean)",placement:"(string|function)",offset:"(number|string|function)",container:"(string|element|boolean)",fallbackPlacement:"(string|array)",boundary:"(string|element)",sanitize:"boolean",sanitizeFn:"(null|function)",whiteList:"object",popperConfig:"(null|object)"},Hn={AUTO:"auto",TOP:"top",RIGHT:"right",BOTTOM:"bottom",LEFT:"left"},Rn={animation:!0,template:'<div class="tooltip" role="tooltip"><div class="arrow"></div><div class="tooltip-inner"></div></div>',trigger:"hover focus",title:"",delay:0,html:!1,selector:!1,placement:"top",offset:0,container:!1,fallbackPlacement:"flip",boundary:"scrollParent",sanitize:!0,sanitizeFn:null,whiteList:Cn,popperConfig:null},Fn="show",Mn="out",Wn={HIDE:"hide"+Nn,HIDDEN:"hidden"+Nn,SHOW:"show"+Nn,SHOWN:"shown"+Nn,INSERTED:"inserted"+Nn,CLICK:"click"+Nn,FOCUSIN:"focusin"+Nn,FOCUSOUT:"focusout"+Nn,MOUSEENTER:"mouseenter"+Nn,MOUSELEAVE:"mouseleave"+Nn},Un="fade",Bn="show",qn=".tooltip-inner",Kn=".arrow",Qn="hover",Vn="focus",Yn="click",zn="manual",Xn=function(){function i(e,t){if("undefined"==typeof St)throw new TypeError("Bootstrap's tooltips require Popper.js (https://popper.js.org/)");this._isEnabled=!0,this._timeout=0,this._hoverState="",this._activeTrigger={},this._popper=null,this.element=e,this.config=this._getConfig(t),this.tip=null,this._setListeners()}var e=i.prototype;return e.enable=function(){this._isEnabled=!0},e.disable=function(){this._isEnabled=!1},e.toggleEnabled=function(){this._isEnabled=!this._isEnabled},e.toggle=function(e){if(this._isEnabled)if(e){var t=this.constructor.DATA_KEY,n=p(e.currentTarget).data(t);n||(n=new this.constructor(e.currentTarget,this._getDelegateConfig()),p(e.currentTarget).data(t,n)),n._activeTrigger.click=!n._activeTrigger.click,n._isWithActiveTrigger()?n._enter(null,n):n._leave(null,n)}else{if(p(this.getTipElement()).hasClass(Bn))return void this._leave(null,this);this._enter(null,this)}},e.dispose=function(){clearTimeout(this._timeout),p.removeData(this.element,this.constructor.DATA_KEY),p(this.element).off(this.constructor.EVENT_KEY),p(this.element).closest(".modal").off("hide.bs.modal",this._hideModalHandler),this.tip&&p(this.tip).remove(),this._isEnabled=null,this._timeout=null,this._hoverState=null,this._activeTrigger=null,this._popper&&this._popper.destroy(),this._popper=null,this.element=null,this.config=null,this.tip=null},e.show=function(){var t=this;if("none"===p(this.element).css("display"))throw new Error("Please use show on visible elements");var e=p.Event(this.constructor.Event.SHOW);if(this.isWithContent()&&this._isEnabled){p(this.element).trigger(e);var n=m.findShadowRoot(this.element),i=p.contains(null!==n?n:this.element.ownerDocument.documentElement,this.element);if(e.isDefaultPrevented()||!i)return;var o=this.getTipElement(),r=m.getUID(this.constructor.NAME);o.setAttribute("id",r),this.element.setAttribute("aria-describedby",r),this.setContent(),this.config.animation&&p(o).addClass(Un);var s="function"==typeof this.config.placement?this.config.placement.call(this,o,this.element):this.config.placement,a=this._getAttachment(s);this.addAttachmentClass(a);var l=this._getContainer();p(o).data(this.constructor.DATA_KEY,this),p.contains(this.element.ownerDocument.documentElement,this.tip)||p(o).appendTo(l),p(this.element).trigger(this.constructor.Event.INSERTED),this._popper=new St(this.element,o,this._getPopperConfig(a)),p(o).addClass(Bn),"ontouchstart"in document.documentElement&&p(document.body).children().on("mouseover",null,p.noop);var c=function(){t.config.animation&&t._fixTransition();var e=t._hoverState;t._hoverState=null,p(t.element).trigger(t.constructor.Event.SHOWN),e===Mn&&t._leave(null,t)};if(p(this.tip).hasClass(Un)){var h=m.getTransitionDurationFromElement(this.tip);p(this.tip).one(m.TRANSITION_END,c).emulateTransitionEnd(h)}else c()}},e.hide=function(e){function t(){n._hoverState!==Fn&&i.parentNode&&i.parentNode.removeChild(i),n._cleanTipClass(),n.element.removeAttribute("aria-describedby"),p(n.element).trigger(n.constructor.Event.HIDDEN),null!==n._popper&&n._popper.destroy(),e&&e()}var n=this,i=this.getTipElement(),o=p.Event(this.constructor.Event.HIDE);if(p(this.element).trigger(o),!o.isDefaultPrevented()){if(p(i).removeClass(Bn),"ontouchstart"in document.documentElement&&p(document.body).children().off("mouseover",null,p.noop),this._activeTrigger[Yn]=!1,this._activeTrigger[Vn]=!1,this._activeTrigger[Qn]=!1,p(this.tip).hasClass(Un)){var r=m.getTransitionDurationFromElement(i);p(i).one(m.TRANSITION_END,t).emulateTransitionEnd(r)}else t();this._hoverState=""}},e.update=function(){null!==this._popper&&this._popper.scheduleUpdate()},e.isWithContent=function(){return Boolean(this.getTitle())},e.addAttachmentClass=function(e){p(this.getTipElement()).addClass(Ln+"-"+e)},e.getTipElement=function(){return this.tip=this.tip||p(this.config.template)[0],this.tip},e.setContent=function(){var e=this.getTipElement();this.setElementContent(p(e.querySelectorAll(qn)),this.getTitle()),p(e).removeClass(Un+" "+Bn)},e.setElementContent=function(e,t){"object"!=typeof t||!t.nodeType&&!t.jquery?this.config.html?(this.config.sanitize&&(t=In(t,this.config.whiteList,this.config.sanitizeFn)),e.html(t)):e.text(t):this.config.html?p(t).parent().is(e)||e.empty().append(t):e.text(p(t).text())},e.getTitle=function(){var e=this.element.getAttribute("data-original-title");return e=e||("function"==typeof this.config.title?this.config.title.call(this.element):this.config.title)},e._getPopperConfig=function(e){var t=this;return l({},{placement:e,modifiers:{offset:this._getOffset(),flip:{behavior:this.config.fallbackPlacement},arrow:{element:Kn},preventOverflow:{boundariesElement:this.config.boundary}},onCreate:function(e){e.originalPlacement!==e.placement&&t._handlePopperPlacementChange(e)},onUpdate:function(e){return t._handlePopperPlacementChange(e)}},{},this.config.popperConfig)},e._getOffset=function(){var t=this,e={};return"function"==typeof this.config.offset?e.fn=function(e){return e.offsets=l({},e.offsets,{},t.config.offset(e.offsets,t.element)||{}),e}:e.offset=this.config.offset,e},e._getContainer=function(){return!1===this.config.container?document.body:m.isElement(this.config.container)?p(this.config.container):p(document).find(this.config.container)},e._getAttachment=function(e){return Hn[e.toUpperCase()]},e._setListeners=function(){var i=this;this.config.trigger.split(" ").forEach(function(e){if("click"===e)p(i.element).on(i.constructor.Event.CLICK,i.config.selector,function(e){return i.toggle(e)});else if(e!==zn){var t=e===Qn?i.constructor.Event.MOUSEENTER:i.constructor.Event.FOCUSIN,n=e===Qn?i.constructor.Event.MOUSELEAVE:i.constructor.Event.FOCUSOUT;p(i.element).on(t,i.config.selector,function(e){return i._enter(e)}).on(n,i.config.selector,function(e){return i._leave(e)})}}),this._hideModalHandler=function(){i.element&&i.hide()},p(this.element).closest(".modal").on("hide.bs.modal",this._hideModalHandler),this.config.selector?this.config=l({},this.config,{trigger:"manual",selector:""}):this._fixTitle()},e._fixTitle=function(){var e=typeof this.element.getAttribute("data-original-title");!this.element.getAttribute("title")&&"string"==e||(this.element.setAttribute("data-original-title",this.element.getAttribute("title")||""),this.element.setAttribute("title",""))},e._enter=function(e,t){var n=this.constructor.DATA_KEY;(t=t||p(e.currentTarget).data(n))||(t=new this.constructor(e.currentTarget,this._getDelegateConfig()),p(e.currentTarget).data(n,t)),e&&(t._activeTrigger["focusin"===e.type?Vn:Qn]=!0),p(t.getTipElement()).hasClass(Bn)||t._hoverState===Fn?t._hoverState=Fn:(clearTimeout(t._timeout),t._hoverState=Fn,t.config.delay&&t.config.delay.show?t._timeout=setTimeout(function(){t._hoverState===Fn&&t.show()},t.config.delay.show):t.show())},e._leave=function(e,t){var n=this.constructor.DATA_KEY;(t=t||p(e.currentTarget).data(n))||(t=new this.constructor(e.currentTarget,this._getDelegateConfig()),p(e.currentTarget).data(n,t)),e&&(t._activeTrigger["focusout"===e.type?Vn:Qn]=!1),t._isWithActiveTrigger()||(clearTimeout(t._timeout),t._hoverState=Mn,t.config.delay&&t.config.delay.hide?t._timeout=setTimeout(function(){t._hoverState===Mn&&t.hide()},t.config.delay.hide):t.hide())},e._isWithActiveTrigger=function(){for(var e in this._activeTrigger)if(this._activeTrigger[e])return!0;return!1},e._getConfig=function(e){var t=p(this.element).data();return Object.keys(t).forEach(function(e){-1!==xn.indexOf(e)&&delete t[e]}),"number"==typeof(e=l({},this.constructor.Default,{},t,{},"object"==typeof e&&e?e:{})).delay&&(e.delay={show:e.delay,hide:e.delay}),"number"==typeof e.title&&(e.title=e.title.toString()),"number"==typeof e.content&&(e.content=e.content.toString()),m.typeCheckConfig(An,e,this.constructor.DefaultType),e.sanitize&&(e.template=In(e.template,e.whiteList,e.sanitizeFn)),e},e._getDelegateConfig=function(){var e={};if(this.config)for(var t in this.config)this.constructor.Default[t]!==this.config[t]&&(e[t]=this.config[t]);return e},e._cleanTipClass=function(){var e=p(this.getTipElement()),t=e.attr("class").match(Pn);null!==t&&t.length&&e.removeClass(t.join(""))},e._handlePopperPlacementChange=function(e){var t=e.instance;this.tip=t.popper,this._cleanTipClass(),this.addAttachmentClass(this._getAttachment(e.placement))},e._fixTransition=function(){var e=this.getTipElement(),t=this.config.animation;null===e.getAttribute("x-placement")&&(p(e).removeClass(Un),this.config.animation=!1,this.hide(),this.show(),this.config.animation=t)},i._jQueryInterface=function(n){return this.each(function(){var e=p(this).data(On),t="object"==typeof n&&n;if((e||!/dispose|hide/.test(n))&&(e||(e=new i(this,t),p(this).data(On,e)),"string"==typeof n)){if("undefined"==typeof e[n])throw new TypeError('No method named "'+n+'"');e[n]()}})},s(i,null,[{key:"VERSION",get:function(){return"4.4.1"}},{key:"Default",get:function(){return Rn}},{key:"NAME",get:function(){return An}},{key:"DATA_KEY",get:function(){return On}},{key:"Event",get:function(){return Wn}},{key:"EVENT_KEY",get:function(){return Nn}},{key:"DefaultType",get:function(){return jn}}]),i}();p.fn[An]=Xn._jQueryInterface,p.fn[An].Constructor=Xn,p.fn[An].noConflict=function(){return p.fn[An]=kn,Xn._jQueryInterface};var Gn="popover",$n="bs.popover",Jn="."+$n,Zn=p.fn[Gn],ei="bs-popover",ti=new RegExp("(^|\\s)"+ei+"\\S+","g"),ni=l({},Xn.Default,{placement:"right",trigger:"click",content:"",template:'<div class="popover" role="tooltip"><div class="arrow"></div><h3 class="popover-header"></h3><div class="popover-body"></div></div>'}),ii=l({},Xn.DefaultType,{content:"(string|element|function)"}),oi="fade",ri="show",si=".popover-header",ai=".popover-body",li={HIDE:"hide"+Jn,HIDDEN:"hidden"+Jn,SHOW:"show"+Jn,SHOWN:"shown"+Jn,INSERTED:"inserted"+Jn,CLICK:"click"+Jn,FOCUSIN:"focusin"+Jn,FOCUSOUT:"focusout"+Jn,MOUSEENTER:"mouseenter"+Jn,MOUSELEAVE:"mouseleave"+Jn},ci=function(e){function i(){return e.apply(this,arguments)||this}!function(e,t){e.prototype=Object.create(t.prototype),(e.prototype.constructor=e).__proto__=t}(i,e);var t=i.prototype;return t.isWithContent=function(){return this.getTitle()||this._getContent()},t.addAttachmentClass=function(e){p(this.getTipElement()).addClass(ei+"-"+e)},t.getTipElement=function(){return this.tip=this.tip||p(this.config.template)[0],this.tip},t.setContent=function(){var e=p(this.getTipElement());this.setElementContent(e.find(si),this.getTitle());var t=this._getContent();"function"==typeof t&&(t=t.call(this.element)),this.setElementContent(e.find(ai),t),e.removeClass(oi+" "+ri)},t._getContent=function(){return this.element.getAttribute("data-content")||this.config.content},t._cleanTipClass=function(){var e=p(this.getTipElement()),t=e.attr("class").match(ti);null!==t&&0<t.length&&e.removeClass(t.join(""))},i._jQueryInterface=function(n){return this.each(function(){var e=p(this).data($n),t="object"==typeof n?n:null;if((e||!/dispose|hide/.test(n))&&(e||(e=new i(this,t),p(this).data($n,e)),"string"==typeof n)){if("undefined"==typeof e[n])throw new TypeError('No method named "'+n+'"');e[n]()}})},s(i,null,[{key:"VERSION",get:function(){return"4.4.1"}},{key:"Default",get:function(){return ni}},{key:"NAME",get:function(){return Gn}},{key:"DATA_KEY",get:function(){return $n}},{key:"Event",get:function(){return li}},{key:"EVENT_KEY",get:function(){return Jn}},{key:"DefaultType",get:function(){return ii}}]),i}(Xn);p.fn[Gn]=ci._jQueryInterface,p.fn[Gn].Constructor=ci,p.fn[Gn].noConflict=function(){return p.fn[Gn]=Zn,ci._jQueryInterface};var hi="scrollspy",ui="bs.scrollspy",fi="."+ui,di=p.fn[hi],pi={offset:10,method:"auto",target:""},mi={offset:"number",method:"string",target:"(string|element)"},gi={ACTIVATE:"activate"+fi,SCROLL:"scroll"+fi,LOAD_DATA_API:"load"+fi+".data-api"},_i="dropdown-item",vi="active",yi='[data-spy="scroll"]',Ei=".nav, .list-group",bi=".nav-link",wi=".nav-item",Ti=".list-group-item",Ci=".dropdown",Si=".dropdown-item",Di=".dropdown-toggle",Ii="offset",Ai="position",Oi=function(){function n(e,t){var n=this;this._element=e,this._scrollElement="BODY"===e.tagName?window:e,this._config=this._getConfig(t),this._selector=this._config.target+" "+bi+","+this._config.target+" "+Ti+","+this._config.target+" "+Si,this._offsets=[],this._targets=[],this._activeTarget=null,this._scrollHeight=0,p(this._scrollElement).on(gi.SCROLL,function(e){return n._process(e)}),this.refresh(),this._process()}var e=n.prototype;return e.refresh=function(){var t=this,e=this._scrollElement===this._scrollElement.window?Ii:Ai,o="auto"===this._config.method?e:this._config.method,r=o===Ai?this._getScrollTop():0;this._offsets=[],this._targets=[],this._scrollHeight=this._getScrollHeight(),[].slice.call(document.querySelectorAll(this._selector)).map(function(e){var t,n=m.getSelectorFromElement(e);if(n&&(t=document.querySelector(n)),t){var i=t.getBoundingClientRect();if(i.width||i.height)return[p(t)[o]().top+r,n]}return null}).filter(function(e){return e}).sort(function(e,t){return e[0]-t[0]}).forEach(function(e){t._offsets.push(e[0]),t._targets.push(e[1])})},e.dispose=function(){p.removeData(this._element,ui),p(this._scrollElement).off(fi),this._element=null,this._scrollElement=null,this._config=null,this._selector=null,this._offsets=null,this._targets=null,this._activeTarget=null,this._scrollHeight=null},e._getConfig=function(e){if("string"!=typeof(e=l({},pi,{},"object"==typeof e&&e?e:{})).target){var t=p(e.target).attr("id");t||(t=m.getUID(hi),p(e.target).attr("id",t)),e.target="#"+t}return m.typeCheckConfig(hi,e,mi),e},e._getScrollTop=function(){return this._scrollElement===window?this._scrollElement.pageYOffset:this._scrollElement.scrollTop},e._getScrollHeight=function(){return this._scrollElement.scrollHeight||Math.max(document.body.scrollHeight,document.documentElement.scrollHeight)},e._getOffsetHeight=function(){return this._scrollElement===window?window.innerHeight:this._scrollElement.getBoundingClientRect().height},e._process=function(){var e=this._getScrollTop()+this._config.offset,t=this._getScrollHeight(),n=this._config.offset+t-this._getOffsetHeight();if(this._scrollHeight!==t&&this.refresh(),n<=e){var i=this._targets[this._targets.length-1];this._activeTarget!==i&&this._activate(i)}else{if(this._activeTarget&&e<this._offsets[0]&&0<this._offsets[0])return this._activeTarget=null,void this._clear();for(var o=this._offsets.length;o--;){this._activeTarget!==this._targets[o]&&e>=this._offsets[o]&&("undefined"==typeof this._offsets[o+1]||e<this._offsets[o+1])&&this._activate(this._targets[o])}}},e._activate=function(t){this._activeTarget=t,this._clear();var e=this._selector.split(",").map(function(e){return e+'[data-target="'+t+'"],'+e+'[href="'+t+'"]'}),n=p([].slice.call(document.querySelectorAll(e.join(","))));n.hasClass(_i)?(n.closest(Ci).find(Di).addClass(vi),n.addClass(vi)):(n.addClass(vi),n.parents(Ei).prev(bi+", "+Ti).addClass(vi),n.parents(Ei).prev(wi).children(bi).addClass(vi)),p(this._scrollElement).trigger(gi.ACTIVATE,{relatedTarget:t})},e._clear=function(){[].slice.call(document.querySelectorAll(this._selector)).filter(function(e){return e.classList.contains(vi)}).forEach(function(e){return e.classList.remove(vi)})},n._jQueryInterface=function(t){return this.each(function(){var e=p(this).data(ui);if(e||(e=new n(this,"object"==typeof t&&t),p(this).data(ui,e)),"string"==typeof t){if("undefined"==typeof e[t])throw new TypeError('No method named "'+t+'"');e[t]()}})},s(n,null,[{key:"VERSION",get:function(){return"4.4.1"}},{key:"Default",get:function(){return pi}}]),n}();p(window).on(gi.LOAD_DATA_API,function(){for(var e=[].slice.call(document.querySelectorAll(yi)),t=e.length;t--;){var n=p(e[t]);Oi._jQueryInterface.call(n,n.data())}}),p.fn[hi]=Oi._jQueryInterface,p.fn[hi].Constructor=Oi,p.fn[hi].noConflict=function(){return p.fn[hi]=di,Oi._jQueryInterface};var Ni="bs.tab",ki="."+Ni,Li=p.fn.tab,Pi={HIDE:"hide"+ki,HIDDEN:"hidden"+ki,SHOW:"show"+ki,SHOWN:"shown"+ki,CLICK_DATA_API:"click"+ki+".data-api"},xi="dropdown-menu",ji="active",Hi="disabled",Ri="fade",Fi="show",Mi=".dropdown",Wi=".nav, .list-group",Ui=".active",Bi="> li > .active",qi='[data-toggle="tab"], [data-toggle="pill"], [data-toggle="list"]',Ki=".dropdown-toggle",Qi="> .dropdown-menu .active",Vi=function(){function i(e){this._element=e}var e=i.prototype;return e.show=function(){var n=this;if(!(this._element.parentNode&&this._element.parentNode.nodeType===Node.ELEMENT_NODE&&p(this._element).hasClass(ji)||p(this._element).hasClass(Hi))){var e,i,t=p(this._element).closest(Wi)[0],o=m.getSelectorFromElement(this._element);if(t){var r="UL"===t.nodeName||"OL"===t.nodeName?Bi:Ui;i=(i=p.makeArray(p(t).find(r)))[i.length-1]}var s=p.Event(Pi.HIDE,{relatedTarget:this._element}),a=p.Event(Pi.SHOW,{relatedTarget:i});if(i&&p(i).trigger(s),p(this._element).trigger(a),!a.isDefaultPrevented()&&!s.isDefaultPrevented()){o&&(e=document.querySelector(o)),this._activate(this._element,t);var l=function(){var e=p.Event(Pi.HIDDEN,{relatedTarget:n._element}),t=p.Event(Pi.SHOWN,{relatedTarget:i});p(i).trigger(e),p(n._element).trigger(t)};e?this._activate(e,e.parentNode,l):l()}}},e.dispose=function(){p.removeData(this._element,Ni),this._element=null},e._activate=function(e,t,n){function i(){return o._transitionComplete(e,r,n)}var o=this,r=(!t||"UL"!==t.nodeName&&"OL"!==t.nodeName?p(t).children(Ui):p(t).find(Bi))[0],s=n&&r&&p(r).hasClass(Ri);if(r&&s){var a=m.getTransitionDurationFromElement(r);p(r).removeClass(Fi).one(m.TRANSITION_END,i).emulateTransitionEnd(a)}else i()},e._transitionComplete=function(e,t,n){if(t){p(t).removeClass(ji);var i=p(t.parentNode).find(Qi)[0];i&&p(i).removeClass(ji),"tab"===t.getAttribute("role")&&t.setAttribute("aria-selected",!1)}if(p(e).addClass(ji),"tab"===e.getAttribute("role")&&e.setAttribute("aria-selected",!0),m.reflow(e),e.classList.contains(Ri)&&e.classList.add(Fi),e.parentNode&&p(e.parentNode).hasClass(xi)){var o=p(e).closest(Mi)[0];if(o){var r=[].slice.call(o.querySelectorAll(Ki));p(r).addClass(ji)}e.setAttribute("aria-expanded",!0)}n&&n()},i._jQueryInterface=function(n){return this.each(function(){var e=p(this),t=e.data(Ni);if(t||(t=new i(this),e.data(Ni,t)),"string"==typeof n){if("undefined"==typeof t[n])throw new TypeError('No method named "'+n+'"');t[n]()}})},s(i,null,[{key:"VERSION",get:function(){return"4.4.1"}}]),i}();p(document).on(Pi.CLICK_DATA_API,qi,function(e){e.preventDefault(),Vi._jQueryInterface.call(p(this),"show")}),p.fn.tab=Vi._jQueryInterface,p.fn.tab.Constructor=Vi,p.fn.tab.noConflict=function(){return p.fn.tab=Li,Vi._jQueryInterface};var Yi="toast",zi="bs.toast",Xi="."+zi,Gi=p.fn[Yi],$i={CLICK_DISMISS:"click.dismiss"+Xi,HIDE:"hide"+Xi,HIDDEN:"hidden"+Xi,SHOW:"show"+Xi,SHOWN:"shown"+Xi},Ji="fade",Zi="hide",eo="show",to="showing",no={animation:"boolean",autohide:"boolean",delay:"number"},io={animation:!0,autohide:!0,delay:500},oo='[data-dismiss="toast"]',ro=function(){function i(e,t){this._element=e,this._config=this._getConfig(t),this._timeout=null,this._setListeners()}var e=i.prototype;return e.show=function(){var e=this,t=p.Event($i.SHOW);if(p(this._element).trigger(t),!t.isDefaultPrevented()){this._config.animation&&this._element.classList.add(Ji);var n=function(){e._element.classList.remove(to),e._element.classList.add(eo),p(e._element).trigger($i.SHOWN),e._config.autohide&&(e._timeout=setTimeout(function(){e.hide()},e._config.delay))};if(this._element.classList.remove(Zi),m.reflow(this._element),this._element.classList.add(to),this._config.animation){var i=m.getTransitionDurationFromElement(this._element);p(this._element).one(m.TRANSITION_END,n).emulateTransitionEnd(i)}else n()}},e.hide=function(){if(this._element.classList.contains(eo)){var e=p.Event($i.HIDE);p(this._element).trigger(e),e.isDefaultPrevented()||this._close()}},e.dispose=function(){clearTimeout(this._timeout),this._timeout=null,this._element.classList.contains(eo)&&this._element.classList.remove(eo),p(this._element).off($i.CLICK_DISMISS),p.removeData(this._element,zi),this._element=null,this._config=null},e._getConfig=function(e){return e=l({},io,{},p(this._element).data(),{},"object"==typeof e&&e?e:{}),m.typeCheckConfig(Yi,e,this.constructor.DefaultType),e},e._setListeners=function(){var e=this;p(this._element).on($i.CLICK_DISMISS,oo,function(){return e.hide()})},e._close=function(){function e(){t._element.classList.add(Zi),p(t._element).trigger($i.HIDDEN)}var t=this;if(this._element.classList.remove(eo),this._config.animation){var n=m.getTransitionDurationFromElement(this._element);p(this._element).one(m.TRANSITION_END,e).emulateTransitionEnd(n)}else e()},i._jQueryInterface=function(n){return this.each(function(){var e=p(this),t=e.data(zi);if(t||(t=new i(this,"object"==typeof n&&n),e.data(zi,t)),"string"==typeof n){if("undefined"==typeof t[n])throw new TypeError('No method named "'+n+'"');t[n](this)}})},s(i,null,[{key:"VERSION",get:function(){return"4.4.1"}},{key:"DefaultType",get:function(){return no}},{key:"Default",get:function(){return io}}]),i}();p.fn[Yi]=ro._jQueryInterface,p.fn[Yi].Constructor=ro,p.fn[Yi].noConflict=function(){return p.fn[Yi]=Gi,ro._jQueryInterface},e.Alert=_,e.Button=x,e.Carousel=he,e.Collapse=De,e.Dropdown=en,e.Modal=wn,e.Popover=ci,e.Scrollspy=Oi,e.Tab=Vi,e.Toast=ro,e.Tooltip=Xn,e.Util=m,Object.defineProperty(e,"__esModule",{value:!0})});

/*
 *  Bootstrap TouchSpin - v4.2.5
 *  A mobile and touch friendly input spinner component for Bootstrap 3 & 4.
 *  http://www.virtuosoft.eu/code/bootstrap-touchspin/
 *
 *  Made by István Ujj-Mészáros
 *  Under Apache License v2.0 License
 */
(function(factory) {
  if (typeof define === 'function' && define.amd) {
    define(['jquery'], factory);
  } else if (typeof module === 'object' && module.exports) {
    module.exports = function(root, jQuery) {
      if (jQuery === undefined) {
        if (typeof window !== 'undefined') {
          jQuery = require('jquery');
        }
        else {
          jQuery = require('jquery')(root);
        }
      }
      factory(jQuery);
      return jQuery;
    };
  } else {
    factory(jQuery);
  }
}(function($) {
  'use strict';

  var _currentSpinnerId = 0;

  $.fn.TouchSpin = function(options) {

    var defaults = {
      min: 0, // If null, there is no minimum enforced
      max: 100, // If null, there is no maximum enforced
      initval: '',
      replacementval: '',
      step: 1,
      decimals: 0,
      stepinterval: 100,
      forcestepdivisibility: 'round', // none | floor | round | ceil
      stepintervaldelay: 500,
      verticalbuttons: false,
      verticalup: '+',
      verticaldown: '-',
      verticalupclass: '',
      verticaldownclass: '',
      prefix: '',
      postfix: '',
      prefix_extraclass: '',
      postfix_extraclass: '',
      booster: true,
      boostat: 10,
      maxboostedstep: false,
      mousewheel: true,
      buttondown_class: 'btn btn-primary',
      buttonup_class: 'btn btn-primary',
      buttondown_txt: '-',
      buttonup_txt: '+',
      callback_before_calculation: function(value) {
        return value;
      },
      callback_after_calculation: function(value) {
        return value;
      }
    };

    var attributeMap = {
      min: 'min',
      max: 'max',
      initval: 'init-val',
      replacementval: 'replacement-val',
      step: 'step',
      decimals: 'decimals',
      stepinterval: 'step-interval',
      verticalbuttons: 'vertical-buttons',
      verticalupclass: 'vertical-up-class',
      verticaldownclass: 'vertical-down-class',
      forcestepdivisibility: 'force-step-divisibility',
      stepintervaldelay: 'step-interval-delay',
      prefix: 'prefix',
      postfix: 'postfix',
      prefix_extraclass: 'prefix-extra-class',
      postfix_extraclass: 'postfix-extra-class',
      booster: 'booster',
      boostat: 'boostat',
      maxboostedstep: 'max-boosted-step',
      mousewheel: 'mouse-wheel',
      buttondown_class: 'button-down-class',
      buttonup_class: 'button-up-class',
      buttondown_txt: 'button-down-txt',
      buttonup_txt: 'button-up-txt'
    };

    return this.each(function() {

      var settings,
        originalinput = $(this),
        originalinput_data = originalinput.data(),
        _detached_prefix,
        _detached_postfix,
        container,
        elements,
        value,
        downSpinTimer,
        upSpinTimer,
        downDelayTimeout,
        upDelayTimeout,
        spincount = 0,
        spinning = false;

      init();

      function init() {
        if (originalinput.data('alreadyinitialized')) {
          return;
        }

        originalinput.data('alreadyinitialized', true);
        _currentSpinnerId += 1;
        originalinput.data('spinnerid', _currentSpinnerId);

        if (!originalinput.is('input')) {
          console.log('Must be an input.');
          return;
        }

        _initSettings();
        _setInitval();
        _checkValue();
        _buildHtml();
        _initElements();
        _hideEmptyPrefixPostfix();
        _bindEvents();
        _bindEventsInterface();
      }

      function _setInitval() {
        if (settings.initval !== '' && originalinput.val() === '') {
          originalinput.val(settings.initval);
        }
      }

      function changeSettings(newsettings) {
        _updateSettings(newsettings);
        _checkValue();

        var value = elements.input.val();

        if (value !== '') {
          value = Number(settings.callback_before_calculation(elements.input.val()));
          elements.input.val(settings.callback_after_calculation(Number(value).toFixed(settings.decimals)));
        }
      }

      function _initSettings() {
        settings = $.extend({}, defaults, originalinput_data, _parseAttributes(), options);
      }

      function _parseAttributes() {
        var data = {};
        $.each(attributeMap, function(key, value) {
          var attrName = 'bts-' + value + '';
          if (originalinput.is('[data-' + attrName + ']')) {
            data[key] = originalinput.data(attrName);
          }
        });
        return data;
      }

      function _destroy() {
        var $parent = originalinput.parent();

        stopSpin();

        originalinput.off('.touchspin');

        if ($parent.hasClass('bootstrap-touchspin-injected')) {
          originalinput.siblings().remove();
          originalinput.unwrap();
        }
        else {
          $('.bootstrap-touchspin-injected', $parent).remove();
          $parent.removeClass('bootstrap-touchspin');
        }

        originalinput.data('alreadyinitialized', false);
      }

      function _updateSettings(newsettings) {
        settings = $.extend({}, settings, newsettings);

        // Update postfix and prefix texts if those settings were changed.
        if (newsettings.postfix) {
          var $postfix = originalinput.parent().find('.bootstrap-touchspin-postfix');

          if ($postfix.length === 0) {
            _detached_postfix.insertAfter(originalinput);
          }

          originalinput.parent().find('.bootstrap-touchspin-postfix .input-group-text').text(newsettings.postfix);
        }

        if (newsettings.prefix) {
          var $prefix = originalinput.parent().find('.bootstrap-touchspin-prefix');

          if ($prefix.length === 0) {
            _detached_prefix.insertBefore(originalinput);
          }

          originalinput.parent().find('.bootstrap-touchspin-prefix .input-group-text').text(newsettings.prefix);
        }

        _hideEmptyPrefixPostfix();
      }

      function _buildHtml() {
        var initval = originalinput.val(),
          parentelement = originalinput.parent();

        if (initval !== '') {
          initval = settings.callback_after_calculation(Number(initval).toFixed(settings.decimals));
        }

        originalinput.data('initvalue', initval).val(initval);
        originalinput.addClass('form-control');

        if (parentelement.hasClass('input-group')) {
          _advanceInputGroup(parentelement);
        }
        else {
          _buildInputGroup();
        }
      }

      function _advanceInputGroup(parentelement) {
        parentelement.addClass('bootstrap-touchspin');

        var prev = originalinput.prev(),
          next = originalinput.next();

        var downhtml,
          uphtml,
          prefixhtml = '<span class="input-group-prepend bootstrap-touchspin-prefix input-group-prepend bootstrap-touchspin-injected"><span class="input-group-text">' + settings.prefix + '</span></span>',
          postfixhtml = '<span class="input-group-append bootstrap-touchspin-postfix input-group-append bootstrap-touchspin-injected"><span class="input-group-text">' + settings.postfix + '</span></span>';

        if (prev.hasClass('input-group-btn') || prev.hasClass('input-group-prepend')) {
          downhtml = '<button class="' + settings.buttondown_class + ' bootstrap-touchspin-down bootstrap-touchspin-injected" type="button">' + settings.buttondown_txt + '</button>';
          prev.append(downhtml);
        }
        else {
          downhtml = '<span class="input-group-btn input-group-prepend bootstrap-touchspin-injected"><button class="' + settings.buttondown_class + ' bootstrap-touchspin-down" type="button">' + settings.buttondown_txt + '</button></span>';
          $(downhtml).insertBefore(originalinput);
        }

        if (next.hasClass('input-group-btn') || next.hasClass('input-group-append')) {
          uphtml = '<button class="' + settings.buttonup_class + ' bootstrap-touchspin-up bootstrap-touchspin-injected" type="button">' + settings.buttonup_txt + '</button>';
          next.prepend(uphtml);
        }
        else {
          uphtml = '<span class="input-group-btn input-group-append bootstrap-touchspin-injected"><button class="' + settings.buttonup_class + ' bootstrap-touchspin-up" type="button">' + settings.buttonup_txt + '</button></span>';
          $(uphtml).insertAfter(originalinput);
        }

        $(prefixhtml).insertBefore(originalinput);
        $(postfixhtml).insertAfter(originalinput);

        container = parentelement;
      }

      function _buildInputGroup() {
        var html;

        var inputGroupSize = '';
        if (originalinput.hasClass('input-sm')) {
          inputGroupSize = 'input-group-sm';
        }

        if (originalinput.hasClass('input-lg')) {
          inputGroupSize = 'input-group-lg';
        }

        if (settings.verticalbuttons) {
          html = '<div class="input-group ' + inputGroupSize + ' bootstrap-touchspin bootstrap-touchspin-injected"><span class="input-group-prepend bootstrap-touchspin-prefix"><span class="input-group-text">' + settings.prefix + '</span></span><span class="bootstrap-touchspin-postfix input-group-append"><span class="input-group-text">' + settings.postfix + '</span></span><span class="input-group-btn-vertical"><button class="' + settings.buttondown_class + ' bootstrap-touchspin-up ' + settings.verticalupclass + '" type="button">' + settings.verticalup + '</button><button class="' + settings.buttonup_class + ' bootstrap-touchspin-down ' + settings.verticaldownclass + '" type="button">' + settings.verticaldown + '</button></span></div>';
        }
        else {
          html = '<div class="input-group bootstrap-touchspin bootstrap-touchspin-injected"><span class="input-group-btn input-group-prepend"><button class="' + settings.buttondown_class + ' bootstrap-touchspin-down" type="button">' + settings.buttondown_txt + '</button></span><span class="bootstrap-touchspin-prefix input-group-prepend"><span class="input-group-text">' + settings.prefix + '</span></span><span class="bootstrap-touchspin-postfix input-group-append"><span class="input-group-text">' + settings.postfix + '</span></span><span class="input-group-btn input-group-append"><button class="' + settings.buttonup_class + ' bootstrap-touchspin-up" type="button">' + settings.buttonup_txt + '</button></span></div>';
        }

        container = $(html).insertBefore(originalinput);

        $('.bootstrap-touchspin-prefix', container).after(originalinput);

        if (originalinput.hasClass('input-sm')) {
          container.addClass('input-group-sm');
        }
        else if (originalinput.hasClass('input-lg')) {
          container.addClass('input-group-lg');
        }
      }

      function _initElements() {
        elements = {
          down: $('.bootstrap-touchspin-down', container),
          up: $('.bootstrap-touchspin-up', container),
          input: $('input', container),
          prefix: $('.bootstrap-touchspin-prefix', container).addClass(settings.prefix_extraclass),
          postfix: $('.bootstrap-touchspin-postfix', container).addClass(settings.postfix_extraclass)
        };
      }

      function _hideEmptyPrefixPostfix() {
        if (settings.prefix === '') {
          _detached_prefix = elements.prefix.detach();
        }

        if (settings.postfix === '') {
          _detached_postfix = elements.postfix.detach();
        }
      }

      function _bindEvents() {
        originalinput.on('keydown.touchspin', function(ev) {
          var code = ev.keyCode || ev.which;

          if (code === 38) {
            if (spinning !== 'up') {
              upOnce();
              startUpSpin();
            }
            ev.preventDefault();
          }
          else if (code === 40) {
            if (spinning !== 'down') {
              downOnce();
              startDownSpin();
            }
            ev.preventDefault();
          }
        });

        originalinput.on('keyup.touchspin', function(ev) {
          var code = ev.keyCode || ev.which;

          if (code === 38) {
            stopSpin();
          }
          else if (code === 40) {
            stopSpin();
          }
        });

        originalinput.on('blur.touchspin', function() {
          _checkValue();
          originalinput.val(settings.callback_after_calculation(originalinput.val()));
        });

        elements.down.on('keydown', function(ev) {
          var code = ev.keyCode || ev.which;

          if (code === 32 || code === 13) {
            if (spinning !== 'down') {
              downOnce();
              startDownSpin();
            }
            ev.preventDefault();
          }
        });

        elements.down.on('keyup.touchspin', function(ev) {
          var code = ev.keyCode || ev.which;

          if (code === 32 || code === 13) {
            stopSpin();
          }
        });

        elements.up.on('keydown.touchspin', function(ev) {
          var code = ev.keyCode || ev.which;

          if (code === 32 || code === 13) {
            if (spinning !== 'up') {
              upOnce();
              startUpSpin();
            }
            ev.preventDefault();
          }
        });

        elements.up.on('keyup.touchspin', function(ev) {
          var code = ev.keyCode || ev.which;

          if (code === 32 || code === 13) {
            stopSpin();
          }
        });

        elements.down.on('mousedown.touchspin', function(ev) {
          elements.down.off('touchstart.touchspin');  // android 4 workaround

          if (originalinput.is(':disabled')) {
            return;
          }

          downOnce();
          startDownSpin();

          ev.preventDefault();
          ev.stopPropagation();
        });

        elements.down.on('touchstart.touchspin', function(ev) {
          elements.down.off('mousedown.touchspin');  // android 4 workaround

          if (originalinput.is(':disabled')) {
            return;
          }

          downOnce();
          startDownSpin();

          ev.preventDefault();
          ev.stopPropagation();
        });

        elements.up.on('mousedown.touchspin', function(ev) {
          elements.up.off('touchstart.touchspin');  // android 4 workaround

          if (originalinput.is(':disabled')) {
            return;
          }

          upOnce();
          startUpSpin();

          ev.preventDefault();
          ev.stopPropagation();
        });

        elements.up.on('touchstart.touchspin', function(ev) {
          elements.up.off('mousedown.touchspin');  // android 4 workaround

          if (originalinput.is(':disabled')) {
            return;
          }

          upOnce();
          startUpSpin();

          ev.preventDefault();
          ev.stopPropagation();
        });

        elements.up.on('mouseup.touchspin mouseout.touchspin touchleave.touchspin touchend.touchspin touchcancel.touchspin', function(ev) {
          if (!spinning) {
            return;
          }

          ev.stopPropagation();
          stopSpin();
        });

        elements.down.on('mouseup.touchspin mouseout.touchspin touchleave.touchspin touchend.touchspin touchcancel.touchspin', function(ev) {
          if (!spinning) {
            return;
          }

          ev.stopPropagation();
          stopSpin();
        });

        elements.down.on('mousemove.touchspin touchmove.touchspin', function(ev) {
          if (!spinning) {
            return;
          }

          ev.stopPropagation();
          ev.preventDefault();
        });

        elements.up.on('mousemove.touchspin touchmove.touchspin', function(ev) {
          if (!spinning) {
            return;
          }

          ev.stopPropagation();
          ev.preventDefault();
        });

        originalinput.on('mousewheel.touchspin DOMMouseScroll.touchspin', function(ev) {
          if (!settings.mousewheel || !originalinput.is(':focus')) {
            return;
          }

          var delta = ev.originalEvent.wheelDelta || -ev.originalEvent.deltaY || -ev.originalEvent.detail;

          ev.stopPropagation();
          ev.preventDefault();

          if (delta < 0) {
            downOnce();
          }
          else {
            upOnce();
          }
        });
      }

      function _bindEventsInterface() {
        originalinput.on('touchspin.destroy', function() {
          _destroy();
        });

        originalinput.on('touchspin.uponce', function() {
          stopSpin();
          upOnce();
        });

        originalinput.on('touchspin.downonce', function() {
          stopSpin();
          downOnce();
        });

        originalinput.on('touchspin.startupspin', function() {
          startUpSpin();
        });

        originalinput.on('touchspin.startdownspin', function() {
          startDownSpin();
        });

        originalinput.on('touchspin.stopspin', function() {
          stopSpin();
        });

        originalinput.on('touchspin.updatesettings', function(e, newsettings) {
          changeSettings(newsettings);
        });
      }

      function _forcestepdivisibility(value) {
        switch (settings.forcestepdivisibility) {
          case 'round':
            return (Math.round(value / settings.step) * settings.step).toFixed(settings.decimals);
          case 'floor':
            return (Math.floor(value / settings.step) * settings.step).toFixed(settings.decimals);
          case 'ceil':
            return (Math.ceil(value / settings.step) * settings.step).toFixed(settings.decimals);
          default:
            return value;
        }
      }

      function _checkValue() {
        var val, parsedval, returnval;

        val = settings.callback_before_calculation(originalinput.val());

        if (val === '') {
          if (settings.replacementval !== '') {
            originalinput.val(settings.replacementval);
            originalinput.trigger('change');
          }
          return;
        }

        if (settings.decimals > 0 && val === '.') {
          return;
        }

        parsedval = parseFloat(val);

        if (isNaN(parsedval)) {
          if (settings.replacementval !== '') {
            parsedval = settings.replacementval;
          }
          else {
            parsedval = 0;
          }
        }

        returnval = parsedval;

        if (parsedval.toString() !== val) {
          returnval = parsedval;
        }

        if ((settings.min !== null) && (parsedval < settings.min)) {
          returnval = settings.min;
        }

        if ((settings.max !== null) && (parsedval > settings.max)) {
          returnval = settings.max;
        }

        returnval = _forcestepdivisibility(returnval);

        if (Number(val).toString() !== returnval.toString()) {
          originalinput.val(returnval);
          originalinput.trigger('change');
        }
      }

      function _getBoostedStep() {
        if (!settings.booster) {
          return settings.step;
        }
        else {
          var boosted = Math.pow(2, Math.floor(spincount / settings.boostat)) * settings.step;

          if (settings.maxboostedstep) {
            if (boosted > settings.maxboostedstep) {
              boosted = settings.maxboostedstep;
              value = Math.round((value / boosted)) * boosted;
            }
          }

          return Math.max(settings.step, boosted);
        }
      }

      function upOnce() {
        _checkValue();

        value = parseFloat(settings.callback_before_calculation(elements.input.val()));
        if (isNaN(value)) {
          value = 0;
        }

        var initvalue = value,
          boostedstep = _getBoostedStep();

        value = value + boostedstep;

        if ((settings.max !== null) && (value > settings.max)) {
          value = settings.max;
          originalinput.trigger('touchspin.on.max');
          stopSpin();
        }

        elements.input.val(settings.callback_after_calculation(Number(value).toFixed(settings.decimals)));

        if (initvalue !== value) {
          originalinput.trigger('change');
        }
      }

      function downOnce() {
        _checkValue();

        value = parseFloat(settings.callback_before_calculation(elements.input.val()));
        if (isNaN(value)) {
          value = 0;
        }

        var initvalue = value,
          boostedstep = _getBoostedStep();

        value = value - boostedstep;

        if ((settings.min !== null) && (value < settings.min)) {
          value = settings.min;
          originalinput.trigger('touchspin.on.min');
          stopSpin();
        }

        elements.input.val(settings.callback_after_calculation(Number(value).toFixed(settings.decimals)));

        if (initvalue !== value) {
          originalinput.trigger('change');
        }
      }

      function startDownSpin() {
        stopSpin();

        spincount = 0;
        spinning = 'down';

        originalinput.trigger('touchspin.on.startspin');
        originalinput.trigger('touchspin.on.startdownspin');

        downDelayTimeout = setTimeout(function() {
          downSpinTimer = setInterval(function() {
            spincount++;
            downOnce();
          }, settings.stepinterval);
        }, settings.stepintervaldelay);
      }

      function startUpSpin() {
        stopSpin();

        spincount = 0;
        spinning = 'up';

        originalinput.trigger('touchspin.on.startspin');
        originalinput.trigger('touchspin.on.startupspin');

        upDelayTimeout = setTimeout(function() {
          upSpinTimer = setInterval(function() {
            spincount++;
            upOnce();
          }, settings.stepinterval);
        }, settings.stepintervaldelay);
      }

      function stopSpin() {
        clearTimeout(downDelayTimeout);
        clearTimeout(upDelayTimeout);
        clearInterval(downSpinTimer);
        clearInterval(upSpinTimer);

        switch (spinning) {
          case 'up':
            originalinput.trigger('touchspin.on.stopupspin');
            originalinput.trigger('touchspin.on.stopspin');
            break;
          case 'down':
            originalinput.trigger('touchspin.on.stopdownspin');
            originalinput.trigger('touchspin.on.stopspin');
            break;
        }

        spincount = 0;
        spinning = false;
      }

    });

  };

}));
/*!
 * Datepicker for Bootstrap v1.9.0 (https://github.com/uxsolutions/bootstrap-datepicker)
 *
 * Licensed under the Apache License v2.0 (http://www.apache.org/licenses/LICENSE-2.0)
 */

(function(factory){
    if (typeof define === 'function' && define.amd) {
        define(['jquery'], factory);
    } else if (typeof exports === 'object') {
        factory(require('jquery'));
    } else {
        factory(jQuery);
    }
}(function($, undefined){
	function UTCDate(){
		return new Date(Date.UTC.apply(Date, arguments));
	}
	function UTCToday(){
		var today = new Date();
		return UTCDate(today.getFullYear(), today.getMonth(), today.getDate());
	}
	function isUTCEquals(date1, date2) {
		return (
			date1.getUTCFullYear() === date2.getUTCFullYear() &&
			date1.getUTCMonth() === date2.getUTCMonth() &&
			date1.getUTCDate() === date2.getUTCDate()
		);
	}
	function alias(method, deprecationMsg){
		return function(){
			if (deprecationMsg !== undefined) {
				$.fn.datepicker.deprecated(deprecationMsg);
			}

			return this[method].apply(this, arguments);
		};
	}
	function isValidDate(d) {
		return d && !isNaN(d.getTime());
	}

	var DateArray = (function(){
		var extras = {
			get: function(i){
				return this.slice(i)[0];
			},
			contains: function(d){
				// Array.indexOf is not cross-browser;
				// $.inArray doesn't work with Dates
				var val = d && d.valueOf();
				for (var i=0, l=this.length; i < l; i++)
          // Use date arithmetic to allow dates with different times to match
          if (0 <= this[i].valueOf() - val && this[i].valueOf() - val < 1000*60*60*24)
						return i;
				return -1;
			},
			remove: function(i){
				this.splice(i,1);
			},
			replace: function(new_array){
				if (!new_array)
					return;
				if (!$.isArray(new_array))
					new_array = [new_array];
				this.clear();
				this.push.apply(this, new_array);
			},
			clear: function(){
				this.length = 0;
			},
			copy: function(){
				var a = new DateArray();
				a.replace(this);
				return a;
			}
		};

		return function(){
			var a = [];
			a.push.apply(a, arguments);
			$.extend(a, extras);
			return a;
		};
	})();


	// Picker object

	var Datepicker = function(element, options){
		$.data(element, 'datepicker', this);

		this._events = [];
		this._secondaryEvents = [];

		this._process_options(options);

		this.dates = new DateArray();
		this.viewDate = this.o.defaultViewDate;
		this.focusDate = null;

		this.element = $(element);
		this.isInput = this.element.is('input');
		this.inputField = this.isInput ? this.element : this.element.find('input');
		this.component = this.element.hasClass('date') ? this.element.find('.add-on, .input-group-addon, .input-group-append, .input-group-prepend, .btn') : false;
		if (this.component && this.component.length === 0)
			this.component = false;
		this.isInline = !this.component && this.element.is('div');

		this.picker = $(DPGlobal.template);

		// Checking templates and inserting
		if (this._check_template(this.o.templates.leftArrow)) {
			this.picker.find('.prev').html(this.o.templates.leftArrow);
		}

		if (this._check_template(this.o.templates.rightArrow)) {
			this.picker.find('.next').html(this.o.templates.rightArrow);
		}

		this._buildEvents();
		this._attachEvents();

		if (this.isInline){
			this.picker.addClass('datepicker-inline').appendTo(this.element);
		}
		else {
			this.picker.addClass('datepicker-dropdown dropdown-menu');
		}

		if (this.o.rtl){
			this.picker.addClass('datepicker-rtl');
		}

		if (this.o.calendarWeeks) {
			this.picker.find('.datepicker-days .datepicker-switch, thead .datepicker-title, tfoot .today, tfoot .clear')
				.attr('colspan', function(i, val){
					return Number(val) + 1;
				});
		}

		this._process_options({
			startDate: this._o.startDate,
			endDate: this._o.endDate,
			daysOfWeekDisabled: this.o.daysOfWeekDisabled,
			daysOfWeekHighlighted: this.o.daysOfWeekHighlighted,
			datesDisabled: this.o.datesDisabled
		});

		this._allow_update = false;
		this.setViewMode(this.o.startView);
		this._allow_update = true;

		this.fillDow();
		this.fillMonths();

		this.update();

		if (this.isInline){
			this.show();
		}
	};

	Datepicker.prototype = {
		constructor: Datepicker,

		_resolveViewName: function(view){
			$.each(DPGlobal.viewModes, function(i, viewMode){
				if (view === i || $.inArray(view, viewMode.names) !== -1){
					view = i;
					return false;
				}
			});

			return view;
		},

		_resolveDaysOfWeek: function(daysOfWeek){
			if (!$.isArray(daysOfWeek))
				daysOfWeek = daysOfWeek.split(/[,\s]*/);
			return $.map(daysOfWeek, Number);
		},

		_check_template: function(tmp){
			try {
				// If empty
				if (tmp === undefined || tmp === "") {
					return false;
				}
				// If no html, everything ok
				if ((tmp.match(/[<>]/g) || []).length <= 0) {
					return true;
				}
				// Checking if html is fine
				var jDom = $(tmp);
				return jDom.length > 0;
			}
			catch (ex) {
				return false;
			}
		},

		_process_options: function(opts){
			// Store raw options for reference
			this._o = $.extend({}, this._o, opts);
			// Processed options
			var o = this.o = $.extend({}, this._o);

			// Check if "de-DE" style date is available, if not language should
			// fallback to 2 letter code eg "de"
			var lang = o.language;
			if (!dates[lang]){
				lang = lang.split('-')[0];
				if (!dates[lang])
					lang = defaults.language;
			}
			o.language = lang;

			// Retrieve view index from any aliases
			o.startView = this._resolveViewName(o.startView);
			o.minViewMode = this._resolveViewName(o.minViewMode);
			o.maxViewMode = this._resolveViewName(o.maxViewMode);

			// Check view is between min and max
			o.startView = Math.max(this.o.minViewMode, Math.min(this.o.maxViewMode, o.startView));

			// true, false, or Number > 0
			if (o.multidate !== true){
				o.multidate = Number(o.multidate) || false;
				if (o.multidate !== false)
					o.multidate = Math.max(0, o.multidate);
			}
			o.multidateSeparator = String(o.multidateSeparator);

			o.weekStart %= 7;
			o.weekEnd = (o.weekStart + 6) % 7;

			var format = DPGlobal.parseFormat(o.format);
			if (o.startDate !== -Infinity){
				if (!!o.startDate){
					if (o.startDate instanceof Date)
						o.startDate = this._local_to_utc(this._zero_time(o.startDate));
					else
						o.startDate = DPGlobal.parseDate(o.startDate, format, o.language, o.assumeNearbyYear);
				}
				else {
					o.startDate = -Infinity;
				}
			}
			if (o.endDate !== Infinity){
				if (!!o.endDate){
					if (o.endDate instanceof Date)
						o.endDate = this._local_to_utc(this._zero_time(o.endDate));
					else
						o.endDate = DPGlobal.parseDate(o.endDate, format, o.language, o.assumeNearbyYear);
				}
				else {
					o.endDate = Infinity;
				}
			}

			o.daysOfWeekDisabled = this._resolveDaysOfWeek(o.daysOfWeekDisabled||[]);
			o.daysOfWeekHighlighted = this._resolveDaysOfWeek(o.daysOfWeekHighlighted||[]);

			o.datesDisabled = o.datesDisabled||[];
			if (!$.isArray(o.datesDisabled)) {
				o.datesDisabled = o.datesDisabled.split(',');
			}
			o.datesDisabled = $.map(o.datesDisabled, function(d){
				return DPGlobal.parseDate(d, format, o.language, o.assumeNearbyYear);
			});

			var plc = String(o.orientation).toLowerCase().split(/\s+/g),
				_plc = o.orientation.toLowerCase();
			plc = $.grep(plc, function(word){
				return /^auto|left|right|top|bottom$/.test(word);
			});
			o.orientation = {x: 'auto', y: 'auto'};
			if (!_plc || _plc === 'auto')
				; // no action
			else if (plc.length === 1){
				switch (plc[0]){
					case 'top':
					case 'bottom':
						o.orientation.y = plc[0];
						break;
					case 'left':
					case 'right':
						o.orientation.x = plc[0];
						break;
				}
			}
			else {
				_plc = $.grep(plc, function(word){
					return /^left|right$/.test(word);
				});
				o.orientation.x = _plc[0] || 'auto';

				_plc = $.grep(plc, function(word){
					return /^top|bottom$/.test(word);
				});
				o.orientation.y = _plc[0] || 'auto';
			}
			if (o.defaultViewDate instanceof Date || typeof o.defaultViewDate === 'string') {
				o.defaultViewDate = DPGlobal.parseDate(o.defaultViewDate, format, o.language, o.assumeNearbyYear);
			} else if (o.defaultViewDate) {
				var year = o.defaultViewDate.year || new Date().getFullYear();
				var month = o.defaultViewDate.month || 0;
				var day = o.defaultViewDate.day || 1;
				o.defaultViewDate = UTCDate(year, month, day);
			} else {
				o.defaultViewDate = UTCToday();
			}
		},
		_applyEvents: function(evs){
			for (var i=0, el, ch, ev; i < evs.length; i++){
				el = evs[i][0];
				if (evs[i].length === 2){
					ch = undefined;
					ev = evs[i][1];
				} else if (evs[i].length === 3){
					ch = evs[i][1];
					ev = evs[i][2];
				}
				el.on(ev, ch);
			}
		},
		_unapplyEvents: function(evs){
			for (var i=0, el, ev, ch; i < evs.length; i++){
				el = evs[i][0];
				if (evs[i].length === 2){
					ch = undefined;
					ev = evs[i][1];
				} else if (evs[i].length === 3){
					ch = evs[i][1];
					ev = evs[i][2];
				}
				el.off(ev, ch);
			}
		},
		_buildEvents: function(){
            var events = {
                keyup: $.proxy(function(e){
                    if ($.inArray(e.keyCode, [27, 37, 39, 38, 40, 32, 13, 9]) === -1)
                        this.update();
                }, this),
                keydown: $.proxy(this.keydown, this),
                paste: $.proxy(this.paste, this)
            };

            if (this.o.showOnFocus === true) {
                events.focus = $.proxy(this.show, this);
            }

            if (this.isInput) { // single input
                this._events = [
                    [this.element, events]
                ];
            }
            // component: input + button
            else if (this.component && this.inputField.length) {
                this._events = [
                    // For components that are not readonly, allow keyboard nav
                    [this.inputField, events],
                    [this.component, {
                        click: $.proxy(this.show, this)
                    }]
                ];
            }
			else {
				this._events = [
					[this.element, {
						click: $.proxy(this.show, this),
						keydown: $.proxy(this.keydown, this)
					}]
				];
			}
			this._events.push(
				// Component: listen for blur on element descendants
				[this.element, '*', {
					blur: $.proxy(function(e){
						this._focused_from = e.target;
					}, this)
				}],
				// Input: listen for blur on element
				[this.element, {
					blur: $.proxy(function(e){
						this._focused_from = e.target;
					}, this)
				}]
			);

			if (this.o.immediateUpdates) {
				// Trigger input updates immediately on changed year/month
				this._events.push([this.element, {
					'changeYear changeMonth': $.proxy(function(e){
						this.update(e.date);
					}, this)
				}]);
			}

			this._secondaryEvents = [
				[this.picker, {
					click: $.proxy(this.click, this)
				}],
				[this.picker, '.prev, .next', {
					click: $.proxy(this.navArrowsClick, this)
				}],
				[this.picker, '.day:not(.disabled)', {
					click: $.proxy(this.dayCellClick, this)
				}],
				[$(window), {
					resize: $.proxy(this.place, this)
				}],
				[$(document), {
					'mousedown touchstart': $.proxy(function(e){
						// Clicked outside the datepicker, hide it
						if (!(
							this.element.is(e.target) ||
							this.element.find(e.target).length ||
							this.picker.is(e.target) ||
							this.picker.find(e.target).length ||
							this.isInline
						)){
							this.hide();
						}
					}, this)
				}]
			];
		},
		_attachEvents: function(){
			this._detachEvents();
			this._applyEvents(this._events);
		},
		_detachEvents: function(){
			this._unapplyEvents(this._events);
		},
		_attachSecondaryEvents: function(){
			this._detachSecondaryEvents();
			this._applyEvents(this._secondaryEvents);
		},
		_detachSecondaryEvents: function(){
			this._unapplyEvents(this._secondaryEvents);
		},
		_trigger: function(event, altdate){
			var date = altdate || this.dates.get(-1),
				local_date = this._utc_to_local(date);

			this.element.trigger({
				type: event,
				date: local_date,
				viewMode: this.viewMode,
				dates: $.map(this.dates, this._utc_to_local),
				format: $.proxy(function(ix, format){
					if (arguments.length === 0){
						ix = this.dates.length - 1;
						format = this.o.format;
					} else if (typeof ix === 'string'){
						format = ix;
						ix = this.dates.length - 1;
					}
					format = format || this.o.format;
					var date = this.dates.get(ix);
					return DPGlobal.formatDate(date, format, this.o.language);
				}, this)
			});
		},

		show: function(){
			if (this.inputField.is(':disabled') || (this.inputField.prop('readonly') && this.o.enableOnReadonly === false))
				return;
			if (!this.isInline)
				this.picker.appendTo(this.o.container);
			this.place();
			this.picker.show();
			this._attachSecondaryEvents();
			this._trigger('show');
			if ((window.navigator.msMaxTouchPoints || 'ontouchstart' in document) && this.o.disableTouchKeyboard) {
				$(this.element).blur();
			}
			return this;
		},

		hide: function(){
			if (this.isInline || !this.picker.is(':visible'))
				return this;
			this.focusDate = null;
			this.picker.hide().detach();
			this._detachSecondaryEvents();
			this.setViewMode(this.o.startView);

			if (this.o.forceParse && this.inputField.val())
				this.setValue();
			this._trigger('hide');
			return this;
		},

		destroy: function(){
			this.hide();
			this._detachEvents();
			this._detachSecondaryEvents();
			this.picker.remove();
			delete this.element.data().datepicker;
			if (!this.isInput){
				delete this.element.data().date;
			}
			return this;
		},

		paste: function(e){
			var dateString;
			if (e.originalEvent.clipboardData && e.originalEvent.clipboardData.types
				&& $.inArray('text/plain', e.originalEvent.clipboardData.types) !== -1) {
				dateString = e.originalEvent.clipboardData.getData('text/plain');
			} else if (window.clipboardData) {
				dateString = window.clipboardData.getData('Text');
			} else {
				return;
			}
			this.setDate(dateString);
			this.update();
			e.preventDefault();
		},

		_utc_to_local: function(utc){
			if (!utc) {
				return utc;
			}

			var local = new Date(utc.getTime() + (utc.getTimezoneOffset() * 60000));

			if (local.getTimezoneOffset() !== utc.getTimezoneOffset()) {
				local = new Date(utc.getTime() + (local.getTimezoneOffset() * 60000));
			}

			return local;
		},
		_local_to_utc: function(local){
			return local && new Date(local.getTime() - (local.getTimezoneOffset()*60000));
		},
		_zero_time: function(local){
			return local && new Date(local.getFullYear(), local.getMonth(), local.getDate());
		},
		_zero_utc_time: function(utc){
			return utc && UTCDate(utc.getUTCFullYear(), utc.getUTCMonth(), utc.getUTCDate());
		},

		getDates: function(){
			return $.map(this.dates, this._utc_to_local);
		},

		getUTCDates: function(){
			return $.map(this.dates, function(d){
				return new Date(d);
			});
		},

		getDate: function(){
			return this._utc_to_local(this.getUTCDate());
		},

		getUTCDate: function(){
			var selected_date = this.dates.get(-1);
			if (selected_date !== undefined) {
				return new Date(selected_date);
			} else {
				return null;
			}
		},

		clearDates: function(){
			this.inputField.val('');
			this.update();
			this._trigger('changeDate');

			if (this.o.autoclose) {
				this.hide();
			}
		},

		setDates: function(){
			var args = $.isArray(arguments[0]) ? arguments[0] : arguments;
			this.update.apply(this, args);
			this._trigger('changeDate');
			this.setValue();
			return this;
		},

		setUTCDates: function(){
			var args = $.isArray(arguments[0]) ? arguments[0] : arguments;
			this.setDates.apply(this, $.map(args, this._utc_to_local));
			return this;
		},

		setDate: alias('setDates'),
		setUTCDate: alias('setUTCDates'),
		remove: alias('destroy', 'Method `remove` is deprecated and will be removed in version 2.0. Use `destroy` instead'),

		setValue: function(){
			var formatted = this.getFormattedDate();
			this.inputField.val(formatted);
			return this;
		},

		getFormattedDate: function(format){
			if (format === undefined)
				format = this.o.format;

			var lang = this.o.language;
			return $.map(this.dates, function(d){
				return DPGlobal.formatDate(d, format, lang);
			}).join(this.o.multidateSeparator);
		},

		getStartDate: function(){
			return this.o.startDate;
		},

		setStartDate: function(startDate){
			this._process_options({startDate: startDate});
			this.update();
			this.updateNavArrows();
			return this;
		},

		getEndDate: function(){
			return this.o.endDate;
		},

		setEndDate: function(endDate){
			this._process_options({endDate: endDate});
			this.update();
			this.updateNavArrows();
			return this;
		},

		setDaysOfWeekDisabled: function(daysOfWeekDisabled){
			this._process_options({daysOfWeekDisabled: daysOfWeekDisabled});
			this.update();
			return this;
		},

		setDaysOfWeekHighlighted: function(daysOfWeekHighlighted){
			this._process_options({daysOfWeekHighlighted: daysOfWeekHighlighted});
			this.update();
			return this;
		},

		setDatesDisabled: function(datesDisabled){
			this._process_options({datesDisabled: datesDisabled});
			this.update();
			return this;
		},

		place: function(){
			if (this.isInline)
				return this;
			var calendarWidth = this.picker.outerWidth(),
				calendarHeight = this.picker.outerHeight(),
				visualPadding = 10,
				container = $(this.o.container),
				windowWidth = container.width(),
				scrollTop = this.o.container === 'body' ? $(document).scrollTop() : container.scrollTop(),
				appendOffset = container.offset();

			var parentsZindex = [0];
			this.element.parents().each(function(){
				var itemZIndex = $(this).css('z-index');
				if (itemZIndex !== 'auto' && Number(itemZIndex) !== 0) parentsZindex.push(Number(itemZIndex));
			});
			var zIndex = Math.max.apply(Math, parentsZindex) + this.o.zIndexOffset;
			var offset = this.component ? this.component.parent().offset() : this.element.offset();
			var height = this.component ? this.component.outerHeight(true) : this.element.outerHeight(false);
			var width = this.component ? this.component.outerWidth(true) : this.element.outerWidth(false);
			var left = offset.left - appendOffset.left;
			var top = offset.top - appendOffset.top;

			if (this.o.container !== 'body') {
				top += scrollTop;
			}

			this.picker.removeClass(
				'datepicker-orient-top datepicker-orient-bottom '+
				'datepicker-orient-right datepicker-orient-left'
			);

			if (this.o.orientation.x !== 'auto'){
				this.picker.addClass('datepicker-orient-' + this.o.orientation.x);
				if (this.o.orientation.x === 'right')
					left -= calendarWidth - width;
			}
			// auto x orientation is best-placement: if it crosses a window
			// edge, fudge it sideways
			else {
				if (offset.left < 0) {
					// component is outside the window on the left side. Move it into visible range
					this.picker.addClass('datepicker-orient-left');
					left -= offset.left - visualPadding;
				} else if (left + calendarWidth > windowWidth) {
					// the calendar passes the widow right edge. Align it to component right side
					this.picker.addClass('datepicker-orient-right');
					left += width - calendarWidth;
				} else {
					if (this.o.rtl) {
						// Default to right
						this.picker.addClass('datepicker-orient-right');
					} else {
						// Default to left
						this.picker.addClass('datepicker-orient-left');
					}
				}
			}

			// auto y orientation is best-situation: top or bottom, no fudging,
			// decision based on which shows more of the calendar
			var yorient = this.o.orientation.y,
				top_overflow;
			if (yorient === 'auto'){
				top_overflow = -scrollTop + top - calendarHeight;
				yorient = top_overflow < 0 ? 'bottom' : 'top';
			}

			this.picker.addClass('datepicker-orient-' + yorient);
			if (yorient === 'top')
				top -= calendarHeight + parseInt(this.picker.css('padding-top'));
			else
				top += height;

			if (this.o.rtl) {
				var right = windowWidth - (left + width);
				this.picker.css({
					top: top,
					right: right,
					zIndex: zIndex
				});
			} else {
				this.picker.css({
					top: top,
					left: left,
					zIndex: zIndex
				});
			}
			return this;
		},

		_allow_update: true,
		update: function(){
			if (!this._allow_update)
				return this;

			var oldDates = this.dates.copy(),
				dates = [],
				fromArgs = false;
			if (arguments.length){
				$.each(arguments, $.proxy(function(i, date){
					if (date instanceof Date)
						date = this._local_to_utc(date);
					dates.push(date);
				}, this));
				fromArgs = true;
			} else {
				dates = this.isInput
						? this.element.val()
						: this.element.data('date') || this.inputField.val();
				if (dates && this.o.multidate)
					dates = dates.split(this.o.multidateSeparator);
				else
					dates = [dates];
				delete this.element.data().date;
			}

			dates = $.map(dates, $.proxy(function(date){
				return DPGlobal.parseDate(date, this.o.format, this.o.language, this.o.assumeNearbyYear);
			}, this));
			dates = $.grep(dates, $.proxy(function(date){
				return (
					!this.dateWithinRange(date) ||
					!date
				);
			}, this), true);
			this.dates.replace(dates);

			if (this.o.updateViewDate) {
				if (this.dates.length)
					this.viewDate = new Date(this.dates.get(-1));
				else if (this.viewDate < this.o.startDate)
					this.viewDate = new Date(this.o.startDate);
				else if (this.viewDate > this.o.endDate)
					this.viewDate = new Date(this.o.endDate);
				else
					this.viewDate = this.o.defaultViewDate;
			}

			if (fromArgs){
				// setting date by clicking
				this.setValue();
				this.element.change();
			}
			else if (this.dates.length){
				// setting date by typing
				if (String(oldDates) !== String(this.dates) && fromArgs) {
					this._trigger('changeDate');
					this.element.change();
				}
			}
			if (!this.dates.length && oldDates.length) {
				this._trigger('clearDate');
				this.element.change();
			}

			this.fill();
			return this;
		},

		fillDow: function(){
      if (this.o.showWeekDays) {
			var dowCnt = this.o.weekStart,
				html = '<tr>';
			if (this.o.calendarWeeks){
				html += '<th class="cw">&#160;</th>';
			}
			while (dowCnt < this.o.weekStart + 7){
				html += '<th class="dow';
        if ($.inArray(dowCnt, this.o.daysOfWeekDisabled) !== -1)
          html += ' disabled';
        html += '">'+dates[this.o.language].daysMin[(dowCnt++)%7]+'</th>';
			}
			html += '</tr>';
			this.picker.find('.datepicker-days thead').append(html);
      }
		},

		fillMonths: function(){
      var localDate = this._utc_to_local(this.viewDate);
			var html = '';
			var focused;
			for (var i = 0; i < 12; i++){
				focused = localDate && localDate.getMonth() === i ? ' focused' : '';
				html += '<span class="month' + focused + '">' + dates[this.o.language].monthsShort[i] + '</span>';
			}
			this.picker.find('.datepicker-months td').html(html);
		},

		setRange: function(range){
			if (!range || !range.length)
				delete this.range;
			else
				this.range = $.map(range, function(d){
					return d.valueOf();
				});
			this.fill();
		},

		getClassNames: function(date){
			var cls = [],
				year = this.viewDate.getUTCFullYear(),
				month = this.viewDate.getUTCMonth(),
				today = UTCToday();
			if (date.getUTCFullYear() < year || (date.getUTCFullYear() === year && date.getUTCMonth() < month)){
				cls.push('old');
			} else if (date.getUTCFullYear() > year || (date.getUTCFullYear() === year && date.getUTCMonth() > month)){
				cls.push('new');
			}
			if (this.focusDate && date.valueOf() === this.focusDate.valueOf())
				cls.push('focused');
			// Compare internal UTC date with UTC today, not local today
			if (this.o.todayHighlight && isUTCEquals(date, today)) {
				cls.push('today');
			}
			if (this.dates.contains(date) !== -1)
				cls.push('active');
			if (!this.dateWithinRange(date)){
				cls.push('disabled');
			}
			if (this.dateIsDisabled(date)){
				cls.push('disabled', 'disabled-date');
			}
			if ($.inArray(date.getUTCDay(), this.o.daysOfWeekHighlighted) !== -1){
				cls.push('highlighted');
			}

			if (this.range){
				if (date > this.range[0] && date < this.range[this.range.length-1]){
					cls.push('range');
				}
				if ($.inArray(date.valueOf(), this.range) !== -1){
					cls.push('selected');
				}
				if (date.valueOf() === this.range[0]){
          cls.push('range-start');
        }
        if (date.valueOf() === this.range[this.range.length-1]){
          cls.push('range-end');
        }
			}
			return cls;
		},

		_fill_yearsView: function(selector, cssClass, factor, year, startYear, endYear, beforeFn){
			var html = '';
			var step = factor / 10;
			var view = this.picker.find(selector);
			var startVal = Math.floor(year / factor) * factor;
			var endVal = startVal + step * 9;
			var focusedVal = Math.floor(this.viewDate.getFullYear() / step) * step;
			var selected = $.map(this.dates, function(d){
				return Math.floor(d.getUTCFullYear() / step) * step;
			});

			var classes, tooltip, before;
			for (var currVal = startVal - step; currVal <= endVal + step; currVal += step) {
				classes = [cssClass];
				tooltip = null;

				if (currVal === startVal - step) {
					classes.push('old');
				} else if (currVal === endVal + step) {
					classes.push('new');
				}
				if ($.inArray(currVal, selected) !== -1) {
					classes.push('active');
				}
				if (currVal < startYear || currVal > endYear) {
					classes.push('disabled');
				}
				if (currVal === focusedVal) {
				  classes.push('focused');
        }

				if (beforeFn !== $.noop) {
					before = beforeFn(new Date(currVal, 0, 1));
					if (before === undefined) {
						before = {};
					} else if (typeof before === 'boolean') {
						before = {enabled: before};
					} else if (typeof before === 'string') {
						before = {classes: before};
					}
					if (before.enabled === false) {
						classes.push('disabled');
					}
					if (before.classes) {
						classes = classes.concat(before.classes.split(/\s+/));
					}
					if (before.tooltip) {
						tooltip = before.tooltip;
					}
				}

				html += '<span class="' + classes.join(' ') + '"' + (tooltip ? ' title="' + tooltip + '"' : '') + '>' + currVal + '</span>';
			}

			view.find('.datepicker-switch').text(startVal + '-' + endVal);
			view.find('td').html(html);
		},

		fill: function(){
			var d = new Date(this.viewDate),
				year = d.getUTCFullYear(),
				month = d.getUTCMonth(),
				startYear = this.o.startDate !== -Infinity ? this.o.startDate.getUTCFullYear() : -Infinity,
				startMonth = this.o.startDate !== -Infinity ? this.o.startDate.getUTCMonth() : -Infinity,
				endYear = this.o.endDate !== Infinity ? this.o.endDate.getUTCFullYear() : Infinity,
				endMonth = this.o.endDate !== Infinity ? this.o.endDate.getUTCMonth() : Infinity,
				todaytxt = dates[this.o.language].today || dates['en'].today || '',
				cleartxt = dates[this.o.language].clear || dates['en'].clear || '',
        titleFormat = dates[this.o.language].titleFormat || dates['en'].titleFormat,
        todayDate = UTCToday(),
        titleBtnVisible = (this.o.todayBtn === true || this.o.todayBtn === 'linked') && todayDate >= this.o.startDate && todayDate <= this.o.endDate && !this.weekOfDateIsDisabled(todayDate),
				tooltip,
				before;
			if (isNaN(year) || isNaN(month))
				return;
			this.picker.find('.datepicker-days .datepicker-switch')
						.text(DPGlobal.formatDate(d, titleFormat, this.o.language));
			this.picker.find('tfoot .today')
						.text(todaytxt)
            .css('display', titleBtnVisible ? 'table-cell' : 'none');
			this.picker.find('tfoot .clear')
						.text(cleartxt)
						.css('display', this.o.clearBtn === true ? 'table-cell' : 'none');
			this.picker.find('thead .datepicker-title')
						.text(this.o.title)
						.css('display', typeof this.o.title === 'string' && this.o.title !== '' ? 'table-cell' : 'none');
			this.updateNavArrows();
			this.fillMonths();
			var prevMonth = UTCDate(year, month, 0),
				day = prevMonth.getUTCDate();
			prevMonth.setUTCDate(day - (prevMonth.getUTCDay() - this.o.weekStart + 7)%7);
			var nextMonth = new Date(prevMonth);
			if (prevMonth.getUTCFullYear() < 100){
        nextMonth.setUTCFullYear(prevMonth.getUTCFullYear());
      }
			nextMonth.setUTCDate(nextMonth.getUTCDate() + 42);
			nextMonth = nextMonth.valueOf();
			var html = [];
			var weekDay, clsName;
			while (prevMonth.valueOf() < nextMonth){
				weekDay = prevMonth.getUTCDay();
				if (weekDay === this.o.weekStart){
					html.push('<tr>');
					if (this.o.calendarWeeks){
						// ISO 8601: First week contains first thursday.
						// ISO also states week starts on Monday, but we can be more abstract here.
						var
							// Start of current week: based on weekstart/current date
							ws = new Date(+prevMonth + (this.o.weekStart - weekDay - 7) % 7 * 864e5),
							// Thursday of this week
							th = new Date(Number(ws) + (7 + 4 - ws.getUTCDay()) % 7 * 864e5),
							// First Thursday of year, year from thursday
							yth = new Date(Number(yth = UTCDate(th.getUTCFullYear(), 0, 1)) + (7 + 4 - yth.getUTCDay()) % 7 * 864e5),
							// Calendar week: ms between thursdays, div ms per day, div 7 days
							calWeek = (th - yth) / 864e5 / 7 + 1;
						html.push('<td class="cw">'+ calWeek +'</td>');
					}
				}
				clsName = this.getClassNames(prevMonth);
				clsName.push('day');

				var content = prevMonth.getUTCDate();

				if (this.o.beforeShowDay !== $.noop){
					before = this.o.beforeShowDay(this._utc_to_local(prevMonth));
					if (before === undefined)
						before = {};
					else if (typeof before === 'boolean')
						before = {enabled: before};
					else if (typeof before === 'string')
						before = {classes: before};
					if (before.enabled === false)
						clsName.push('disabled');
					if (before.classes)
						clsName = clsName.concat(before.classes.split(/\s+/));
					if (before.tooltip)
						tooltip = before.tooltip;
					if (before.content)
						content = before.content;
				}

				//Check if uniqueSort exists (supported by jquery >=1.12 and >=2.2)
				//Fallback to unique function for older jquery versions
				if ($.isFunction($.uniqueSort)) {
					clsName = $.uniqueSort(clsName);
				} else {
					clsName = $.unique(clsName);
				}

				html.push('<td class="'+clsName.join(' ')+'"' + (tooltip ? ' title="'+tooltip+'"' : '') + ' data-date="' + prevMonth.getTime().toString() + '">' + content + '</td>');
				tooltip = null;
				if (weekDay === this.o.weekEnd){
					html.push('</tr>');
				}
				prevMonth.setUTCDate(prevMonth.getUTCDate() + 1);
			}
			this.picker.find('.datepicker-days tbody').html(html.join(''));

			var monthsTitle = dates[this.o.language].monthsTitle || dates['en'].monthsTitle || 'Months';
			var months = this.picker.find('.datepicker-months')
						.find('.datepicker-switch')
							.text(this.o.maxViewMode < 2 ? monthsTitle : year)
							.end()
						.find('tbody span').removeClass('active');

			$.each(this.dates, function(i, d){
				if (d.getUTCFullYear() === year)
					months.eq(d.getUTCMonth()).addClass('active');
			});

			if (year < startYear || year > endYear){
				months.addClass('disabled');
			}
			if (year === startYear){
				months.slice(0, startMonth).addClass('disabled');
			}
			if (year === endYear){
				months.slice(endMonth+1).addClass('disabled');
			}

			if (this.o.beforeShowMonth !== $.noop){
				var that = this;
				$.each(months, function(i, month){
          var moDate = new Date(year, i, 1);
          var before = that.o.beforeShowMonth(moDate);
					if (before === undefined)
						before = {};
					else if (typeof before === 'boolean')
						before = {enabled: before};
					else if (typeof before === 'string')
						before = {classes: before};
					if (before.enabled === false && !$(month).hasClass('disabled'))
					    $(month).addClass('disabled');
					if (before.classes)
					    $(month).addClass(before.classes);
					if (before.tooltip)
					    $(month).prop('title', before.tooltip);
				});
			}

			// Generating decade/years picker
			this._fill_yearsView(
				'.datepicker-years',
				'year',
				10,
				year,
				startYear,
				endYear,
				this.o.beforeShowYear
			);

			// Generating century/decades picker
			this._fill_yearsView(
				'.datepicker-decades',
				'decade',
				100,
				year,
				startYear,
				endYear,
				this.o.beforeShowDecade
			);

			// Generating millennium/centuries picker
			this._fill_yearsView(
				'.datepicker-centuries',
				'century',
				1000,
				year,
				startYear,
				endYear,
				this.o.beforeShowCentury
			);
		},

		updateNavArrows: function(){
			if (!this._allow_update)
				return;

			var d = new Date(this.viewDate),
				year = d.getUTCFullYear(),
				month = d.getUTCMonth(),
				startYear = this.o.startDate !== -Infinity ? this.o.startDate.getUTCFullYear() : -Infinity,
				startMonth = this.o.startDate !== -Infinity ? this.o.startDate.getUTCMonth() : -Infinity,
				endYear = this.o.endDate !== Infinity ? this.o.endDate.getUTCFullYear() : Infinity,
				endMonth = this.o.endDate !== Infinity ? this.o.endDate.getUTCMonth() : Infinity,
				prevIsDisabled,
				nextIsDisabled,
				factor = 1;
			switch (this.viewMode){
				case 4:
					factor *= 10;
					/* falls through */
				case 3:
					factor *= 10;
					/* falls through */
				case 2:
					factor *= 10;
					/* falls through */
				case 1:
					prevIsDisabled = Math.floor(year / factor) * factor <= startYear;
					nextIsDisabled = Math.floor(year / factor) * factor + factor > endYear;
					break;
				case 0:
					prevIsDisabled = year <= startYear && month <= startMonth;
					nextIsDisabled = year >= endYear && month >= endMonth;
					break;
			}

			this.picker.find('.prev').toggleClass('disabled', prevIsDisabled);
			this.picker.find('.next').toggleClass('disabled', nextIsDisabled);
		},

		click: function(e){
			e.preventDefault();
			e.stopPropagation();

			var target, dir, day, year, month;
			target = $(e.target);

			// Clicked on the switch
			if (target.hasClass('datepicker-switch') && this.viewMode !== this.o.maxViewMode){
				this.setViewMode(this.viewMode + 1);
			}

			// Clicked on today button
			if (target.hasClass('today') && !target.hasClass('day')){
				this.setViewMode(0);
				this._setDate(UTCToday(), this.o.todayBtn === 'linked' ? null : 'view');
			}

			// Clicked on clear button
			if (target.hasClass('clear')){
				this.clearDates();
			}

			if (!target.hasClass('disabled')){
				// Clicked on a month, year, decade, century
				if (target.hasClass('month')
						|| target.hasClass('year')
						|| target.hasClass('decade')
						|| target.hasClass('century')) {
					this.viewDate.setUTCDate(1);

					day = 1;
					if (this.viewMode === 1){
						month = target.parent().find('span').index(target);
						year = this.viewDate.getUTCFullYear();
						this.viewDate.setUTCMonth(month);
					} else {
						month = 0;
						year = Number(target.text());
						this.viewDate.setUTCFullYear(year);
					}

					this._trigger(DPGlobal.viewModes[this.viewMode - 1].e, this.viewDate);

					if (this.viewMode === this.o.minViewMode){
						this._setDate(UTCDate(year, month, day));
					} else {
						this.setViewMode(this.viewMode - 1);
						this.fill();
					}
				}
			}

			if (this.picker.is(':visible') && this._focused_from){
				this._focused_from.focus();
			}
			delete this._focused_from;
		},

		dayCellClick: function(e){
			var $target = $(e.currentTarget);
			var timestamp = $target.data('date');
			var date = new Date(timestamp);

			if (this.o.updateViewDate) {
				if (date.getUTCFullYear() !== this.viewDate.getUTCFullYear()) {
					this._trigger('changeYear', this.viewDate);
				}

				if (date.getUTCMonth() !== this.viewDate.getUTCMonth()) {
					this._trigger('changeMonth', this.viewDate);
				}
			}
			this._setDate(date);
		},

		// Clicked on prev or next
		navArrowsClick: function(e){
			var $target = $(e.currentTarget);
			var dir = $target.hasClass('prev') ? -1 : 1;
			if (this.viewMode !== 0){
				dir *= DPGlobal.viewModes[this.viewMode].navStep * 12;
			}
			this.viewDate = this.moveMonth(this.viewDate, dir);
			this._trigger(DPGlobal.viewModes[this.viewMode].e, this.viewDate);
			this.fill();
		},

		_toggle_multidate: function(date){
			var ix = this.dates.contains(date);
			if (!date){
				this.dates.clear();
			}

			if (ix !== -1){
				if (this.o.multidate === true || this.o.multidate > 1 || this.o.toggleActive){
					this.dates.remove(ix);
				}
			} else if (this.o.multidate === false) {
				this.dates.clear();
				this.dates.push(date);
			}
			else {
				this.dates.push(date);
			}

			if (typeof this.o.multidate === 'number')
				while (this.dates.length > this.o.multidate)
					this.dates.remove(0);
		},

		_setDate: function(date, which){
			if (!which || which === 'date')
				this._toggle_multidate(date && new Date(date));
			if ((!which && this.o.updateViewDate) || which === 'view')
				this.viewDate = date && new Date(date);

			this.fill();
			this.setValue();
			if (!which || which !== 'view') {
				this._trigger('changeDate');
			}
			this.inputField.trigger('change');
			if (this.o.autoclose && (!which || which === 'date')){
				this.hide();
			}
		},

		moveDay: function(date, dir){
			var newDate = new Date(date);
			newDate.setUTCDate(date.getUTCDate() + dir);

			return newDate;
		},

		moveWeek: function(date, dir){
			return this.moveDay(date, dir * 7);
		},

		moveMonth: function(date, dir){
			if (!isValidDate(date))
				return this.o.defaultViewDate;
			if (!dir)
				return date;
			var new_date = new Date(date.valueOf()),
				day = new_date.getUTCDate(),
				month = new_date.getUTCMonth(),
				mag = Math.abs(dir),
				new_month, test;
			dir = dir > 0 ? 1 : -1;
			if (mag === 1){
				test = dir === -1
					// If going back one month, make sure month is not current month
					// (eg, Mar 31 -> Feb 31 == Feb 28, not Mar 02)
					? function(){
						return new_date.getUTCMonth() === month;
					}
					// If going forward one month, make sure month is as expected
					// (eg, Jan 31 -> Feb 31 == Feb 28, not Mar 02)
					: function(){
						return new_date.getUTCMonth() !== new_month;
					};
				new_month = month + dir;
				new_date.setUTCMonth(new_month);
				// Dec -> Jan (12) or Jan -> Dec (-1) -- limit expected date to 0-11
				new_month = (new_month + 12) % 12;
			}
			else {
				// For magnitudes >1, move one month at a time...
				for (var i=0; i < mag; i++)
					// ...which might decrease the day (eg, Jan 31 to Feb 28, etc)...
					new_date = this.moveMonth(new_date, dir);
				// ...then reset the day, keeping it in the new month
				new_month = new_date.getUTCMonth();
				new_date.setUTCDate(day);
				test = function(){
					return new_month !== new_date.getUTCMonth();
				};
			}
			// Common date-resetting loop -- if date is beyond end of month, make it
			// end of month
			while (test()){
				new_date.setUTCDate(--day);
				new_date.setUTCMonth(new_month);
			}
			return new_date;
		},

		moveYear: function(date, dir){
			return this.moveMonth(date, dir*12);
		},

		moveAvailableDate: function(date, dir, fn){
			do {
				date = this[fn](date, dir);

				if (!this.dateWithinRange(date))
					return false;

				fn = 'moveDay';
			}
			while (this.dateIsDisabled(date));

			return date;
		},

		weekOfDateIsDisabled: function(date){
			return $.inArray(date.getUTCDay(), this.o.daysOfWeekDisabled) !== -1;
		},

		dateIsDisabled: function(date){
			return (
				this.weekOfDateIsDisabled(date) ||
				$.grep(this.o.datesDisabled, function(d){
					return isUTCEquals(date, d);
				}).length > 0
			);
		},

		dateWithinRange: function(date){
			return date >= this.o.startDate && date <= this.o.endDate;
		},

		keydown: function(e){
			if (!this.picker.is(':visible')){
				if (e.keyCode === 40 || e.keyCode === 27) { // allow down to re-show picker
					this.show();
					e.stopPropagation();
        }
				return;
			}
			var dateChanged = false,
				dir, newViewDate,
				focusDate = this.focusDate || this.viewDate;
			switch (e.keyCode){
				case 27: // escape
					if (this.focusDate){
						this.focusDate = null;
						this.viewDate = this.dates.get(-1) || this.viewDate;
						this.fill();
					}
					else
						this.hide();
					e.preventDefault();
					e.stopPropagation();
					break;
				case 37: // left
				case 38: // up
				case 39: // right
				case 40: // down
					if (!this.o.keyboardNavigation || this.o.daysOfWeekDisabled.length === 7)
						break;
					dir = e.keyCode === 37 || e.keyCode === 38 ? -1 : 1;
          if (this.viewMode === 0) {
  					if (e.ctrlKey){
  						newViewDate = this.moveAvailableDate(focusDate, dir, 'moveYear');

  						if (newViewDate)
  							this._trigger('changeYear', this.viewDate);
  					} else if (e.shiftKey){
  						newViewDate = this.moveAvailableDate(focusDate, dir, 'moveMonth');

  						if (newViewDate)
  							this._trigger('changeMonth', this.viewDate);
  					} else if (e.keyCode === 37 || e.keyCode === 39){
  						newViewDate = this.moveAvailableDate(focusDate, dir, 'moveDay');
  					} else if (!this.weekOfDateIsDisabled(focusDate)){
  						newViewDate = this.moveAvailableDate(focusDate, dir, 'moveWeek');
  					}
          } else if (this.viewMode === 1) {
            if (e.keyCode === 38 || e.keyCode === 40) {
              dir = dir * 4;
            }
            newViewDate = this.moveAvailableDate(focusDate, dir, 'moveMonth');
          } else if (this.viewMode === 2) {
            if (e.keyCode === 38 || e.keyCode === 40) {
              dir = dir * 4;
            }
            newViewDate = this.moveAvailableDate(focusDate, dir, 'moveYear');
          }
					if (newViewDate){
						this.focusDate = this.viewDate = newViewDate;
						this.setValue();
						this.fill();
						e.preventDefault();
					}
					break;
				case 13: // enter
					if (!this.o.forceParse)
						break;
					focusDate = this.focusDate || this.dates.get(-1) || this.viewDate;
					if (this.o.keyboardNavigation) {
						this._toggle_multidate(focusDate);
						dateChanged = true;
					}
					this.focusDate = null;
					this.viewDate = this.dates.get(-1) || this.viewDate;
					this.setValue();
					this.fill();
					if (this.picker.is(':visible')){
						e.preventDefault();
						e.stopPropagation();
						if (this.o.autoclose)
							this.hide();
					}
					break;
				case 9: // tab
					this.focusDate = null;
					this.viewDate = this.dates.get(-1) || this.viewDate;
					this.fill();
					this.hide();
					break;
			}
			if (dateChanged){
				if (this.dates.length)
					this._trigger('changeDate');
				else
					this._trigger('clearDate');
				this.inputField.trigger('change');
			}
		},

		setViewMode: function(viewMode){
			this.viewMode = viewMode;
			this.picker
				.children('div')
				.hide()
				.filter('.datepicker-' + DPGlobal.viewModes[this.viewMode].clsName)
					.show();
			this.updateNavArrows();
      this._trigger('changeViewMode', new Date(this.viewDate));
		}
	};

	var DateRangePicker = function(element, options){
		$.data(element, 'datepicker', this);
		this.element = $(element);
		this.inputs = $.map(options.inputs, function(i){
			return i.jquery ? i[0] : i;
		});
		delete options.inputs;

		this.keepEmptyValues = options.keepEmptyValues;
		delete options.keepEmptyValues;

		datepickerPlugin.call($(this.inputs), options)
			.on('changeDate', $.proxy(this.dateUpdated, this));

		this.pickers = $.map(this.inputs, function(i){
			return $.data(i, 'datepicker');
		});
		this.updateDates();
	};
	DateRangePicker.prototype = {
		updateDates: function(){
			this.dates = $.map(this.pickers, function(i){
				return i.getUTCDate();
			});
			this.updateRanges();
		},
		updateRanges: function(){
			var range = $.map(this.dates, function(d){
				return d.valueOf();
			});
			$.each(this.pickers, function(i, p){
				p.setRange(range);
			});
		},
		clearDates: function(){
			$.each(this.pickers, function(i, p){
				p.clearDates();
			});
		},
		dateUpdated: function(e){
			// `this.updating` is a workaround for preventing infinite recursion
			// between `changeDate` triggering and `setUTCDate` calling.  Until
			// there is a better mechanism.
			if (this.updating)
				return;
			this.updating = true;

			var dp = $.data(e.target, 'datepicker');

			if (dp === undefined) {
				return;
			}

			var new_date = dp.getUTCDate(),
				keep_empty_values = this.keepEmptyValues,
				i = $.inArray(e.target, this.inputs),
				j = i - 1,
				k = i + 1,
				l = this.inputs.length;
			if (i === -1)
				return;

			$.each(this.pickers, function(i, p){
				if (!p.getUTCDate() && (p === dp || !keep_empty_values))
					p.setUTCDate(new_date);
			});

			if (new_date < this.dates[j]){
				// Date being moved earlier/left
				while (j >= 0 && new_date < this.dates[j]){
					this.pickers[j--].setUTCDate(new_date);
				}
			} else if (new_date > this.dates[k]){
				// Date being moved later/right
				while (k < l && new_date > this.dates[k]){
					this.pickers[k++].setUTCDate(new_date);
				}
			}
			this.updateDates();

			delete this.updating;
		},
		destroy: function(){
			$.map(this.pickers, function(p){ p.destroy(); });
			$(this.inputs).off('changeDate', this.dateUpdated);
			delete this.element.data().datepicker;
		},
		remove: alias('destroy', 'Method `remove` is deprecated and will be removed in version 2.0. Use `destroy` instead')
	};

	function opts_from_el(el, prefix){
		// Derive options from element data-attrs
		var data = $(el).data(),
			out = {}, inkey,
			replace = new RegExp('^' + prefix.toLowerCase() + '([A-Z])');
		prefix = new RegExp('^' + prefix.toLowerCase());
		function re_lower(_,a){
			return a.toLowerCase();
		}
		for (var key in data)
			if (prefix.test(key)){
				inkey = key.replace(replace, re_lower);
				out[inkey] = data[key];
			}
		return out;
	}

	function opts_from_locale(lang){
		// Derive options from locale plugins
		var out = {};
		// Check if "de-DE" style date is available, if not language should
		// fallback to 2 letter code eg "de"
		if (!dates[lang]){
			lang = lang.split('-')[0];
			if (!dates[lang])
				return;
		}
		var d = dates[lang];
		$.each(locale_opts, function(i,k){
			if (k in d)
				out[k] = d[k];
		});
		return out;
	}

	var old = $.fn.datepicker;
	var datepickerPlugin = function(option){
		var args = Array.apply(null, arguments);
		args.shift();
		var internal_return;
		this.each(function(){
			var $this = $(this),
				data = $this.data('datepicker'),
				options = typeof option === 'object' && option;
			if (!data){
				var elopts = opts_from_el(this, 'date'),
					// Preliminary otions
					xopts = $.extend({}, defaults, elopts, options),
					locopts = opts_from_locale(xopts.language),
					// Options priority: js args, data-attrs, locales, defaults
					opts = $.extend({}, defaults, locopts, elopts, options);
				if ($this.hasClass('input-daterange') || opts.inputs){
					$.extend(opts, {
						inputs: opts.inputs || $this.find('input').toArray()
					});
					data = new DateRangePicker(this, opts);
				}
				else {
					data = new Datepicker(this, opts);
				}
				$this.data('datepicker', data);
			}
			if (typeof option === 'string' && typeof data[option] === 'function'){
				internal_return = data[option].apply(data, args);
			}
		});

		if (
			internal_return === undefined ||
			internal_return instanceof Datepicker ||
			internal_return instanceof DateRangePicker
		)
			return this;

		if (this.length > 1)
			throw new Error('Using only allowed for the collection of a single element (' + option + ' function)');
		else
			return internal_return;
	};
	$.fn.datepicker = datepickerPlugin;

	var defaults = $.fn.datepicker.defaults = {
		assumeNearbyYear: false,
		autoclose: false,
		beforeShowDay: $.noop,
		beforeShowMonth: $.noop,
		beforeShowYear: $.noop,
		beforeShowDecade: $.noop,
		beforeShowCentury: $.noop,
		calendarWeeks: false,
		clearBtn: false,
		toggleActive: false,
		daysOfWeekDisabled: [],
		daysOfWeekHighlighted: [],
		datesDisabled: [],
		endDate: Infinity,
		forceParse: true,
		format: 'mm/dd/yyyy',
		keepEmptyValues: false,
		keyboardNavigation: true,
		language: 'en',
		minViewMode: 0,
		maxViewMode: 4,
		multidate: false,
		multidateSeparator: ',',
		orientation: "auto",
		rtl: false,
		startDate: -Infinity,
		startView: 0,
		todayBtn: false,
		todayHighlight: false,
		updateViewDate: true,
		weekStart: 0,
		disableTouchKeyboard: false,
		enableOnReadonly: true,
		showOnFocus: true,
		zIndexOffset: 10,
		container: 'body',
		immediateUpdates: false,
		title: '',
		templates: {
			leftArrow: '&#x00AB;',
			rightArrow: '&#x00BB;'
		},
    showWeekDays: true
	};
	var locale_opts = $.fn.datepicker.locale_opts = [
		'format',
		'rtl',
		'weekStart'
	];
	$.fn.datepicker.Constructor = Datepicker;
	var dates = $.fn.datepicker.dates = {
		en: {
			days: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
			daysShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
			daysMin: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
			months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
			monthsShort: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
			today: "Today",
			clear: "Clear",
			titleFormat: "MM yyyy"
		}
	};

	var DPGlobal = {
		viewModes: [
			{
				names: ['days', 'month'],
				clsName: 'days',
				e: 'changeMonth'
			},
			{
				names: ['months', 'year'],
				clsName: 'months',
				e: 'changeYear',
				navStep: 1
			},
			{
				names: ['years', 'decade'],
				clsName: 'years',
				e: 'changeDecade',
				navStep: 10
			},
			{
				names: ['decades', 'century'],
				clsName: 'decades',
				e: 'changeCentury',
				navStep: 100
			},
			{
				names: ['centuries', 'millennium'],
				clsName: 'centuries',
				e: 'changeMillennium',
				navStep: 1000
			}
		],
		validParts: /dd?|DD?|mm?|MM?|yy(?:yy)?/g,
		nonpunctuation: /[^ -\/:-@\u5e74\u6708\u65e5\[-`{-~\t\n\r]+/g,
		parseFormat: function(format){
			if (typeof format.toValue === 'function' && typeof format.toDisplay === 'function')
                return format;
            // IE treats \0 as a string end in inputs (truncating the value),
			// so it's a bad format delimiter, anyway
			var separators = format.replace(this.validParts, '\0').split('\0'),
				parts = format.match(this.validParts);
			if (!separators || !separators.length || !parts || parts.length === 0){
				throw new Error("Invalid date format.");
			}
			return {separators: separators, parts: parts};
		},
		parseDate: function(date, format, language, assumeNearby){
			if (!date)
				return undefined;
			if (date instanceof Date)
				return date;
			if (typeof format === 'string')
				format = DPGlobal.parseFormat(format);
			if (format.toValue)
				return format.toValue(date, format, language);
			var fn_map = {
					d: 'moveDay',
					m: 'moveMonth',
					w: 'moveWeek',
					y: 'moveYear'
				},
				dateAliases = {
					yesterday: '-1d',
					today: '+0d',
					tomorrow: '+1d'
				},
				parts, part, dir, i, fn;
			if (date in dateAliases){
				date = dateAliases[date];
			}
			if (/^[\-+]\d+[dmwy]([\s,]+[\-+]\d+[dmwy])*$/i.test(date)){
				parts = date.match(/([\-+]\d+)([dmwy])/gi);
				date = new Date();
				for (i=0; i < parts.length; i++){
					part = parts[i].match(/([\-+]\d+)([dmwy])/i);
					dir = Number(part[1]);
					fn = fn_map[part[2].toLowerCase()];
					date = Datepicker.prototype[fn](date, dir);
				}
				return Datepicker.prototype._zero_utc_time(date);
			}

			parts = date && date.match(this.nonpunctuation) || [];

			function applyNearbyYear(year, threshold){
				if (threshold === true)
					threshold = 10;

				// if year is 2 digits or less, than the user most likely is trying to get a recent century
				if (year < 100){
					year += 2000;
					// if the new year is more than threshold years in advance, use last century
					if (year > ((new Date()).getFullYear()+threshold)){
						year -= 100;
					}
				}

				return year;
			}

			var parsed = {},
				setters_order = ['yyyy', 'yy', 'M', 'MM', 'm', 'mm', 'd', 'dd'],
				setters_map = {
					yyyy: function(d,v){
						return d.setUTCFullYear(assumeNearby ? applyNearbyYear(v, assumeNearby) : v);
					},
					m: function(d,v){
						if (isNaN(d))
							return d;
						v -= 1;
						while (v < 0) v += 12;
						v %= 12;
						d.setUTCMonth(v);
						while (d.getUTCMonth() !== v)
							d.setUTCDate(d.getUTCDate()-1);
						return d;
					},
					d: function(d,v){
						return d.setUTCDate(v);
					}
				},
				val, filtered;
			setters_map['yy'] = setters_map['yyyy'];
			setters_map['M'] = setters_map['MM'] = setters_map['mm'] = setters_map['m'];
			setters_map['dd'] = setters_map['d'];
			date = UTCToday();
			var fparts = format.parts.slice();
			// Remove noop parts
			if (parts.length !== fparts.length){
				fparts = $(fparts).filter(function(i,p){
					return $.inArray(p, setters_order) !== -1;
				}).toArray();
			}
			// Process remainder
			function match_part(){
				var m = this.slice(0, parts[i].length),
					p = parts[i].slice(0, m.length);
				return m.toLowerCase() === p.toLowerCase();
			}
			if (parts.length === fparts.length){
				var cnt;
				for (i=0, cnt = fparts.length; i < cnt; i++){
					val = parseInt(parts[i], 10);
					part = fparts[i];
					if (isNaN(val)){
						switch (part){
							case 'MM':
								filtered = $(dates[language].months).filter(match_part);
								val = $.inArray(filtered[0], dates[language].months) + 1;
								break;
							case 'M':
								filtered = $(dates[language].monthsShort).filter(match_part);
								val = $.inArray(filtered[0], dates[language].monthsShort) + 1;
								break;
						}
					}
					parsed[part] = val;
				}
				var _date, s;
				for (i=0; i < setters_order.length; i++){
					s = setters_order[i];
					if (s in parsed && !isNaN(parsed[s])){
						_date = new Date(date);
						setters_map[s](_date, parsed[s]);
						if (!isNaN(_date))
							date = _date;
					}
				}
			}
			return date;
		},
		formatDate: function(date, format, language){
			if (!date)
				return '';
			if (typeof format === 'string')
				format = DPGlobal.parseFormat(format);
			if (format.toDisplay)
                return format.toDisplay(date, format, language);
            var val = {
				d: date.getUTCDate(),
				D: dates[language].daysShort[date.getUTCDay()],
				DD: dates[language].days[date.getUTCDay()],
				m: date.getUTCMonth() + 1,
				M: dates[language].monthsShort[date.getUTCMonth()],
				MM: dates[language].months[date.getUTCMonth()],
				yy: date.getUTCFullYear().toString().substring(2),
				yyyy: date.getUTCFullYear()
			};
			val.dd = (val.d < 10 ? '0' : '') + val.d;
			val.mm = (val.m < 10 ? '0' : '') + val.m;
			date = [];
			var seps = $.extend([], format.separators);
			for (var i=0, cnt = format.parts.length; i <= cnt; i++){
				if (seps.length)
					date.push(seps.shift());
				date.push(val[format.parts[i]]);
			}
			return date.join('');
		},
		headTemplate: '<thead>'+
			              '<tr>'+
			                '<th colspan="7" class="datepicker-title"></th>'+
			              '</tr>'+
							'<tr>'+
								'<th class="prev">'+defaults.templates.leftArrow+'</th>'+
								'<th colspan="5" class="datepicker-switch"></th>'+
								'<th class="next">'+defaults.templates.rightArrow+'</th>'+
							'</tr>'+
						'</thead>',
		contTemplate: '<tbody><tr><td colspan="7"></td></tr></tbody>',
		footTemplate: '<tfoot>'+
							'<tr>'+
								'<th colspan="7" class="today"></th>'+
							'</tr>'+
							'<tr>'+
								'<th colspan="7" class="clear"></th>'+
							'</tr>'+
						'</tfoot>'
	};
	DPGlobal.template = '<div class="datepicker">'+
							'<div class="datepicker-days">'+
								'<table class="table-condensed">'+
									DPGlobal.headTemplate+
									'<tbody></tbody>'+
									DPGlobal.footTemplate+
								'</table>'+
							'</div>'+
							'<div class="datepicker-months">'+
								'<table class="table-condensed">'+
									DPGlobal.headTemplate+
									DPGlobal.contTemplate+
									DPGlobal.footTemplate+
								'</table>'+
							'</div>'+
							'<div class="datepicker-years">'+
								'<table class="table-condensed">'+
									DPGlobal.headTemplate+
									DPGlobal.contTemplate+
									DPGlobal.footTemplate+
								'</table>'+
							'</div>'+
							'<div class="datepicker-decades">'+
								'<table class="table-condensed">'+
									DPGlobal.headTemplate+
									DPGlobal.contTemplate+
									DPGlobal.footTemplate+
								'</table>'+
							'</div>'+
							'<div class="datepicker-centuries">'+
								'<table class="table-condensed">'+
									DPGlobal.headTemplate+
									DPGlobal.contTemplate+
									DPGlobal.footTemplate+
								'</table>'+
							'</div>'+
						'</div>';

	$.fn.datepicker.DPGlobal = DPGlobal;


	/* DATEPICKER NO CONFLICT
	* =================== */

	$.fn.datepicker.noConflict = function(){
		$.fn.datepicker = old;
		return this;
	};

	/* DATEPICKER VERSION
	 * =================== */
	$.fn.datepicker.version = '1.9.0';

	$.fn.datepicker.deprecated = function(msg){
		var console = window.console;
		if (console && console.warn) {
			console.warn('DEPRECATED: ' + msg);
		}
	};


	/* DATEPICKER DATA-API
	* ================== */

	$(document).on(
		'focus.datepicker.data-api click.datepicker.data-api',
		'[data-provide="datepicker"]',
		function(e){
			var $this = $(this);
			if ($this.data('datepicker'))
				return;
			e.preventDefault();
			// component click requires us to explicitly show it
			datepickerPlugin.call($this, 'show');
		}
	);
	$(function(){
		datepickerPlugin.call($('[data-provide="datepicker-inline"]'));
	});

}));

// Ion.RangeSlider
// version 2.3.1 Build: 382
// © Denis Ineshin, 2019
// https://github.com/IonDen
//
// Project page:    http://ionden.com/a/plugins/ion.rangeSlider/en.html
// GitHub page:     https://github.com/IonDen/ion.rangeSlider
//
// Released under MIT licence:
// http://ionden.com/a/plugins/licence-en.html
// =====================================================================================================================

;(function(factory) {
    if ((typeof jQuery === 'undefined' || !jQuery) && typeof define === "function" && define.amd) {
        define(["jquery"], function (jQuery) {
            return factory(jQuery, document, window, navigator);
        });
    } else if ((typeof jQuery === 'undefined' || !jQuery) && typeof exports === "object") {
        factory(require("jquery"), document, window, navigator);
    } else {
        factory(jQuery, document, window, navigator);
    }
} (function ($, document, window, navigator, undefined) {
    "use strict";

    // =================================================================================================================
    // Service

    var plugin_count = 0;

    // IE8 fix
    var is_old_ie = (function () {
        var n = navigator.userAgent,
            r = /msie\s\d+/i,
            v;
        if (n.search(r) > 0) {
            v = r.exec(n).toString();
            v = v.split(" ")[1];
            if (v < 9) {
                $("html").addClass("lt-ie9");
                return true;
            }
        }
        return false;
    } ());
    if (!Function.prototype.bind) {
        Function.prototype.bind = function bind(that) {

            var target = this;
            var slice = [].slice;

            if (typeof target != "function") {
                throw new TypeError();
            }

            var args = slice.call(arguments, 1),
                bound = function () {

                    if (this instanceof bound) {

                        var F = function(){};
                        F.prototype = target.prototype;
                        var self = new F();

                        var result = target.apply(
                            self,
                            args.concat(slice.call(arguments))
                        );
                        if (Object(result) === result) {
                            return result;
                        }
                        return self;

                    } else {

                        return target.apply(
                            that,
                            args.concat(slice.call(arguments))
                        );

                    }

                };

            return bound;
        };
    }
    if (!Array.prototype.indexOf) {
        Array.prototype.indexOf = function(searchElement, fromIndex) {
            var k;
            if (this == null) {
                throw new TypeError('"this" is null or not defined');
            }
            var O = Object(this);
            var len = O.length >>> 0;
            if (len === 0) {
                return -1;
            }
            var n = +fromIndex || 0;
            if (Math.abs(n) === Infinity) {
                n = 0;
            }
            if (n >= len) {
                return -1;
            }
            k = Math.max(n >= 0 ? n : len - Math.abs(n), 0);
            while (k < len) {
                if (k in O && O[k] === searchElement) {
                    return k;
                }
                k++;
            }
            return -1;
        };
    }



    // =================================================================================================================
    // Template

    var base_html =
        '<span class="irs">' +
        '<span class="irs-line" tabindex="0"></span>' +
        '<span class="irs-min">0</span><span class="irs-max">1</span>' +
        '<span class="irs-from">0</span><span class="irs-to">0</span><span class="irs-single">0</span>' +
        '</span>' +
        '<span class="irs-grid"></span>';

    var single_html =
        '<span class="irs-bar irs-bar--single"></span>' +
        '<span class="irs-shadow shadow-single"></span>' +
        '<span class="irs-handle single"><i></i><i></i><i></i></span>';

    var double_html =
        '<span class="irs-bar"></span>' +
        '<span class="irs-shadow shadow-from"></span>' +
        '<span class="irs-shadow shadow-to"></span>' +
        '<span class="irs-handle from"><i></i><i></i><i></i></span>' +
        '<span class="irs-handle to"><i></i><i></i><i></i></span>';

    var disable_html =
        '<span class="irs-disable-mask"></span>';



    // =================================================================================================================
    // Core

    /**
     * Main plugin constructor
     *
     * @param input {Object} link to base input element
     * @param options {Object} slider config
     * @param plugin_count {Number}
     * @constructor
     */
    var IonRangeSlider = function (input, options, plugin_count) {
        this.VERSION = "2.3.1";
        this.input = input;
        this.plugin_count = plugin_count;
        this.current_plugin = 0;
        this.calc_count = 0;
        this.update_tm = 0;
        this.old_from = 0;
        this.old_to = 0;
        this.old_min_interval = null;
        this.raf_id = null;
        this.dragging = false;
        this.force_redraw = false;
        this.no_diapason = false;
        this.has_tab_index = true;
        this.is_key = false;
        this.is_update = false;
        this.is_start = true;
        this.is_finish = false;
        this.is_active = false;
        this.is_resize = false;
        this.is_click = false;

        options = options || {};

        // cache for links to all DOM elements
        this.$cache = {
            win: $(window),
            body: $(document.body),
            input: $(input),
            cont: null,
            rs: null,
            min: null,
            max: null,
            from: null,
            to: null,
            single: null,
            bar: null,
            line: null,
            s_single: null,
            s_from: null,
            s_to: null,
            shad_single: null,
            shad_from: null,
            shad_to: null,
            edge: null,
            grid: null,
            grid_labels: []
        };

        // storage for measure variables
        this.coords = {
            // left
            x_gap: 0,
            x_pointer: 0,

            // width
            w_rs: 0,
            w_rs_old: 0,
            w_handle: 0,

            // percents
            p_gap: 0,
            p_gap_left: 0,
            p_gap_right: 0,
            p_step: 0,
            p_pointer: 0,
            p_handle: 0,
            p_single_fake: 0,
            p_single_real: 0,
            p_from_fake: 0,
            p_from_real: 0,
            p_to_fake: 0,
            p_to_real: 0,
            p_bar_x: 0,
            p_bar_w: 0,

            // grid
            grid_gap: 0,
            big_num: 0,
            big: [],
            big_w: [],
            big_p: [],
            big_x: []
        };

        // storage for labels measure variables
        this.labels = {
            // width
            w_min: 0,
            w_max: 0,
            w_from: 0,
            w_to: 0,
            w_single: 0,

            // percents
            p_min: 0,
            p_max: 0,
            p_from_fake: 0,
            p_from_left: 0,
            p_to_fake: 0,
            p_to_left: 0,
            p_single_fake: 0,
            p_single_left: 0
        };



        /**
         * get and validate config
         */
        var $inp = this.$cache.input,
            val = $inp.prop("value"),
            config, config_from_data, prop;

        // default config
        config = {
            skin: "flat",
            type: "single",

            min: 10,
            max: 100,
            from: null,
            to: null,
            step: 1,

            min_interval: 0,
            max_interval: 0,
            drag_interval: false,

            values: [],
            p_values: [],

            from_fixed: false,
            from_min: null,
            from_max: null,
            from_shadow: false,

            to_fixed: false,
            to_min: null,
            to_max: null,
            to_shadow: false,

            prettify_enabled: true,
            prettify_separator: " ",
            prettify: null,

            force_edges: false,

            keyboard: true,

            grid: false,
            grid_margin: true,
            grid_num: 4,
            grid_snap: false,

            hide_min_max: false,
            hide_from_to: false,

            prefix: "",
            postfix: "",
            max_postfix: "",
            decorate_both: true,
            values_separator: " — ",

            input_values_separator: ";",

            disable: false,
            block: false,

            extra_classes: "",

            scope: null,
            onStart: null,
            onChange: null,
            onFinish: null,
            onUpdate: null
        };


        // check if base element is input
        if ($inp[0].nodeName !== "INPUT") {
            console && console.warn && console.warn("Base element should be <input>!", $inp[0]);
        }


        // config from data-attributes extends js config
        config_from_data = {
            skin: $inp.data("skin"),
            type: $inp.data("type"),

            min: $inp.data("min"),
            max: $inp.data("max"),
            from: $inp.data("from"),
            to: $inp.data("to"),
            step: $inp.data("step"),

            min_interval: $inp.data("minInterval"),
            max_interval: $inp.data("maxInterval"),
            drag_interval: $inp.data("dragInterval"),

            values: $inp.data("values"),

            from_fixed: $inp.data("fromFixed"),
            from_min: $inp.data("fromMin"),
            from_max: $inp.data("fromMax"),
            from_shadow: $inp.data("fromShadow"),

            to_fixed: $inp.data("toFixed"),
            to_min: $inp.data("toMin"),
            to_max: $inp.data("toMax"),
            to_shadow: $inp.data("toShadow"),

            prettify_enabled: $inp.data("prettifyEnabled"),
            prettify_separator: $inp.data("prettifySeparator"),

            force_edges: $inp.data("forceEdges"),

            keyboard: $inp.data("keyboard"),

            grid: $inp.data("grid"),
            grid_margin: $inp.data("gridMargin"),
            grid_num: $inp.data("gridNum"),
            grid_snap: $inp.data("gridSnap"),

            hide_min_max: $inp.data("hideMinMax"),
            hide_from_to: $inp.data("hideFromTo"),

            prefix: $inp.data("prefix"),
            postfix: $inp.data("postfix"),
            max_postfix: $inp.data("maxPostfix"),
            decorate_both: $inp.data("decorateBoth"),
            values_separator: $inp.data("valuesSeparator"),

            input_values_separator: $inp.data("inputValuesSeparator"),

            disable: $inp.data("disable"),
            block: $inp.data("block"),

            extra_classes: $inp.data("extraClasses"),
        };
        config_from_data.values = config_from_data.values && config_from_data.values.split(",");

        for (prop in config_from_data) {
            if (config_from_data.hasOwnProperty(prop)) {
                if (config_from_data[prop] === undefined || config_from_data[prop] === "") {
                    delete config_from_data[prop];
                }
            }
        }


        // input value extends default config
        if (val !== undefined && val !== "") {
            val = val.split(config_from_data.input_values_separator || options.input_values_separator || ";");

            if (val[0] && val[0] == +val[0]) {
                val[0] = +val[0];
            }
            if (val[1] && val[1] == +val[1]) {
                val[1] = +val[1];
            }

            if (options && options.values && options.values.length) {
                config.from = val[0] && options.values.indexOf(val[0]);
                config.to = val[1] && options.values.indexOf(val[1]);
            } else {
                config.from = val[0] && +val[0];
                config.to = val[1] && +val[1];
            }
        }



        // js config extends default config
        $.extend(config, options);


        // data config extends config
        $.extend(config, config_from_data);
        this.options = config;



        // validate config, to be sure that all data types are correct
        this.update_check = {};
        this.validate();



        // default result object, returned to callbacks
        this.result = {
            input: this.$cache.input,
            slider: null,

            min: this.options.min,
            max: this.options.max,

            from: this.options.from,
            from_percent: 0,
            from_value: null,

            to: this.options.to,
            to_percent: 0,
            to_value: null
        };



        this.init();
    };

    IonRangeSlider.prototype = {

        /**
         * Starts or updates the plugin instance
         *
         * @param [is_update] {boolean}
         */
        init: function (is_update) {
            this.no_diapason = false;
            this.coords.p_step = this.convertToPercent(this.options.step, true);

            this.target = "base";

            this.toggleInput();
            this.append();
            this.setMinMax();

            if (is_update) {
                this.force_redraw = true;
                this.calc(true);

                // callbacks called
                this.callOnUpdate();
            } else {
                this.force_redraw = true;
                this.calc(true);

                // callbacks called
                this.callOnStart();
            }

            this.updateScene();
        },

        /**
         * Appends slider template to a DOM
         */
        append: function () {
            var container_html = '<span class="irs irs--' + this.options.skin + ' js-irs-' + this.plugin_count + ' ' + this.options.extra_classes + '"></span>';
            this.$cache.input.before(container_html);
            this.$cache.input.prop("readonly", true);
            this.$cache.cont = this.$cache.input.prev();
            this.result.slider = this.$cache.cont;

            this.$cache.cont.html(base_html);
            this.$cache.rs = this.$cache.cont.find(".irs");
            this.$cache.min = this.$cache.cont.find(".irs-min");
            this.$cache.max = this.$cache.cont.find(".irs-max");
            this.$cache.from = this.$cache.cont.find(".irs-from");
            this.$cache.to = this.$cache.cont.find(".irs-to");
            this.$cache.single = this.$cache.cont.find(".irs-single");
            this.$cache.line = this.$cache.cont.find(".irs-line");
            this.$cache.grid = this.$cache.cont.find(".irs-grid");

            if (this.options.type === "single") {
                this.$cache.cont.append(single_html);
                this.$cache.bar = this.$cache.cont.find(".irs-bar");
                this.$cache.edge = this.$cache.cont.find(".irs-bar-edge");
                this.$cache.s_single = this.$cache.cont.find(".single");
                this.$cache.from[0].style.visibility = "hidden";
                this.$cache.to[0].style.visibility = "hidden";
                this.$cache.shad_single = this.$cache.cont.find(".shadow-single");
            } else {
                this.$cache.cont.append(double_html);
                this.$cache.bar = this.$cache.cont.find(".irs-bar");
                this.$cache.s_from = this.$cache.cont.find(".from");
                this.$cache.s_to = this.$cache.cont.find(".to");
                this.$cache.shad_from = this.$cache.cont.find(".shadow-from");
                this.$cache.shad_to = this.$cache.cont.find(".shadow-to");

                this.setTopHandler();
            }

            if (this.options.hide_from_to) {
                this.$cache.from[0].style.display = "none";
                this.$cache.to[0].style.display = "none";
                this.$cache.single[0].style.display = "none";
            }

            this.appendGrid();

            if (this.options.disable) {
                this.appendDisableMask();
                this.$cache.input[0].disabled = true;
            } else {
                this.$cache.input[0].disabled = false;
                this.removeDisableMask();
                this.bindEvents();
            }

            // block only if not disabled
            if (!this.options.disable) {
                if (this.options.block) {
                    this.appendDisableMask();
                } else {
                    this.removeDisableMask();
                }
            }

            if (this.options.drag_interval) {
                this.$cache.bar[0].style.cursor = "ew-resize";
            }
        },

        /**
         * Determine which handler has a priority
         * works only for double slider type
         */
        setTopHandler: function () {
            var min = this.options.min,
                max = this.options.max,
                from = this.options.from,
                to = this.options.to;

            if (from > min && to === max) {
                this.$cache.s_from.addClass("type_last");
            } else if (to < max) {
                this.$cache.s_to.addClass("type_last");
            }
        },

        /**
         * Determine which handles was clicked last
         * and which handler should have hover effect
         *
         * @param target {String}
         */
        changeLevel: function (target) {
            switch (target) {
                case "single":
                    this.coords.p_gap = this.toFixed(this.coords.p_pointer - this.coords.p_single_fake);
                    this.$cache.s_single.addClass("state_hover");
                    break;
                case "from":
                    this.coords.p_gap = this.toFixed(this.coords.p_pointer - this.coords.p_from_fake);
                    this.$cache.s_from.addClass("state_hover");
                    this.$cache.s_from.addClass("type_last");
                    this.$cache.s_to.removeClass("type_last");
                    break;
                case "to":
                    this.coords.p_gap = this.toFixed(this.coords.p_pointer - this.coords.p_to_fake);
                    this.$cache.s_to.addClass("state_hover");
                    this.$cache.s_to.addClass("type_last");
                    this.$cache.s_from.removeClass("type_last");
                    break;
                case "both":
                    this.coords.p_gap_left = this.toFixed(this.coords.p_pointer - this.coords.p_from_fake);
                    this.coords.p_gap_right = this.toFixed(this.coords.p_to_fake - this.coords.p_pointer);
                    this.$cache.s_to.removeClass("type_last");
                    this.$cache.s_from.removeClass("type_last");
                    break;
            }
        },

        /**
         * Then slider is disabled
         * appends extra layer with opacity
         */
        appendDisableMask: function () {
            this.$cache.cont.append(disable_html);
            this.$cache.cont.addClass("irs-disabled");
        },

        /**
         * Then slider is not disabled
         * remove disable mask
         */
        removeDisableMask: function () {
            this.$cache.cont.remove(".irs-disable-mask");
            this.$cache.cont.removeClass("irs-disabled");
        },

        /**
         * Remove slider instance
         * and unbind all events
         */
        remove: function () {
            this.$cache.cont.remove();
            this.$cache.cont = null;

            this.$cache.line.off("keydown.irs_" + this.plugin_count);

            this.$cache.body.off("touchmove.irs_" + this.plugin_count);
            this.$cache.body.off("mousemove.irs_" + this.plugin_count);

            this.$cache.win.off("touchend.irs_" + this.plugin_count);
            this.$cache.win.off("mouseup.irs_" + this.plugin_count);

            if (is_old_ie) {
                this.$cache.body.off("mouseup.irs_" + this.plugin_count);
                this.$cache.body.off("mouseleave.irs_" + this.plugin_count);
            }

            this.$cache.grid_labels = [];
            this.coords.big = [];
            this.coords.big_w = [];
            this.coords.big_p = [];
            this.coords.big_x = [];

            cancelAnimationFrame(this.raf_id);
        },

        /**
         * bind all slider events
         */
        bindEvents: function () {
            if (this.no_diapason) {
                return;
            }

            this.$cache.body.on("touchmove.irs_" + this.plugin_count, this.pointerMove.bind(this));
            this.$cache.body.on("mousemove.irs_" + this.plugin_count, this.pointerMove.bind(this));

            this.$cache.win.on("touchend.irs_" + this.plugin_count, this.pointerUp.bind(this));
            this.$cache.win.on("mouseup.irs_" + this.plugin_count, this.pointerUp.bind(this));

            this.$cache.line.on("touchstart.irs_" + this.plugin_count, this.pointerClick.bind(this, "click"));
            this.$cache.line.on("mousedown.irs_" + this.plugin_count, this.pointerClick.bind(this, "click"));

            this.$cache.line.on("focus.irs_" + this.plugin_count, this.pointerFocus.bind(this));

            if (this.options.drag_interval && this.options.type === "double") {
                this.$cache.bar.on("touchstart.irs_" + this.plugin_count, this.pointerDown.bind(this, "both"));
                this.$cache.bar.on("mousedown.irs_" + this.plugin_count, this.pointerDown.bind(this, "both"));
            } else {
                this.$cache.bar.on("touchstart.irs_" + this.plugin_count, this.pointerClick.bind(this, "click"));
                this.$cache.bar.on("mousedown.irs_" + this.plugin_count, this.pointerClick.bind(this, "click"));
            }

            if (this.options.type === "single") {
                this.$cache.single.on("touchstart.irs_" + this.plugin_count, this.pointerDown.bind(this, "single"));
                this.$cache.s_single.on("touchstart.irs_" + this.plugin_count, this.pointerDown.bind(this, "single"));
                this.$cache.shad_single.on("touchstart.irs_" + this.plugin_count, this.pointerClick.bind(this, "click"));

                this.$cache.single.on("mousedown.irs_" + this.plugin_count, this.pointerDown.bind(this, "single"));
                this.$cache.s_single.on("mousedown.irs_" + this.plugin_count, this.pointerDown.bind(this, "single"));
                this.$cache.edge.on("mousedown.irs_" + this.plugin_count, this.pointerClick.bind(this, "click"));
                this.$cache.shad_single.on("mousedown.irs_" + this.plugin_count, this.pointerClick.bind(this, "click"));
            } else {
                this.$cache.single.on("touchstart.irs_" + this.plugin_count, this.pointerDown.bind(this, null));
                this.$cache.single.on("mousedown.irs_" + this.plugin_count, this.pointerDown.bind(this, null));

                this.$cache.from.on("touchstart.irs_" + this.plugin_count, this.pointerDown.bind(this, "from"));
                this.$cache.s_from.on("touchstart.irs_" + this.plugin_count, this.pointerDown.bind(this, "from"));
                this.$cache.to.on("touchstart.irs_" + this.plugin_count, this.pointerDown.bind(this, "to"));
                this.$cache.s_to.on("touchstart.irs_" + this.plugin_count, this.pointerDown.bind(this, "to"));
                this.$cache.shad_from.on("touchstart.irs_" + this.plugin_count, this.pointerClick.bind(this, "click"));
                this.$cache.shad_to.on("touchstart.irs_" + this.plugin_count, this.pointerClick.bind(this, "click"));

                this.$cache.from.on("mousedown.irs_" + this.plugin_count, this.pointerDown.bind(this, "from"));
                this.$cache.s_from.on("mousedown.irs_" + this.plugin_count, this.pointerDown.bind(this, "from"));
                this.$cache.to.on("mousedown.irs_" + this.plugin_count, this.pointerDown.bind(this, "to"));
                this.$cache.s_to.on("mousedown.irs_" + this.plugin_count, this.pointerDown.bind(this, "to"));
                this.$cache.shad_from.on("mousedown.irs_" + this.plugin_count, this.pointerClick.bind(this, "click"));
                this.$cache.shad_to.on("mousedown.irs_" + this.plugin_count, this.pointerClick.bind(this, "click"));
            }

            if (this.options.keyboard) {
                this.$cache.line.on("keydown.irs_" + this.plugin_count, this.key.bind(this, "keyboard"));
            }

            if (is_old_ie) {
                this.$cache.body.on("mouseup.irs_" + this.plugin_count, this.pointerUp.bind(this));
                this.$cache.body.on("mouseleave.irs_" + this.plugin_count, this.pointerUp.bind(this));
            }
        },

        /**
         * Focus with tabIndex
         *
         * @param e {Object} event object
         */
        pointerFocus: function (e) {
            if (!this.target) {
                var x;
                var $handle;

                if (this.options.type === "single") {
                    $handle = this.$cache.single;
                } else {
                    $handle = this.$cache.from;
                }

                x = $handle.offset().left;
                x += ($handle.width() / 2) - 1;

                this.pointerClick("single", {preventDefault: function () {}, pageX: x});
            }
        },

        /**
         * Mousemove or touchmove
         * only for handlers
         *
         * @param e {Object} event object
         */
        pointerMove: function (e) {
            if (!this.dragging) {
                return;
            }

            var x = e.pageX || e.originalEvent.touches && e.originalEvent.touches[0].pageX;
            this.coords.x_pointer = x - this.coords.x_gap;

            this.calc();
        },

        /**
         * Mouseup or touchend
         * only for handlers
         *
         * @param e {Object} event object
         */
        pointerUp: function (e) {
            if (this.current_plugin !== this.plugin_count) {
                return;
            }

            if (this.is_active) {
                this.is_active = false;
            } else {
                return;
            }

            this.$cache.cont.find(".state_hover").removeClass("state_hover");

            this.force_redraw = true;

            if (is_old_ie) {
                $("*").prop("unselectable", false);
            }

            this.updateScene();
            this.restoreOriginalMinInterval();

            // callbacks call
            if ($.contains(this.$cache.cont[0], e.target) || this.dragging) {
                this.callOnFinish();
            }

            this.dragging = false;
        },

        /**
         * Mousedown or touchstart
         * only for handlers
         *
         * @param target {String|null}
         * @param e {Object} event object
         */
        pointerDown: function (target, e) {
            e.preventDefault();
            var x = e.pageX || e.originalEvent.touches && e.originalEvent.touches[0].pageX;
            if (e.button === 2) {
                return;
            }

            if (target === "both") {
                this.setTempMinInterval();
            }

            if (!target) {
                target = this.target || "from";
            }

            this.current_plugin = this.plugin_count;
            this.target = target;

            this.is_active = true;
            this.dragging = true;

            this.coords.x_gap = this.$cache.rs.offset().left;
            this.coords.x_pointer = x - this.coords.x_gap;

            this.calcPointerPercent();
            this.changeLevel(target);

            if (is_old_ie) {
                $("*").prop("unselectable", true);
            }

            this.$cache.line.trigger("focus");

            this.updateScene();
        },

        /**
         * Mousedown or touchstart
         * for other slider elements, like diapason line
         *
         * @param target {String}
         * @param e {Object} event object
         */
        pointerClick: function (target, e) {
            e.preventDefault();
            var x = e.pageX || e.originalEvent.touches && e.originalEvent.touches[0].pageX;
            if (e.button === 2) {
                return;
            }

            this.current_plugin = this.plugin_count;
            this.target = target;

            this.is_click = true;
            this.coords.x_gap = this.$cache.rs.offset().left;
            this.coords.x_pointer = +(x - this.coords.x_gap).toFixed();

            this.force_redraw = true;
            this.calc();

            this.$cache.line.trigger("focus");
        },

        /**
         * Keyborard controls for focused slider
         *
         * @param target {String}
         * @param e {Object} event object
         * @returns {boolean|undefined}
         */
        key: function (target, e) {
            if (this.current_plugin !== this.plugin_count || e.altKey || e.ctrlKey || e.shiftKey || e.metaKey) {
                return;
            }

            switch (e.which) {
                case 83: // W
                case 65: // A
                case 40: // DOWN
                case 37: // LEFT
                    e.preventDefault();
                    this.moveByKey(false);
                    break;

                case 87: // S
                case 68: // D
                case 38: // UP
                case 39: // RIGHT
                    e.preventDefault();
                    this.moveByKey(true);
                    break;
            }

            return true;
        },

        /**
         * Move by key
         *
         * @param right {boolean} direction to move
         */
        moveByKey: function (right) {
            var p = this.coords.p_pointer;
            var p_step = (this.options.max - this.options.min) / 100;
            p_step = this.options.step / p_step;

            if (right) {
                p += p_step;
            } else {
                p -= p_step;
            }

            this.coords.x_pointer = this.toFixed(this.coords.w_rs / 100 * p);
            this.is_key = true;
            this.calc();
        },

        /**
         * Set visibility and content
         * of Min and Max labels
         */
        setMinMax: function () {
            if (!this.options) {
                return;
            }

            if (this.options.hide_min_max) {
                this.$cache.min[0].style.display = "none";
                this.$cache.max[0].style.display = "none";
                return;
            }

            if (this.options.values.length) {
                this.$cache.min.html(this.decorate(this.options.p_values[this.options.min]));
                this.$cache.max.html(this.decorate(this.options.p_values[this.options.max]));
            } else {
                var min_pretty = this._prettify(this.options.min);
                var max_pretty = this._prettify(this.options.max);

                this.result.min_pretty = min_pretty;
                this.result.max_pretty = max_pretty;

                this.$cache.min.html(this.decorate(min_pretty, this.options.min));
                this.$cache.max.html(this.decorate(max_pretty, this.options.max));
            }

            this.labels.w_min = this.$cache.min.outerWidth(false);
            this.labels.w_max = this.$cache.max.outerWidth(false);
        },

        /**
         * Then dragging interval, prevent interval collapsing
         * using min_interval option
         */
        setTempMinInterval: function () {
            var interval = this.result.to - this.result.from;

            if (this.old_min_interval === null) {
                this.old_min_interval = this.options.min_interval;
            }

            this.options.min_interval = interval;
        },

        /**
         * Restore min_interval option to original
         */
        restoreOriginalMinInterval: function () {
            if (this.old_min_interval !== null) {
                this.options.min_interval = this.old_min_interval;
                this.old_min_interval = null;
            }
        },



        // =============================================================================================================
        // Calculations

        /**
         * All calculations and measures start here
         *
         * @param update {boolean=}
         */
        calc: function (update) {
            if (!this.options) {
                return;
            }

            this.calc_count++;

            if (this.calc_count === 10 || update) {
                this.calc_count = 0;
                this.coords.w_rs = this.$cache.rs.outerWidth(false);

                this.calcHandlePercent();
            }

            if (!this.coords.w_rs) {
                return;
            }

            this.calcPointerPercent();
            var handle_x = this.getHandleX();


            if (this.target === "both") {
                this.coords.p_gap = 0;
                handle_x = this.getHandleX();
            }

            if (this.target === "click") {
                this.coords.p_gap = this.coords.p_handle / 2;
                handle_x = this.getHandleX();

                if (this.options.drag_interval) {
                    this.target = "both_one";
                } else {
                    this.target = this.chooseHandle(handle_x);
                }
            }

            switch (this.target) {
                case "base":
                    var w = (this.options.max - this.options.min) / 100,
                        f = (this.result.from - this.options.min) / w,
                        t = (this.result.to - this.options.min) / w;

                    this.coords.p_single_real = this.toFixed(f);
                    this.coords.p_from_real = this.toFixed(f);
                    this.coords.p_to_real = this.toFixed(t);

                    this.coords.p_single_real = this.checkDiapason(this.coords.p_single_real, this.options.from_min, this.options.from_max);
                    this.coords.p_from_real = this.checkDiapason(this.coords.p_from_real, this.options.from_min, this.options.from_max);
                    this.coords.p_to_real = this.checkDiapason(this.coords.p_to_real, this.options.to_min, this.options.to_max);

                    this.coords.p_single_fake = this.convertToFakePercent(this.coords.p_single_real);
                    this.coords.p_from_fake = this.convertToFakePercent(this.coords.p_from_real);
                    this.coords.p_to_fake = this.convertToFakePercent(this.coords.p_to_real);

                    this.target = null;

                    break;

                case "single":
                    if (this.options.from_fixed) {
                        break;
                    }

                    this.coords.p_single_real = this.convertToRealPercent(handle_x);
                    this.coords.p_single_real = this.calcWithStep(this.coords.p_single_real);
                    this.coords.p_single_real = this.checkDiapason(this.coords.p_single_real, this.options.from_min, this.options.from_max);

                    this.coords.p_single_fake = this.convertToFakePercent(this.coords.p_single_real);

                    break;

                case "from":
                    if (this.options.from_fixed) {
                        break;
                    }

                    this.coords.p_from_real = this.convertToRealPercent(handle_x);
                    this.coords.p_from_real = this.calcWithStep(this.coords.p_from_real);
                    if (this.coords.p_from_real > this.coords.p_to_real) {
                        this.coords.p_from_real = this.coords.p_to_real;
                    }
                    this.coords.p_from_real = this.checkDiapason(this.coords.p_from_real, this.options.from_min, this.options.from_max);
                    this.coords.p_from_real = this.checkMinInterval(this.coords.p_from_real, this.coords.p_to_real, "from");
                    this.coords.p_from_real = this.checkMaxInterval(this.coords.p_from_real, this.coords.p_to_real, "from");

                    this.coords.p_from_fake = this.convertToFakePercent(this.coords.p_from_real);

                    break;

                case "to":
                    if (this.options.to_fixed) {
                        break;
                    }

                    this.coords.p_to_real = this.convertToRealPercent(handle_x);
                    this.coords.p_to_real = this.calcWithStep(this.coords.p_to_real);
                    if (this.coords.p_to_real < this.coords.p_from_real) {
                        this.coords.p_to_real = this.coords.p_from_real;
                    }
                    this.coords.p_to_real = this.checkDiapason(this.coords.p_to_real, this.options.to_min, this.options.to_max);
                    this.coords.p_to_real = this.checkMinInterval(this.coords.p_to_real, this.coords.p_from_real, "to");
                    this.coords.p_to_real = this.checkMaxInterval(this.coords.p_to_real, this.coords.p_from_real, "to");

                    this.coords.p_to_fake = this.convertToFakePercent(this.coords.p_to_real);

                    break;

                case "both":
                    if (this.options.from_fixed || this.options.to_fixed) {
                        break;
                    }

                    handle_x = this.toFixed(handle_x + (this.coords.p_handle * 0.001));

                    this.coords.p_from_real = this.convertToRealPercent(handle_x) - this.coords.p_gap_left;
                    this.coords.p_from_real = this.calcWithStep(this.coords.p_from_real);
                    this.coords.p_from_real = this.checkDiapason(this.coords.p_from_real, this.options.from_min, this.options.from_max);
                    this.coords.p_from_real = this.checkMinInterval(this.coords.p_from_real, this.coords.p_to_real, "from");
                    this.coords.p_from_fake = this.convertToFakePercent(this.coords.p_from_real);

                    this.coords.p_to_real = this.convertToRealPercent(handle_x) + this.coords.p_gap_right;
                    this.coords.p_to_real = this.calcWithStep(this.coords.p_to_real);
                    this.coords.p_to_real = this.checkDiapason(this.coords.p_to_real, this.options.to_min, this.options.to_max);
                    this.coords.p_to_real = this.checkMinInterval(this.coords.p_to_real, this.coords.p_from_real, "to");
                    this.coords.p_to_fake = this.convertToFakePercent(this.coords.p_to_real);

                    break;

                case "both_one":
                    if (this.options.from_fixed || this.options.to_fixed) {
                        break;
                    }

                    var real_x = this.convertToRealPercent(handle_x),
                        from = this.result.from_percent,
                        to = this.result.to_percent,
                        full = to - from,
                        half = full / 2,
                        new_from = real_x - half,
                        new_to = real_x + half;

                    if (new_from < 0) {
                        new_from = 0;
                        new_to = new_from + full;
                    }

                    if (new_to > 100) {
                        new_to = 100;
                        new_from = new_to - full;
                    }

                    this.coords.p_from_real = this.calcWithStep(new_from);
                    this.coords.p_from_real = this.checkDiapason(this.coords.p_from_real, this.options.from_min, this.options.from_max);
                    this.coords.p_from_fake = this.convertToFakePercent(this.coords.p_from_real);

                    this.coords.p_to_real = this.calcWithStep(new_to);
                    this.coords.p_to_real = this.checkDiapason(this.coords.p_to_real, this.options.to_min, this.options.to_max);
                    this.coords.p_to_fake = this.convertToFakePercent(this.coords.p_to_real);

                    break;
            }

            if (this.options.type === "single") {
                this.coords.p_bar_x = (this.coords.p_handle / 2);
                this.coords.p_bar_w = this.coords.p_single_fake;

                this.result.from_percent = this.coords.p_single_real;
                this.result.from = this.convertToValue(this.coords.p_single_real);
                this.result.from_pretty = this._prettify(this.result.from);

                if (this.options.values.length) {
                    this.result.from_value = this.options.values[this.result.from];
                }
            } else {
                this.coords.p_bar_x = this.toFixed(this.coords.p_from_fake + (this.coords.p_handle / 2));
                this.coords.p_bar_w = this.toFixed(this.coords.p_to_fake - this.coords.p_from_fake);

                this.result.from_percent = this.coords.p_from_real;
                this.result.from = this.convertToValue(this.coords.p_from_real);
                this.result.from_pretty = this._prettify(this.result.from);
                this.result.to_percent = this.coords.p_to_real;
                this.result.to = this.convertToValue(this.coords.p_to_real);
                this.result.to_pretty = this._prettify(this.result.to);

                if (this.options.values.length) {
                    this.result.from_value = this.options.values[this.result.from];
                    this.result.to_value = this.options.values[this.result.to];
                }
            }

            this.calcMinMax();
            this.calcLabels();
        },


        /**
         * calculates pointer X in percent
         */
        calcPointerPercent: function () {
            if (!this.coords.w_rs) {
                this.coords.p_pointer = 0;
                return;
            }

            if (this.coords.x_pointer < 0 || isNaN(this.coords.x_pointer)  ) {
                this.coords.x_pointer = 0;
            } else if (this.coords.x_pointer > this.coords.w_rs) {
                this.coords.x_pointer = this.coords.w_rs;
            }

            this.coords.p_pointer = this.toFixed(this.coords.x_pointer / this.coords.w_rs * 100);
        },

        convertToRealPercent: function (fake) {
            var full = 100 - this.coords.p_handle;
            return fake / full * 100;
        },

        convertToFakePercent: function (real) {
            var full = 100 - this.coords.p_handle;
            return real / 100 * full;
        },

        getHandleX: function () {
            var max = 100 - this.coords.p_handle,
                x = this.toFixed(this.coords.p_pointer - this.coords.p_gap);

            if (x < 0) {
                x = 0;
            } else if (x > max) {
                x = max;
            }

            return x;
        },

        calcHandlePercent: function () {
            if (this.options.type === "single") {
                this.coords.w_handle = this.$cache.s_single.outerWidth(false);
            } else {
                this.coords.w_handle = this.$cache.s_from.outerWidth(false);
            }

            this.coords.p_handle = this.toFixed(this.coords.w_handle / this.coords.w_rs * 100);
        },

        /**
         * Find closest handle to pointer click
         *
         * @param real_x {Number}
         * @returns {String}
         */
        chooseHandle: function (real_x) {
            if (this.options.type === "single") {
                return "single";
            } else {
                var m_point = this.coords.p_from_real + ((this.coords.p_to_real - this.coords.p_from_real) / 2);
                if (real_x >= m_point) {
                    return this.options.to_fixed ? "from" : "to";
                } else {
                    return this.options.from_fixed ? "to" : "from";
                }
            }
        },

        /**
         * Measure Min and Max labels width in percent
         */
        calcMinMax: function () {
            if (!this.coords.w_rs) {
                return;
            }

            this.labels.p_min = this.labels.w_min / this.coords.w_rs * 100;
            this.labels.p_max = this.labels.w_max / this.coords.w_rs * 100;
        },

        /**
         * Measure labels width and X in percent
         */
        calcLabels: function () {
            if (!this.coords.w_rs || this.options.hide_from_to) {
                return;
            }

            if (this.options.type === "single") {

                this.labels.w_single = this.$cache.single.outerWidth(false);
                this.labels.p_single_fake = this.labels.w_single / this.coords.w_rs * 100;
                this.labels.p_single_left = this.coords.p_single_fake + (this.coords.p_handle / 2) - (this.labels.p_single_fake / 2);
                this.labels.p_single_left = this.checkEdges(this.labels.p_single_left, this.labels.p_single_fake);

            } else {

                this.labels.w_from = this.$cache.from.outerWidth(false);
                this.labels.p_from_fake = this.labels.w_from / this.coords.w_rs * 100;
                this.labels.p_from_left = this.coords.p_from_fake + (this.coords.p_handle / 2) - (this.labels.p_from_fake / 2);
                this.labels.p_from_left = this.toFixed(this.labels.p_from_left);
                this.labels.p_from_left = this.checkEdges(this.labels.p_from_left, this.labels.p_from_fake);

                this.labels.w_to = this.$cache.to.outerWidth(false);
                this.labels.p_to_fake = this.labels.w_to / this.coords.w_rs * 100;
                this.labels.p_to_left = this.coords.p_to_fake + (this.coords.p_handle / 2) - (this.labels.p_to_fake / 2);
                this.labels.p_to_left = this.toFixed(this.labels.p_to_left);
                this.labels.p_to_left = this.checkEdges(this.labels.p_to_left, this.labels.p_to_fake);

                this.labels.w_single = this.$cache.single.outerWidth(false);
                this.labels.p_single_fake = this.labels.w_single / this.coords.w_rs * 100;
                this.labels.p_single_left = ((this.labels.p_from_left + this.labels.p_to_left + this.labels.p_to_fake) / 2) - (this.labels.p_single_fake / 2);
                this.labels.p_single_left = this.toFixed(this.labels.p_single_left);
                this.labels.p_single_left = this.checkEdges(this.labels.p_single_left, this.labels.p_single_fake);

            }
        },



        // =============================================================================================================
        // Drawings

        /**
         * Main function called in request animation frame
         * to update everything
         */
        updateScene: function () {
            if (this.raf_id) {
                cancelAnimationFrame(this.raf_id);
                this.raf_id = null;
            }

            clearTimeout(this.update_tm);
            this.update_tm = null;

            if (!this.options) {
                return;
            }

            this.drawHandles();

            if (this.is_active) {
                this.raf_id = requestAnimationFrame(this.updateScene.bind(this));
            } else {
                this.update_tm = setTimeout(this.updateScene.bind(this), 300);
            }
        },

        /**
         * Draw handles
         */
        drawHandles: function () {
            this.coords.w_rs = this.$cache.rs.outerWidth(false);

            if (!this.coords.w_rs) {
                return;
            }

            if (this.coords.w_rs !== this.coords.w_rs_old) {
                this.target = "base";
                this.is_resize = true;
            }

            if (this.coords.w_rs !== this.coords.w_rs_old || this.force_redraw) {
                this.setMinMax();
                this.calc(true);
                this.drawLabels();
                if (this.options.grid) {
                    this.calcGridMargin();
                    this.calcGridLabels();
                }
                this.force_redraw = true;
                this.coords.w_rs_old = this.coords.w_rs;
                this.drawShadow();
            }

            if (!this.coords.w_rs) {
                return;
            }

            if (!this.dragging && !this.force_redraw && !this.is_key) {
                return;
            }

            if (this.old_from !== this.result.from || this.old_to !== this.result.to || this.force_redraw || this.is_key) {

                this.drawLabels();

                this.$cache.bar[0].style.left = this.coords.p_bar_x + "%";
                this.$cache.bar[0].style.width = this.coords.p_bar_w + "%";

                if (this.options.type === "single") {
                    this.$cache.bar[0].style.left = 0;
                    this.$cache.bar[0].style.width = this.coords.p_bar_w + this.coords.p_bar_x + "%";

                    this.$cache.s_single[0].style.left = this.coords.p_single_fake + "%";

                    this.$cache.single[0].style.left = this.labels.p_single_left + "%";
                } else {
                    this.$cache.s_from[0].style.left = this.coords.p_from_fake + "%";
                    this.$cache.s_to[0].style.left = this.coords.p_to_fake + "%";

                    if (this.old_from !== this.result.from || this.force_redraw) {
                        this.$cache.from[0].style.left = this.labels.p_from_left + "%";
                    }
                    if (this.old_to !== this.result.to || this.force_redraw) {
                        this.$cache.to[0].style.left = this.labels.p_to_left + "%";
                    }

                    this.$cache.single[0].style.left = this.labels.p_single_left + "%";
                }

                this.writeToInput();

                if ((this.old_from !== this.result.from || this.old_to !== this.result.to) && !this.is_start) {
                    this.$cache.input.trigger("change");
                    this.$cache.input.trigger("input");
                }

                this.old_from = this.result.from;
                this.old_to = this.result.to;

                // callbacks call
                if (!this.is_resize && !this.is_update && !this.is_start && !this.is_finish) {
                    this.callOnChange();
                }
                if (this.is_key || this.is_click) {
                    this.is_key = false;
                    this.is_click = false;
                    this.callOnFinish();
                }

                this.is_update = false;
                this.is_resize = false;
                this.is_finish = false;
            }

            this.is_start = false;
            this.is_key = false;
            this.is_click = false;
            this.force_redraw = false;
        },

        /**
         * Draw labels
         * measure labels collisions
         * collapse close labels
         */
        drawLabels: function () {
            if (!this.options) {
                return;
            }

            var values_num = this.options.values.length;
            var p_values = this.options.p_values;
            var text_single;
            var text_from;
            var text_to;
            var from_pretty;
            var to_pretty;

            if (this.options.hide_from_to) {
                return;
            }

            if (this.options.type === "single") {

                if (values_num) {
                    text_single = this.decorate(p_values[this.result.from]);
                    this.$cache.single.html(text_single);
                } else {
                    from_pretty = this._prettify(this.result.from);

                    text_single = this.decorate(from_pretty, this.result.from);
                    this.$cache.single.html(text_single);
                }

                this.calcLabels();

                if (this.labels.p_single_left < this.labels.p_min + 1) {
                    this.$cache.min[0].style.visibility = "hidden";
                } else {
                    this.$cache.min[0].style.visibility = "visible";
                }

                if (this.labels.p_single_left + this.labels.p_single_fake > 100 - this.labels.p_max - 1) {
                    this.$cache.max[0].style.visibility = "hidden";
                } else {
                    this.$cache.max[0].style.visibility = "visible";
                }

            } else {

                if (values_num) {

                    if (this.options.decorate_both) {
                        text_single = this.decorate(p_values[this.result.from]);
                        text_single += this.options.values_separator;
                        text_single += this.decorate(p_values[this.result.to]);
                    } else {
                        text_single = this.decorate(p_values[this.result.from] + this.options.values_separator + p_values[this.result.to]);
                    }
                    text_from = this.decorate(p_values[this.result.from]);
                    text_to = this.decorate(p_values[this.result.to]);

                    this.$cache.single.html(text_single);
                    this.$cache.from.html(text_from);
                    this.$cache.to.html(text_to);

                } else {
                    from_pretty = this._prettify(this.result.from);
                    to_pretty = this._prettify(this.result.to);

                    if (this.options.decorate_both) {
                        text_single = this.decorate(from_pretty, this.result.from);
                        text_single += this.options.values_separator;
                        text_single += this.decorate(to_pretty, this.result.to);
                    } else {
                        text_single = this.decorate(from_pretty + this.options.values_separator + to_pretty, this.result.to);
                    }
                    text_from = this.decorate(from_pretty, this.result.from);
                    text_to = this.decorate(to_pretty, this.result.to);

                    this.$cache.single.html(text_single);
                    this.$cache.from.html(text_from);
                    this.$cache.to.html(text_to);

                }

                this.calcLabels();

                var min = Math.min(this.labels.p_single_left, this.labels.p_from_left),
                    single_left = this.labels.p_single_left + this.labels.p_single_fake,
                    to_left = this.labels.p_to_left + this.labels.p_to_fake,
                    max = Math.max(single_left, to_left);

                if (this.labels.p_from_left + this.labels.p_from_fake >= this.labels.p_to_left) {
                    this.$cache.from[0].style.visibility = "hidden";
                    this.$cache.to[0].style.visibility = "hidden";
                    this.$cache.single[0].style.visibility = "visible";

                    if (this.result.from === this.result.to) {
                        if (this.target === "from") {
                            this.$cache.from[0].style.visibility = "visible";
                        } else if (this.target === "to") {
                            this.$cache.to[0].style.visibility = "visible";
                        } else if (!this.target) {
                            this.$cache.from[0].style.visibility = "visible";
                        }
                        this.$cache.single[0].style.visibility = "hidden";
                        max = to_left;
                    } else {
                        this.$cache.from[0].style.visibility = "hidden";
                        this.$cache.to[0].style.visibility = "hidden";
                        this.$cache.single[0].style.visibility = "visible";
                        max = Math.max(single_left, to_left);
                    }
                } else {
                    this.$cache.from[0].style.visibility = "visible";
                    this.$cache.to[0].style.visibility = "visible";
                    this.$cache.single[0].style.visibility = "hidden";
                }

                if (min < this.labels.p_min + 1) {
                    this.$cache.min[0].style.visibility = "hidden";
                } else {
                    this.$cache.min[0].style.visibility = "visible";
                }

                if (max > 100 - this.labels.p_max - 1) {
                    this.$cache.max[0].style.visibility = "hidden";
                } else {
                    this.$cache.max[0].style.visibility = "visible";
                }

            }
        },

        /**
         * Draw shadow intervals
         */
        drawShadow: function () {
            var o = this.options,
                c = this.$cache,

                is_from_min = typeof o.from_min === "number" && !isNaN(o.from_min),
                is_from_max = typeof o.from_max === "number" && !isNaN(o.from_max),
                is_to_min = typeof o.to_min === "number" && !isNaN(o.to_min),
                is_to_max = typeof o.to_max === "number" && !isNaN(o.to_max),

                from_min,
                from_max,
                to_min,
                to_max;

            if (o.type === "single") {
                if (o.from_shadow && (is_from_min || is_from_max)) {
                    from_min = this.convertToPercent(is_from_min ? o.from_min : o.min);
                    from_max = this.convertToPercent(is_from_max ? o.from_max : o.max) - from_min;
                    from_min = this.toFixed(from_min - (this.coords.p_handle / 100 * from_min));
                    from_max = this.toFixed(from_max - (this.coords.p_handle / 100 * from_max));
                    from_min = from_min + (this.coords.p_handle / 2);

                    c.shad_single[0].style.display = "block";
                    c.shad_single[0].style.left = from_min + "%";
                    c.shad_single[0].style.width = from_max + "%";
                } else {
                    c.shad_single[0].style.display = "none";
                }
            } else {
                if (o.from_shadow && (is_from_min || is_from_max)) {
                    from_min = this.convertToPercent(is_from_min ? o.from_min : o.min);
                    from_max = this.convertToPercent(is_from_max ? o.from_max : o.max) - from_min;
                    from_min = this.toFixed(from_min - (this.coords.p_handle / 100 * from_min));
                    from_max = this.toFixed(from_max - (this.coords.p_handle / 100 * from_max));
                    from_min = from_min + (this.coords.p_handle / 2);

                    c.shad_from[0].style.display = "block";
                    c.shad_from[0].style.left = from_min + "%";
                    c.shad_from[0].style.width = from_max + "%";
                } else {
                    c.shad_from[0].style.display = "none";
                }

                if (o.to_shadow && (is_to_min || is_to_max)) {
                    to_min = this.convertToPercent(is_to_min ? o.to_min : o.min);
                    to_max = this.convertToPercent(is_to_max ? o.to_max : o.max) - to_min;
                    to_min = this.toFixed(to_min - (this.coords.p_handle / 100 * to_min));
                    to_max = this.toFixed(to_max - (this.coords.p_handle / 100 * to_max));
                    to_min = to_min + (this.coords.p_handle / 2);

                    c.shad_to[0].style.display = "block";
                    c.shad_to[0].style.left = to_min + "%";
                    c.shad_to[0].style.width = to_max + "%";
                } else {
                    c.shad_to[0].style.display = "none";
                }
            }
        },



        /**
         * Write values to input element
         */
        writeToInput: function () {
            if (this.options.type === "single") {
                if (this.options.values.length) {
                    this.$cache.input.prop("value", this.result.from_value);
                } else {
                    this.$cache.input.prop("value", this.result.from);
                }
                this.$cache.input.data("from", this.result.from);
            } else {
                if (this.options.values.length) {
                    this.$cache.input.prop("value", this.result.from_value + this.options.input_values_separator + this.result.to_value);
                } else {
                    this.$cache.input.prop("value", this.result.from + this.options.input_values_separator + this.result.to);
                }
                this.$cache.input.data("from", this.result.from);
                this.$cache.input.data("to", this.result.to);
            }
        },



        // =============================================================================================================
        // Callbacks

        callOnStart: function () {
            this.writeToInput();

            if (this.options.onStart && typeof this.options.onStart === "function") {
                if (this.options.scope) {
                    this.options.onStart.call(this.options.scope, this.result);
                } else {
                    this.options.onStart(this.result);
                }
            }
        },
        callOnChange: function () {
            this.writeToInput();

            if (this.options.onChange && typeof this.options.onChange === "function") {
                if (this.options.scope) {
                    this.options.onChange.call(this.options.scope, this.result);
                } else {
                    this.options.onChange(this.result);
                }
            }
        },
        callOnFinish: function () {
            this.writeToInput();

            if (this.options.onFinish && typeof this.options.onFinish === "function") {
                if (this.options.scope) {
                    this.options.onFinish.call(this.options.scope, this.result);
                } else {
                    this.options.onFinish(this.result);
                }
            }
        },
        callOnUpdate: function () {
            this.writeToInput();

            if (this.options.onUpdate && typeof this.options.onUpdate === "function") {
                if (this.options.scope) {
                    this.options.onUpdate.call(this.options.scope, this.result);
                } else {
                    this.options.onUpdate(this.result);
                }
            }
        },




        // =============================================================================================================
        // Service methods

        toggleInput: function () {
            this.$cache.input.toggleClass("irs-hidden-input");

            if (this.has_tab_index) {
                this.$cache.input.prop("tabindex", -1);
            } else {
                this.$cache.input.removeProp("tabindex");
            }

            this.has_tab_index = !this.has_tab_index;
        },

        /**
         * Convert real value to percent
         *
         * @param value {Number} X in real
         * @param no_min {boolean=} don't use min value
         * @returns {Number} X in percent
         */
        convertToPercent: function (value, no_min) {
            var diapason = this.options.max - this.options.min,
                one_percent = diapason / 100,
                val, percent;

            if (!diapason) {
                this.no_diapason = true;
                return 0;
            }

            if (no_min) {
                val = value;
            } else {
                val = value - this.options.min;
            }

            percent = val / one_percent;

            return this.toFixed(percent);
        },

        /**
         * Convert percent to real values
         *
         * @param percent {Number} X in percent
         * @returns {Number} X in real
         */
        convertToValue: function (percent) {
            var min = this.options.min,
                max = this.options.max,
                min_decimals = min.toString().split(".")[1],
                max_decimals = max.toString().split(".")[1],
                min_length, max_length,
                avg_decimals = 0,
                abs = 0;

            if (percent === 0) {
                return this.options.min;
            }
            if (percent === 100) {
                return this.options.max;
            }


            if (min_decimals) {
                min_length = min_decimals.length;
                avg_decimals = min_length;
            }
            if (max_decimals) {
                max_length = max_decimals.length;
                avg_decimals = max_length;
            }
            if (min_length && max_length) {
                avg_decimals = (min_length >= max_length) ? min_length : max_length;
            }

            if (min < 0) {
                abs = Math.abs(min);
                min = +(min + abs).toFixed(avg_decimals);
                max = +(max + abs).toFixed(avg_decimals);
            }

            var number = ((max - min) / 100 * percent) + min,
                string = this.options.step.toString().split(".")[1],
                result;

            if (string) {
                number = +number.toFixed(string.length);
            } else {
                number = number / this.options.step;
                number = number * this.options.step;

                number = +number.toFixed(0);
            }

            if (abs) {
                number -= abs;
            }

            if (string) {
                result = +number.toFixed(string.length);
            } else {
                result = this.toFixed(number);
            }

            if (result < this.options.min) {
                result = this.options.min;
            } else if (result > this.options.max) {
                result = this.options.max;
            }

            return result;
        },

        /**
         * Round percent value with step
         *
         * @param percent {Number}
         * @returns percent {Number} rounded
         */
        calcWithStep: function (percent) {
            var rounded = Math.round(percent / this.coords.p_step) * this.coords.p_step;

            if (rounded > 100) {
                rounded = 100;
            }
            if (percent === 100) {
                rounded = 100;
            }

            return this.toFixed(rounded);
        },

        checkMinInterval: function (p_current, p_next, type) {
            var o = this.options,
                current,
                next;

            if (!o.min_interval) {
                return p_current;
            }

            current = this.convertToValue(p_current);
            next = this.convertToValue(p_next);

            if (type === "from") {

                if (next - current < o.min_interval) {
                    current = next - o.min_interval;
                }

            } else {

                if (current - next < o.min_interval) {
                    current = next + o.min_interval;
                }

            }

            return this.convertToPercent(current);
        },

        checkMaxInterval: function (p_current, p_next, type) {
            var o = this.options,
                current,
                next;

            if (!o.max_interval) {
                return p_current;
            }

            current = this.convertToValue(p_current);
            next = this.convertToValue(p_next);

            if (type === "from") {

                if (next - current > o.max_interval) {
                    current = next - o.max_interval;
                }

            } else {

                if (current - next > o.max_interval) {
                    current = next + o.max_interval;
                }

            }

            return this.convertToPercent(current);
        },

        checkDiapason: function (p_num, min, max) {
            var num = this.convertToValue(p_num),
                o = this.options;

            if (typeof min !== "number") {
                min = o.min;
            }

            if (typeof max !== "number") {
                max = o.max;
            }

            if (num < min) {
                num = min;
            }

            if (num > max) {
                num = max;
            }

            return this.convertToPercent(num);
        },

        toFixed: function (num) {
            num = num.toFixed(20);
            return +num;
        },

        _prettify: function (num) {
            if (!this.options.prettify_enabled) {
                return num;
            }

            if (this.options.prettify && typeof this.options.prettify === "function") {
                return this.options.prettify(num);
            } else {
                return this.prettify(num);
            }
        },

        prettify: function (num) {
            var n = num.toString();
            return n.replace(/(\d{1,3}(?=(?:\d\d\d)+(?!\d)))/g, "$1" + this.options.prettify_separator);
        },

        checkEdges: function (left, width) {
            if (!this.options.force_edges) {
                return this.toFixed(left);
            }

            if (left < 0) {
                left = 0;
            } else if (left > 100 - width) {
                left = 100 - width;
            }

            return this.toFixed(left);
        },

        validate: function () {
            var o = this.options,
                r = this.result,
                v = o.values,
                vl = v.length,
                value,
                i;

            if (typeof o.min === "string") o.min = +o.min;
            if (typeof o.max === "string") o.max = +o.max;
            if (typeof o.from === "string") o.from = +o.from;
            if (typeof o.to === "string") o.to = +o.to;
            if (typeof o.step === "string") o.step = +o.step;

            if (typeof o.from_min === "string") o.from_min = +o.from_min;
            if (typeof o.from_max === "string") o.from_max = +o.from_max;
            if (typeof o.to_min === "string") o.to_min = +o.to_min;
            if (typeof o.to_max === "string") o.to_max = +o.to_max;

            if (typeof o.grid_num === "string") o.grid_num = +o.grid_num;

            if (o.max < o.min) {
                o.max = o.min;
            }

            if (vl) {
                o.p_values = [];
                o.min = 0;
                o.max = vl - 1;
                o.step = 1;
                o.grid_num = o.max;
                o.grid_snap = true;

                for (i = 0; i < vl; i++) {
                    value = +v[i];

                    if (!isNaN(value)) {
                        v[i] = value;
                        value = this._prettify(value);
                    } else {
                        value = v[i];
                    }

                    o.p_values.push(value);
                }
            }

            if (typeof o.from !== "number" || isNaN(o.from)) {
                o.from = o.min;
            }

            if (typeof o.to !== "number" || isNaN(o.to)) {
                o.to = o.max;
            }

            if (o.type === "single") {

                if (o.from < o.min) o.from = o.min;
                if (o.from > o.max) o.from = o.max;

            } else {

                if (o.from < o.min) o.from = o.min;
                if (o.from > o.max) o.from = o.max;

                if (o.to < o.min) o.to = o.min;
                if (o.to > o.max) o.to = o.max;

                if (this.update_check.from) {

                    if (this.update_check.from !== o.from) {
                        if (o.from > o.to) o.from = o.to;
                    }
                    if (this.update_check.to !== o.to) {
                        if (o.to < o.from) o.to = o.from;
                    }

                }

                if (o.from > o.to) o.from = o.to;
                if (o.to < o.from) o.to = o.from;

            }

            if (typeof o.step !== "number" || isNaN(o.step) || !o.step || o.step < 0) {
                o.step = 1;
            }

            if (typeof o.from_min === "number" && o.from < o.from_min) {
                o.from = o.from_min;
            }

            if (typeof o.from_max === "number" && o.from > o.from_max) {
                o.from = o.from_max;
            }

            if (typeof o.to_min === "number" && o.to < o.to_min) {
                o.to = o.to_min;
            }

            if (typeof o.to_max === "number" && o.from > o.to_max) {
                o.to = o.to_max;
            }

            if (r) {
                if (r.min !== o.min) {
                    r.min = o.min;
                }

                if (r.max !== o.max) {
                    r.max = o.max;
                }

                if (r.from < r.min || r.from > r.max) {
                    r.from = o.from;
                }

                if (r.to < r.min || r.to > r.max) {
                    r.to = o.to;
                }
            }

            if (typeof o.min_interval !== "number" || isNaN(o.min_interval) || !o.min_interval || o.min_interval < 0) {
                o.min_interval = 0;
            }

            if (typeof o.max_interval !== "number" || isNaN(o.max_interval) || !o.max_interval || o.max_interval < 0) {
                o.max_interval = 0;
            }

            if (o.min_interval && o.min_interval > o.max - o.min) {
                o.min_interval = o.max - o.min;
            }

            if (o.max_interval && o.max_interval > o.max - o.min) {
                o.max_interval = o.max - o.min;
            }
        },

        decorate: function (num, original) {
            var decorated = "",
                o = this.options;

            if (o.prefix) {
                decorated += o.prefix;
            }

            decorated += num;

            if (o.max_postfix) {
                if (o.values.length && num === o.p_values[o.max]) {
                    decorated += o.max_postfix;
                    if (o.postfix) {
                        decorated += " ";
                    }
                } else if (original === o.max) {
                    decorated += o.max_postfix;
                    if (o.postfix) {
                        decorated += " ";
                    }
                }
            }

            if (o.postfix) {
                decorated += o.postfix;
            }

            return decorated;
        },

        updateFrom: function () {
            this.result.from = this.options.from;
            this.result.from_percent = this.convertToPercent(this.result.from);
            this.result.from_pretty = this._prettify(this.result.from);
            if (this.options.values) {
                this.result.from_value = this.options.values[this.result.from];
            }
        },

        updateTo: function () {
            this.result.to = this.options.to;
            this.result.to_percent = this.convertToPercent(this.result.to);
            this.result.to_pretty = this._prettify(this.result.to);
            if (this.options.values) {
                this.result.to_value = this.options.values[this.result.to];
            }
        },

        updateResult: function () {
            this.result.min = this.options.min;
            this.result.max = this.options.max;
            this.updateFrom();
            this.updateTo();
        },


        // =============================================================================================================
        // Grid

        appendGrid: function () {
            if (!this.options.grid) {
                return;
            }

            var o = this.options,
                i, z,

                total = o.max - o.min,
                big_num = o.grid_num,
                big_p = 0,
                big_w = 0,

                small_max = 4,
                local_small_max,
                small_p,
                small_w = 0,

                result,
                html = '';



            this.calcGridMargin();

            if (o.grid_snap) {
                big_num = total / o.step;
            }

            if (big_num > 50) big_num = 50;
            big_p = this.toFixed(100 / big_num);

            if (big_num > 4) {
                small_max = 3;
            }
            if (big_num > 7) {
                small_max = 2;
            }
            if (big_num > 14) {
                small_max = 1;
            }
            if (big_num > 28) {
                small_max = 0;
            }

            for (i = 0; i < big_num + 1; i++) {
                local_small_max = small_max;

                big_w = this.toFixed(big_p * i);

                if (big_w > 100) {
                    big_w = 100;
                }
                this.coords.big[i] = big_w;

                small_p = (big_w - (big_p * (i - 1))) / (local_small_max + 1);

                for (z = 1; z <= local_small_max; z++) {
                    if (big_w === 0) {
                        break;
                    }

                    small_w = this.toFixed(big_w - (small_p * z));

                    html += '<span class="irs-grid-pol small" style="left: ' + small_w + '%"></span>';
                }

                html += '<span class="irs-grid-pol" style="left: ' + big_w + '%"></span>';

                result = this.convertToValue(big_w);
                if (o.values.length) {
                    result = o.p_values[result];
                } else {
                    result = this._prettify(result);
                }

                html += '<span class="irs-grid-text js-grid-text-' + i + '" style="left: ' + big_w + '%">' + result + '</span>';
            }
            this.coords.big_num = Math.ceil(big_num + 1);



            this.$cache.cont.addClass("irs-with-grid");
            this.$cache.grid.html(html);
            this.cacheGridLabels();
        },

        cacheGridLabels: function () {
            var $label, i,
                num = this.coords.big_num;

            for (i = 0; i < num; i++) {
                $label = this.$cache.grid.find(".js-grid-text-" + i);
                this.$cache.grid_labels.push($label);
            }

            this.calcGridLabels();
        },

        calcGridLabels: function () {
            var i, label, start = [], finish = [],
                num = this.coords.big_num;

            for (i = 0; i < num; i++) {
                this.coords.big_w[i] = this.$cache.grid_labels[i].outerWidth(false);
                this.coords.big_p[i] = this.toFixed(this.coords.big_w[i] / this.coords.w_rs * 100);
                this.coords.big_x[i] = this.toFixed(this.coords.big_p[i] / 2);

                start[i] = this.toFixed(this.coords.big[i] - this.coords.big_x[i]);
                finish[i] = this.toFixed(start[i] + this.coords.big_p[i]);
            }

            if (this.options.force_edges) {
                if (start[0] < -this.coords.grid_gap) {
                    start[0] = -this.coords.grid_gap;
                    finish[0] = this.toFixed(start[0] + this.coords.big_p[0]);

                    this.coords.big_x[0] = this.coords.grid_gap;
                }

                if (finish[num - 1] > 100 + this.coords.grid_gap) {
                    finish[num - 1] = 100 + this.coords.grid_gap;
                    start[num - 1] = this.toFixed(finish[num - 1] - this.coords.big_p[num - 1]);

                    this.coords.big_x[num - 1] = this.toFixed(this.coords.big_p[num - 1] - this.coords.grid_gap);
                }
            }

            this.calcGridCollision(2, start, finish);
            this.calcGridCollision(4, start, finish);

            for (i = 0; i < num; i++) {
                label = this.$cache.grid_labels[i][0];

                if (this.coords.big_x[i] !== Number.POSITIVE_INFINITY) {
                    label.style.marginLeft = -this.coords.big_x[i] + "%";
                }
            }
        },

        // Collisions Calc Beta
        // TODO: Refactor then have plenty of time
        calcGridCollision: function (step, start, finish) {
            var i, next_i, label,
                num = this.coords.big_num;

            for (i = 0; i < num; i += step) {
                next_i = i + (step / 2);
                if (next_i >= num) {
                    break;
                }

                label = this.$cache.grid_labels[next_i][0];

                if (finish[i] <= start[next_i]) {
                    label.style.visibility = "visible";
                } else {
                    label.style.visibility = "hidden";
                }
            }
        },

        calcGridMargin: function () {
            if (!this.options.grid_margin) {
                return;
            }

            this.coords.w_rs = this.$cache.rs.outerWidth(false);
            if (!this.coords.w_rs) {
                return;
            }

            if (this.options.type === "single") {
                this.coords.w_handle = this.$cache.s_single.outerWidth(false);
            } else {
                this.coords.w_handle = this.$cache.s_from.outerWidth(false);
            }
            this.coords.p_handle = this.toFixed(this.coords.w_handle  / this.coords.w_rs * 100);
            this.coords.grid_gap = this.toFixed((this.coords.p_handle / 2) - 0.1);

            this.$cache.grid[0].style.width = this.toFixed(100 - this.coords.p_handle) + "%";
            this.$cache.grid[0].style.left = this.coords.grid_gap + "%";
        },



        // =============================================================================================================
        // Public methods

        update: function (options) {
            if (!this.input) {
                return;
            }

            this.is_update = true;

            this.options.from = this.result.from;
            this.options.to = this.result.to;
            this.update_check.from = this.result.from;
            this.update_check.to = this.result.to;

            this.options = $.extend(this.options, options);
            this.validate();
            this.updateResult(options);

            this.toggleInput();
            this.remove();
            this.init(true);
        },

        reset: function () {
            if (!this.input) {
                return;
            }

            this.updateResult();
            this.update();
        },

        destroy: function () {
            if (!this.input) {
                return;
            }

            this.toggleInput();
            this.$cache.input.prop("readonly", false);
            $.data(this.input, "ionRangeSlider", null);

            this.remove();
            this.input = null;
            this.options = null;
        }
    };

    $.fn.ionRangeSlider = function (options) {
        return this.each(function() {
            if (!$.data(this, "ionRangeSlider")) {
                $.data(this, "ionRangeSlider", new IonRangeSlider(this, options, plugin_count++));
            }
        });
    };



    // =================================================================================================================
    // http://paulirish.com/2011/requestanimationframe-for-smart-animating/
    // http://my.opera.com/emoller/blog/2011/12/20/requestanimationframe-for-smart-er-animating

    // requestAnimationFrame polyfill by Erik Möller. fixes from Paul Irish and Tino Zijdel

    // MIT license

    (function() {
        var lastTime = 0;
        var vendors = ['ms', 'moz', 'webkit', 'o'];
        for(var x = 0; x < vendors.length && !window.requestAnimationFrame; ++x) {
            window.requestAnimationFrame = window[vendors[x]+'RequestAnimationFrame'];
            window.cancelAnimationFrame = window[vendors[x]+'CancelAnimationFrame']
                || window[vendors[x]+'CancelRequestAnimationFrame'];
        }

        if (!window.requestAnimationFrame)
            window.requestAnimationFrame = function(callback, element) {
                var currTime = new Date().getTime();
                var timeToCall = Math.max(0, 16 - (currTime - lastTime));
                var id = window.setTimeout(function() { callback(currTime + timeToCall); },
                    timeToCall);
                lastTime = currTime + timeToCall;
                return id;
            };

        if (!window.cancelAnimationFrame)
            window.cancelAnimationFrame = function(id) {
                clearTimeout(id);
            };
    }());

}));

/*!
 * Select2 4.1.0
 * https://select2.github.io
 *
 * Released under the MIT license
 * https://github.com/select2/select2/blob/master/LICENSE.md
 */
;(function (factory) {
  if (typeof define === 'function' && define.amd) {
    // AMD. Register as an anonymous module.
    define(['jquery'], factory);
  } else if (typeof module === 'object' && module.exports) {
    // Node/CommonJS
    module.exports = function (root, jQuery) {
      if (jQuery === undefined) {
        // require('jQuery') returns a factory that requires window to
        // build a jQuery instance, we normalize how we use modules
        // that require this pattern but the window provided is a noop
        // if it's defined (how jquery works)
        if (typeof window !== 'undefined') {
          jQuery = require('jquery');
        }
        else {
          jQuery = require('jquery')(root);
        }
      }
      factory(jQuery);
      return jQuery;
    };
  } else {
    // Browser globals
    factory(jQuery);
  }
} (function (jQuery) {
  // This is needed so we can catch the AMD loader configuration and use it
  // The inner file should be wrapped (by `banner.start.js`) in a function that
  // returns the AMD loader references.
  var S2 =(function () {
  // Restore the Select2 AMD loader so it can be used
  // Needed mostly in the language files, where the loader is not inserted
  if (jQuery && jQuery.fn && jQuery.fn.select2 && jQuery.fn.select2.amd) {
    var S2 = jQuery.fn.select2.amd;
  }
var S2;(function () { if (!S2 || !S2.requirejs) {
if (!S2) { S2 = {}; } else { require = S2; }
/**
 * @license almond 0.3.3 Copyright jQuery Foundation and other contributors.
 * Released under MIT license, http://github.com/requirejs/almond/LICENSE
 */
//Going sloppy to avoid 'use strict' string cost, but strict practices should
//be followed.
/*global setTimeout: false */

var requirejs, require, define;
(function (undef) {
    var main, req, makeMap, handlers,
        defined = {},
        waiting = {},
        config = {},
        defining = {},
        hasOwn = Object.prototype.hasOwnProperty,
        aps = [].slice,
        jsSuffixRegExp = /\.js$/;

    function hasProp(obj, prop) {
        return hasOwn.call(obj, prop);
    }

    /**
     * Given a relative module name, like ./something, normalize it to
     * a real name that can be mapped to a path.
     * @param {String} name the relative name
     * @param {String} baseName a real name that the name arg is relative
     * to.
     * @returns {String} normalized name
     */
    function normalize(name, baseName) {
        var nameParts, nameSegment, mapValue, foundMap, lastIndex,
            foundI, foundStarMap, starI, i, j, part, normalizedBaseParts,
            baseParts = baseName && baseName.split("/"),
            map = config.map,
            starMap = (map && map['*']) || {};

        //Adjust any relative paths.
        if (name) {
            name = name.split('/');
            lastIndex = name.length - 1;

            // If wanting node ID compatibility, strip .js from end
            // of IDs. Have to do this here, and not in nameToUrl
            // because node allows either .js or non .js to map
            // to same file.
            if (config.nodeIdCompat && jsSuffixRegExp.test(name[lastIndex])) {
                name[lastIndex] = name[lastIndex].replace(jsSuffixRegExp, '');
            }

            // Starts with a '.' so need the baseName
            if (name[0].charAt(0) === '.' && baseParts) {
                //Convert baseName to array, and lop off the last part,
                //so that . matches that 'directory' and not name of the baseName's
                //module. For instance, baseName of 'one/two/three', maps to
                //'one/two/three.js', but we want the directory, 'one/two' for
                //this normalization.
                normalizedBaseParts = baseParts.slice(0, baseParts.length - 1);
                name = normalizedBaseParts.concat(name);
            }

            //start trimDots
            for (i = 0; i < name.length; i++) {
                part = name[i];
                if (part === '.') {
                    name.splice(i, 1);
                    i -= 1;
                } else if (part === '..') {
                    // If at the start, or previous value is still ..,
                    // keep them so that when converted to a path it may
                    // still work when converted to a path, even though
                    // as an ID it is less than ideal. In larger point
                    // releases, may be better to just kick out an error.
                    if (i === 0 || (i === 1 && name[2] === '..') || name[i - 1] === '..') {
                        continue;
                    } else if (i > 0) {
                        name.splice(i - 1, 2);
                        i -= 2;
                    }
                }
            }
            //end trimDots

            name = name.join('/');
        }

        //Apply map config if available.
        if ((baseParts || starMap) && map) {
            nameParts = name.split('/');

            for (i = nameParts.length; i > 0; i -= 1) {
                nameSegment = nameParts.slice(0, i).join("/");

                if (baseParts) {
                    //Find the longest baseName segment match in the config.
                    //So, do joins on the biggest to smallest lengths of baseParts.
                    for (j = baseParts.length; j > 0; j -= 1) {
                        mapValue = map[baseParts.slice(0, j).join('/')];

                        //baseName segment has  config, find if it has one for
                        //this name.
                        if (mapValue) {
                            mapValue = mapValue[nameSegment];
                            if (mapValue) {
                                //Match, update name to the new value.
                                foundMap = mapValue;
                                foundI = i;
                                break;
                            }
                        }
                    }
                }

                if (foundMap) {
                    break;
                }

                //Check for a star map match, but just hold on to it,
                //if there is a shorter segment match later in a matching
                //config, then favor over this star map.
                if (!foundStarMap && starMap && starMap[nameSegment]) {
                    foundStarMap = starMap[nameSegment];
                    starI = i;
                }
            }

            if (!foundMap && foundStarMap) {
                foundMap = foundStarMap;
                foundI = starI;
            }

            if (foundMap) {
                nameParts.splice(0, foundI, foundMap);
                name = nameParts.join('/');
            }
        }

        return name;
    }

    function makeRequire(relName, forceSync) {
        return function () {
            //A version of a require function that passes a moduleName
            //value for items that may need to
            //look up paths relative to the moduleName
            var args = aps.call(arguments, 0);

            //If first arg is not require('string'), and there is only
            //one arg, it is the array form without a callback. Insert
            //a null so that the following concat is correct.
            if (typeof args[0] !== 'string' && args.length === 1) {
                args.push(null);
            }
            return req.apply(undef, args.concat([relName, forceSync]));
        };
    }

    function makeNormalize(relName) {
        return function (name) {
            return normalize(name, relName);
        };
    }

    function makeLoad(depName) {
        return function (value) {
            defined[depName] = value;
        };
    }

    function callDep(name) {
        if (hasProp(waiting, name)) {
            var args = waiting[name];
            delete waiting[name];
            defining[name] = true;
            main.apply(undef, args);
        }

        if (!hasProp(defined, name) && !hasProp(defining, name)) {
            throw new Error('No ' + name);
        }
        return defined[name];
    }

    //Turns a plugin!resource to [plugin, resource]
    //with the plugin being undefined if the name
    //did not have a plugin prefix.
    function splitPrefix(name) {
        var prefix,
            index = name ? name.indexOf('!') : -1;
        if (index > -1) {
            prefix = name.substring(0, index);
            name = name.substring(index + 1, name.length);
        }
        return [prefix, name];
    }

    //Creates a parts array for a relName where first part is plugin ID,
    //second part is resource ID. Assumes relName has already been normalized.
    function makeRelParts(relName) {
        return relName ? splitPrefix(relName) : [];
    }

    /**
     * Makes a name map, normalizing the name, and using a plugin
     * for normalization if necessary. Grabs a ref to plugin
     * too, as an optimization.
     */
    makeMap = function (name, relParts) {
        var plugin,
            parts = splitPrefix(name),
            prefix = parts[0],
            relResourceName = relParts[1];

        name = parts[1];

        if (prefix) {
            prefix = normalize(prefix, relResourceName);
            plugin = callDep(prefix);
        }

        //Normalize according
        if (prefix) {
            if (plugin && plugin.normalize) {
                name = plugin.normalize(name, makeNormalize(relResourceName));
            } else {
                name = normalize(name, relResourceName);
            }
        } else {
            name = normalize(name, relResourceName);
            parts = splitPrefix(name);
            prefix = parts[0];
            name = parts[1];
            if (prefix) {
                plugin = callDep(prefix);
            }
        }

        //Using ridiculous property names for space reasons
        return {
            f: prefix ? prefix + '!' + name : name, //fullName
            n: name,
            pr: prefix,
            p: plugin
        };
    };

    function makeConfig(name) {
        return function () {
            return (config && config.config && config.config[name]) || {};
        };
    }

    handlers = {
        require: function (name) {
            return makeRequire(name);
        },
        exports: function (name) {
            var e = defined[name];
            if (typeof e !== 'undefined') {
                return e;
            } else {
                return (defined[name] = {});
            }
        },
        module: function (name) {
            return {
                id: name,
                uri: '',
                exports: defined[name],
                config: makeConfig(name)
            };
        }
    };

    main = function (name, deps, callback, relName) {
        var cjsModule, depName, ret, map, i, relParts,
            args = [],
            callbackType = typeof callback,
            usingExports;

        //Use name if no relName
        relName = relName || name;
        relParts = makeRelParts(relName);

        //Call the callback to define the module, if necessary.
        if (callbackType === 'undefined' || callbackType === 'function') {
            //Pull out the defined dependencies and pass the ordered
            //values to the callback.
            //Default to [require, exports, module] if no deps
            deps = !deps.length && callback.length ? ['require', 'exports', 'module'] : deps;
            for (i = 0; i < deps.length; i += 1) {
                map = makeMap(deps[i], relParts);
                depName = map.f;

                //Fast path CommonJS standard dependencies.
                if (depName === "require") {
                    args[i] = handlers.require(name);
                } else if (depName === "exports") {
                    //CommonJS module spec 1.1
                    args[i] = handlers.exports(name);
                    usingExports = true;
                } else if (depName === "module") {
                    //CommonJS module spec 1.1
                    cjsModule = args[i] = handlers.module(name);
                } else if (hasProp(defined, depName) ||
                           hasProp(waiting, depName) ||
                           hasProp(defining, depName)) {
                    args[i] = callDep(depName);
                } else if (map.p) {
                    map.p.load(map.n, makeRequire(relName, true), makeLoad(depName), {});
                    args[i] = defined[depName];
                } else {
                    throw new Error(name + ' missing ' + depName);
                }
            }

            ret = callback ? callback.apply(defined[name], args) : undefined;

            if (name) {
                //If setting exports via "module" is in play,
                //favor that over return value and exports. After that,
                //favor a non-undefined return value over exports use.
                if (cjsModule && cjsModule.exports !== undef &&
                        cjsModule.exports !== defined[name]) {
                    defined[name] = cjsModule.exports;
                } else if (ret !== undef || !usingExports) {
                    //Use the return value from the function.
                    defined[name] = ret;
                }
            }
        } else if (name) {
            //May just be an object definition for the module. Only
            //worry about defining if have a module name.
            defined[name] = callback;
        }
    };

    requirejs = require = req = function (deps, callback, relName, forceSync, alt) {
        if (typeof deps === "string") {
            if (handlers[deps]) {
                //callback in this case is really relName
                return handlers[deps](callback);
            }
            //Just return the module wanted. In this scenario, the
            //deps arg is the module name, and second arg (if passed)
            //is just the relName.
            //Normalize module name, if it contains . or ..
            return callDep(makeMap(deps, makeRelParts(callback)).f);
        } else if (!deps.splice) {
            //deps is a config object, not an array.
            config = deps;
            if (config.deps) {
                req(config.deps, config.callback);
            }
            if (!callback) {
                return;
            }

            if (callback.splice) {
                //callback is an array, which means it is a dependency list.
                //Adjust args if there are dependencies
                deps = callback;
                callback = relName;
                relName = null;
            } else {
                deps = undef;
            }
        }

        //Support require(['a'])
        callback = callback || function () {};

        //If relName is a function, it is an errback handler,
        //so remove it.
        if (typeof relName === 'function') {
            relName = forceSync;
            forceSync = alt;
        }

        //Simulate async callback;
        if (forceSync) {
            main(undef, deps, callback, relName);
        } else {
            //Using a non-zero value because of concern for what old browsers
            //do, and latest browsers "upgrade" to 4 if lower value is used:
            //http://www.whatwg.org/specs/web-apps/current-work/multipage/timers.html#dom-windowtimers-settimeout:
            //If want a value immediately, use require('id') instead -- something
            //that works in almond on the global level, but not guaranteed and
            //unlikely to work in other AMD implementations.
            setTimeout(function () {
                main(undef, deps, callback, relName);
            }, 4);
        }

        return req;
    };

    /**
     * Just drops the config on the floor, but returns req in case
     * the config return value is used.
     */
    req.config = function (cfg) {
        return req(cfg);
    };

    /**
     * Expose module registry for debugging and tooling
     */
    requirejs._defined = defined;

    define = function (name, deps, callback) {
        if (typeof name !== 'string') {
            throw new Error('See almond README: incorrect module build, no module name');
        }

        //This module may not have dependencies
        if (!deps.splice) {
            //deps is not an array, so probably means
            //an object literal or factory function for
            //the value. Adjust args.
            callback = deps;
            deps = [];
        }

        if (!hasProp(defined, name) && !hasProp(waiting, name)) {
            waiting[name] = [name, deps, callback];
        }
    };

    define.amd = {
        jQuery: true
    };
}());

S2.requirejs = requirejs;S2.require = require;S2.define = define;
}
}());
S2.define("almond", function(){});

/* global jQuery:false, $:false */
S2.define('jquery',[],function () {
  var _$ = jQuery || $;

  if (_$ == null && console && console.error) {
    console.error(
      'Select2: An instance of jQuery or a jQuery-compatible library was not ' +
      'found. Make sure that you are including jQuery before Select2 on your ' +
      'web page.'
    );
  }

  return _$;
});

S2.define('select2/utils',[
  'jquery'
], function ($) {
  var Utils = {};

  Utils.Extend = function (ChildClass, SuperClass) {
    var __hasProp = {}.hasOwnProperty;

    function BaseConstructor () {
      this.constructor = ChildClass;
    }

    for (var key in SuperClass) {
      if (__hasProp.call(SuperClass, key)) {
        ChildClass[key] = SuperClass[key];
      }
    }

    BaseConstructor.prototype = SuperClass.prototype;
    ChildClass.prototype = new BaseConstructor();
    ChildClass.__super__ = SuperClass.prototype;

    return ChildClass;
  };

  function getMethods (theClass) {
    var proto = theClass.prototype;

    var methods = [];

    for (var methodName in proto) {
      var m = proto[methodName];

      if (typeof m !== 'function') {
        continue;
      }

      if (methodName === 'constructor') {
        continue;
      }

      methods.push(methodName);
    }

    return methods;
  }

  Utils.Decorate = function (SuperClass, DecoratorClass) {
    var decoratedMethods = getMethods(DecoratorClass);
    var superMethods = getMethods(SuperClass);

    function DecoratedClass () {
      var unshift = Array.prototype.unshift;

      var argCount = DecoratorClass.prototype.constructor.length;

      var calledConstructor = SuperClass.prototype.constructor;

      if (argCount > 0) {
        unshift.call(arguments, SuperClass.prototype.constructor);

        calledConstructor = DecoratorClass.prototype.constructor;
      }

      calledConstructor.apply(this, arguments);
    }

    DecoratorClass.displayName = SuperClass.displayName;

    function ctr () {
      this.constructor = DecoratedClass;
    }

    DecoratedClass.prototype = new ctr();

    for (var m = 0; m < superMethods.length; m++) {
      var superMethod = superMethods[m];

      DecoratedClass.prototype[superMethod] =
        SuperClass.prototype[superMethod];
    }

    var calledMethod = function (methodName) {
      // Stub out the original method if it's not decorating an actual method
      var originalMethod = function () {};

      if (methodName in DecoratedClass.prototype) {
        originalMethod = DecoratedClass.prototype[methodName];
      }

      var decoratedMethod = DecoratorClass.prototype[methodName];

      return function () {
        var unshift = Array.prototype.unshift;

        unshift.call(arguments, originalMethod);

        return decoratedMethod.apply(this, arguments);
      };
    };

    for (var d = 0; d < decoratedMethods.length; d++) {
      var decoratedMethod = decoratedMethods[d];

      DecoratedClass.prototype[decoratedMethod] = calledMethod(decoratedMethod);
    }

    return DecoratedClass;
  };

  var Observable = function () {
    this.listeners = {};
  };

  Observable.prototype.on = function (event, callback) {
    this.listeners = this.listeners || {};

    if (event in this.listeners) {
      this.listeners[event].push(callback);
    } else {
      this.listeners[event] = [callback];
    }
  };

  Observable.prototype.trigger = function (event) {
    var slice = Array.prototype.slice;
    var params = slice.call(arguments, 1);

    this.listeners = this.listeners || {};

    // Params should always come in as an array
    if (params == null) {
      params = [];
    }

    // If there are no arguments to the event, use a temporary object
    if (params.length === 0) {
      params.push({});
    }

    // Set the `_type` of the first object to the event
    params[0]._type = event;

    if (event in this.listeners) {
      this.invoke(this.listeners[event], slice.call(arguments, 1));
    }

    if ('*' in this.listeners) {
      this.invoke(this.listeners['*'], arguments);
    }
  };

  Observable.prototype.invoke = function (listeners, params) {
    for (var i = 0, len = listeners.length; i < len; i++) {
      listeners[i].apply(this, params);
    }
  };

  Utils.Observable = Observable;

  Utils.generateChars = function (length) {
    var chars = '';

    for (var i = 0; i < length; i++) {
      var randomChar = Math.floor(Math.random() * 36);
      chars += randomChar.toString(36);
    }

    return chars;
  };

  Utils.bind = function (func, context) {
    return function () {
      func.apply(context, arguments);
    };
  };

  Utils._convertData = function (data) {
    for (var originalKey in data) {
      var keys = originalKey.split('-');

      var dataLevel = data;

      if (keys.length === 1) {
        continue;
      }

      for (var k = 0; k < keys.length; k++) {
        var key = keys[k];

        // Lowercase the first letter
        // By default, dash-separated becomes camelCase
        key = key.substring(0, 1).toLowerCase() + key.substring(1);

        if (!(key in dataLevel)) {
          dataLevel[key] = {};
        }

        if (k == keys.length - 1) {
          dataLevel[key] = data[originalKey];
        }

        dataLevel = dataLevel[key];
      }

      delete data[originalKey];
    }

    return data;
  };

  Utils.hasScroll = function (index, el) {
    // Adapted from the function created by @ShadowScripter
    // and adapted by @BillBarry on the Stack Exchange Code Review website.
    // The original code can be found at
    // http://codereview.stackexchange.com/q/13338
    // and was designed to be used with the Sizzle selector engine.

    var $el = $(el);
    var overflowX = el.style.overflowX;
    var overflowY = el.style.overflowY;

    //Check both x and y declarations
    if (overflowX === overflowY &&
        (overflowY === 'hidden' || overflowY === 'visible')) {
      return false;
    }

    if (overflowX === 'scroll' || overflowY === 'scroll') {
      return true;
    }

    return ($el.innerHeight() < el.scrollHeight ||
      $el.innerWidth() < el.scrollWidth);
  };

  Utils.escapeMarkup = function (markup) {
    var replaceMap = {
      '\\': '&#92;',
      '&': '&amp;',
      '<': '&lt;',
      '>': '&gt;',
      '"': '&quot;',
      '\'': '&#39;',
      '/': '&#47;'
    };

    // Do not try to escape the markup if it's not a string
    if (typeof markup !== 'string') {
      return markup;
    }

    return String(markup).replace(/[&<>"'\/\\]/g, function (match) {
      return replaceMap[match];
    });
  };

  // Cache objects in Utils.__cache instead of $.data (see #4346)
  Utils.__cache = {};

  var id = 0;
  Utils.GetUniqueElementId = function (element) {
    // Get a unique element Id. If element has no id,
    // creates a new unique number, stores it in the id
    // attribute and returns the new id with a prefix.
    // If an id already exists, it simply returns it with a prefix.

    var select2Id = element.getAttribute('data-select2-id');

    if (select2Id != null) {
      return select2Id;
    }

    // If element has id, use it.
    if (element.id) {
      select2Id = 'select2-data-' + element.id;
    } else {
      select2Id = 'select2-data-' + (++id).toString() +
        '-' + Utils.generateChars(4);
    }

    element.setAttribute('data-select2-id', select2Id);

    return select2Id;
  };

  Utils.StoreData = function (element, name, value) {
    // Stores an item in the cache for a specified element.
    // name is the cache key.
    var id = Utils.GetUniqueElementId(element);
    if (!Utils.__cache[id]) {
      Utils.__cache[id] = {};
    }

    Utils.__cache[id][name] = value;
  };

  Utils.GetData = function (element, name) {
    // Retrieves a value from the cache by its key (name)
    // name is optional. If no name specified, return
    // all cache items for the specified element.
    // and for a specified element.
    var id = Utils.GetUniqueElementId(element);
    if (name) {
      if (Utils.__cache[id]) {
        if (Utils.__cache[id][name] != null) {
          return Utils.__cache[id][name];
        }
        return $(element).data(name); // Fallback to HTML5 data attribs.
      }
      return $(element).data(name); // Fallback to HTML5 data attribs.
    } else {
      return Utils.__cache[id];
    }
  };

  Utils.RemoveData = function (element) {
    // Removes all cached items for a specified element.
    var id = Utils.GetUniqueElementId(element);
    if (Utils.__cache[id] != null) {
      delete Utils.__cache[id];
    }

    element.removeAttribute('data-select2-id');
  };

  Utils.copyNonInternalCssClasses = function (dest, src) {
    var classes;

    var destinationClasses = dest.getAttribute('class').trim().split(/\s+/);

    destinationClasses = destinationClasses.filter(function (clazz) {
      // Save all Select2 classes
      return clazz.indexOf('select2-') === 0;
    });

    var sourceClasses = src.getAttribute('class').trim().split(/\s+/);

    sourceClasses = sourceClasses.filter(function (clazz) {
      // Only copy non-Select2 classes
      return clazz.indexOf('select2-') !== 0;
    });

    var replacements = destinationClasses.concat(sourceClasses);

    dest.setAttribute('class', replacements.join(' '));
  };

  return Utils;
});

S2.define('select2/results',[
  'jquery',
  './utils'
], function ($, Utils) {
  function Results ($element, options, dataAdapter) {
    this.$element = $element;
    this.data = dataAdapter;
    this.options = options;

    Results.__super__.constructor.call(this);
  }

  Utils.Extend(Results, Utils.Observable);

  Results.prototype.render = function () {
    var $results = $(
      '<ul class="select2-results__options" role="listbox"></ul>'
    );

    if (this.options.get('multiple')) {
      $results.attr('aria-multiselectable', 'true');
    }

    this.$results = $results;

    return $results;
  };

  Results.prototype.clear = function () {
    this.$results.empty();
  };

  Results.prototype.displayMessage = function (params) {
    var escapeMarkup = this.options.get('escapeMarkup');

    this.clear();
    this.hideLoading();

    var $message = $(
      '<li role="alert" aria-live="assertive"' +
      ' class="select2-results__option"></li>'
    );

    var message = this.options.get('translations').get(params.message);

    $message.append(
      escapeMarkup(
        message(params.args)
      )
    );

    $message[0].className += ' select2-results__message';

    this.$results.append($message);
  };

  Results.prototype.hideMessages = function () {
    this.$results.find('.select2-results__message').remove();
  };

  Results.prototype.append = function (data) {
    this.hideLoading();

    var $options = [];

    if (data.results == null || data.results.length === 0) {
      if (this.$results.children().length === 0) {
        this.trigger('results:message', {
          message: 'noResults'
        });
      }

      return;
    }

    data.results = this.sort(data.results);

    for (var d = 0; d < data.results.length; d++) {
      var item = data.results[d];

      var $option = this.option(item);

      $options.push($option);
    }

    this.$results.append($options);
  };

  Results.prototype.position = function ($results, $dropdown) {
    var $resultsContainer = $dropdown.find('.select2-results');
    $resultsContainer.append($results);
  };

  Results.prototype.sort = function (data) {
    var sorter = this.options.get('sorter');

    return sorter(data);
  };

  Results.prototype.highlightFirstItem = function () {
    var $options = this.$results
      .find('.select2-results__option--selectable');

    var $selected = $options.filter('.select2-results__option--selected');

    // Check if there are any selected options
    if ($selected.length > 0) {
      // If there are selected options, highlight the first
      $selected.first().trigger('mouseenter');
    } else {
      // If there are no selected options, highlight the first option
      // in the dropdown
      $options.first().trigger('mouseenter');
    }

    this.ensureHighlightVisible();
  };

  Results.prototype.setClasses = function () {
    var self = this;

    this.data.current(function (selected) {
      var selectedIds = selected.map(function (s) {
        return s.id.toString();
      });

      var $options = self.$results
        .find('.select2-results__option--selectable');

      $options.each(function () {
        var $option = $(this);

        var item = Utils.GetData(this, 'data');

        // id needs to be converted to a string when comparing
        var id = '' + item.id;

        if ((item.element != null && item.element.selected) ||
            (item.element == null && selectedIds.indexOf(id) > -1)) {
          this.classList.add('select2-results__option--selected');
          $option.attr('aria-selected', 'true');
        } else {
          this.classList.remove('select2-results__option--selected');
          $option.attr('aria-selected', 'false');
        }
      });

    });
  };

  Results.prototype.showLoading = function (params) {
    this.hideLoading();

    var loadingMore = this.options.get('translations').get('searching');

    var loading = {
      disabled: true,
      loading: true,
      text: loadingMore(params)
    };
    var $loading = this.option(loading);
    $loading.className += ' loading-results';

    this.$results.prepend($loading);
  };

  Results.prototype.hideLoading = function () {
    this.$results.find('.loading-results').remove();
  };

  Results.prototype.option = function (data) {
    var option = document.createElement('li');
    option.classList.add('select2-results__option');
    option.classList.add('select2-results__option--selectable');

    var attrs = {
      'role': 'option'
    };

    var matches = window.Element.prototype.matches ||
      window.Element.prototype.msMatchesSelector ||
      window.Element.prototype.webkitMatchesSelector;

    if ((data.element != null && matches.call(data.element, ':disabled')) ||
        (data.element == null && data.disabled)) {
      attrs['aria-disabled'] = 'true';

      option.classList.remove('select2-results__option--selectable');
      option.classList.add('select2-results__option--disabled');
    }

    if (data.id == null) {
      option.classList.remove('select2-results__option--selectable');
    }

    if (data._resultId != null) {
      option.id = data._resultId;
    }

    if (data.title) {
      option.title = data.title;
    }

    if (data.children) {
      attrs.role = 'group';
      attrs['aria-label'] = data.text;

      option.classList.remove('select2-results__option--selectable');
      option.classList.add('select2-results__option--group');
    }

    for (var attr in attrs) {
      var val = attrs[attr];

      option.setAttribute(attr, val);
    }

    if (data.children) {
      var $option = $(option);

      var label = document.createElement('strong');
      label.className = 'select2-results__group';

      this.template(data, label);

      var $children = [];

      for (var c = 0; c < data.children.length; c++) {
        var child = data.children[c];

        var $child = this.option(child);

        $children.push($child);
      }

      var $childrenContainer = $('<ul></ul>', {
        'class': 'select2-results__options select2-results__options--nested'
      });

      $childrenContainer.append($children);

      $option.append(label);
      $option.append($childrenContainer);
    } else {
      this.template(data, option);
    }

    Utils.StoreData(option, 'data', data);

    return option;
  };

  Results.prototype.bind = function (container, $container) {
    var self = this;

    var id = container.id + '-results';

    this.$results.attr('id', id);

    container.on('results:all', function (params) {
      self.clear();
      self.append(params.data);

      if (container.isOpen()) {
        self.setClasses();
        self.highlightFirstItem();
      }
    });

    container.on('results:append', function (params) {
      self.append(params.data);

      if (container.isOpen()) {
        self.setClasses();
      }
    });

    container.on('query', function (params) {
      self.hideMessages();
      self.showLoading(params);
    });

    container.on('select', function () {
      if (!container.isOpen()) {
        return;
      }

      self.setClasses();

      if (self.options.get('scrollAfterSelect')) {
        self.highlightFirstItem();
      }
    });

    container.on('unselect', function () {
      if (!container.isOpen()) {
        return;
      }

      self.setClasses();

      if (self.options.get('scrollAfterSelect')) {
        self.highlightFirstItem();
      }
    });

    container.on('open', function () {
      // When the dropdown is open, aria-expended="true"
      self.$results.attr('aria-expanded', 'true');
      self.$results.attr('aria-hidden', 'false');

      self.setClasses();
      self.ensureHighlightVisible();
    });

    container.on('close', function () {
      // When the dropdown is closed, aria-expended="false"
      self.$results.attr('aria-expanded', 'false');
      self.$results.attr('aria-hidden', 'true');
      self.$results.removeAttr('aria-activedescendant');
    });

    container.on('results:toggle', function () {
      var $highlighted = self.getHighlightedResults();

      if ($highlighted.length === 0) {
        return;
      }

      $highlighted.trigger('mouseup');
    });

    container.on('results:select', function () {
      var $highlighted = self.getHighlightedResults();

      if ($highlighted.length === 0) {
        return;
      }

      var data = Utils.GetData($highlighted[0], 'data');

      if ($highlighted.hasClass('select2-results__option--selected')) {
        self.trigger('close', {});
      } else {
        self.trigger('select', {
          data: data
        });
      }
    });

    container.on('results:previous', function () {
      var $highlighted = self.getHighlightedResults();

      var $options = self.$results.find('.select2-results__option--selectable');

      var currentIndex = $options.index($highlighted);

      // If we are already at the top, don't move further
      // If no options, currentIndex will be -1
      if (currentIndex <= 0) {
        return;
      }

      var nextIndex = currentIndex - 1;

      // If none are highlighted, highlight the first
      if ($highlighted.length === 0) {
        nextIndex = 0;
      }

      var $next = $options.eq(nextIndex);

      $next.trigger('mouseenter');

      var currentOffset = self.$results.offset().top;
      var nextTop = $next.offset().top;
      var nextOffset = self.$results.scrollTop() + (nextTop - currentOffset);

      if (nextIndex === 0) {
        self.$results.scrollTop(0);
      } else if (nextTop - currentOffset < 0) {
        self.$results.scrollTop(nextOffset);
      }
    });

    container.on('results:next', function () {
      var $highlighted = self.getHighlightedResults();

      var $options = self.$results.find('.select2-results__option--selectable');

      var currentIndex = $options.index($highlighted);

      var nextIndex = currentIndex + 1;

      // If we are at the last option, stay there
      if (nextIndex >= $options.length) {
        return;
      }

      var $next = $options.eq(nextIndex);

      $next.trigger('mouseenter');

      var currentOffset = self.$results.offset().top +
        self.$results.outerHeight(false);
      var nextBottom = $next.offset().top + $next.outerHeight(false);
      var nextOffset = self.$results.scrollTop() + nextBottom - currentOffset;

      if (nextIndex === 0) {
        self.$results.scrollTop(0);
      } else if (nextBottom > currentOffset) {
        self.$results.scrollTop(nextOffset);
      }
    });

    container.on('results:focus', function (params) {
      params.element[0].classList.add('select2-results__option--highlighted');
      params.element[0].setAttribute('aria-selected', 'true');
    });

    container.on('results:message', function (params) {
      self.displayMessage(params);
    });

    if ($.fn.mousewheel) {
      this.$results.on('mousewheel', function (e) {
        var top = self.$results.scrollTop();

        var bottom = self.$results.get(0).scrollHeight - top + e.deltaY;

        var isAtTop = e.deltaY > 0 && top - e.deltaY <= 0;
        var isAtBottom = e.deltaY < 0 && bottom <= self.$results.height();

        if (isAtTop) {
          self.$results.scrollTop(0);

          e.preventDefault();
          e.stopPropagation();
        } else if (isAtBottom) {
          self.$results.scrollTop(
            self.$results.get(0).scrollHeight - self.$results.height()
          );

          e.preventDefault();
          e.stopPropagation();
        }
      });
    }

    this.$results.on('mouseup', '.select2-results__option--selectable',
      function (evt) {
      var $this = $(this);

      var data = Utils.GetData(this, 'data');

      if ($this.hasClass('select2-results__option--selected')) {
        if (self.options.get('multiple')) {
          self.trigger('unselect', {
            originalEvent: evt,
            data: data
          });
        } else {
          self.trigger('close', {});
        }

        return;
      }

      self.trigger('select', {
        originalEvent: evt,
        data: data
      });
    });

    this.$results.on('mouseenter', '.select2-results__option--selectable',
      function (evt) {
      var data = Utils.GetData(this, 'data');

      self.getHighlightedResults()
          .removeClass('select2-results__option--highlighted')
          .attr('aria-selected', 'false');

      self.trigger('results:focus', {
        data: data,
        element: $(this)
      });
    });
  };

  Results.prototype.getHighlightedResults = function () {
    var $highlighted = this.$results
    .find('.select2-results__option--highlighted');

    return $highlighted;
  };

  Results.prototype.destroy = function () {
    this.$results.remove();
  };

  Results.prototype.ensureHighlightVisible = function () {
    var $highlighted = this.getHighlightedResults();

    if ($highlighted.length === 0) {
      return;
    }

    var $options = this.$results.find('.select2-results__option--selectable');

    var currentIndex = $options.index($highlighted);

    var currentOffset = this.$results.offset().top;
    var nextTop = $highlighted.offset().top;
    var nextOffset = this.$results.scrollTop() + (nextTop - currentOffset);

    var offsetDelta = nextTop - currentOffset;
    nextOffset -= $highlighted.outerHeight(false) * 2;

    if (currentIndex <= 2) {
      this.$results.scrollTop(0);
    } else if (offsetDelta > this.$results.outerHeight() || offsetDelta < 0) {
      this.$results.scrollTop(nextOffset);
    }
  };

  Results.prototype.template = function (result, container) {
    var template = this.options.get('templateResult');
    var escapeMarkup = this.options.get('escapeMarkup');

    var content = template(result, container);

    if (content == null) {
      container.style.display = 'none';
    } else if (typeof content === 'string') {
      container.innerHTML = escapeMarkup(content);
    } else {
      $(container).append(content);
    }
  };

  return Results;
});

S2.define('select2/keys',[

], function () {
  var KEYS = {
    BACKSPACE: 8,
    TAB: 9,
    ENTER: 13,
    SHIFT: 16,
    CTRL: 17,
    ALT: 18,
    ESC: 27,
    SPACE: 32,
    PAGE_UP: 33,
    PAGE_DOWN: 34,
    END: 35,
    HOME: 36,
    LEFT: 37,
    UP: 38,
    RIGHT: 39,
    DOWN: 40,
    DELETE: 46
  };

  return KEYS;
});

S2.define('select2/selection/base',[
  'jquery',
  '../utils',
  '../keys'
], function ($, Utils, KEYS) {
  function BaseSelection ($element, options) {
    this.$element = $element;
    this.options = options;

    BaseSelection.__super__.constructor.call(this);
  }

  Utils.Extend(BaseSelection, Utils.Observable);

  BaseSelection.prototype.render = function () {
    var $selection = $(
      '<span class="select2-selection" role="combobox" ' +
      ' aria-haspopup="true" aria-expanded="false">' +
      '</span>'
    );

    this._tabindex = 0;

    if (Utils.GetData(this.$element[0], 'old-tabindex') != null) {
      this._tabindex = Utils.GetData(this.$element[0], 'old-tabindex');
    } else if (this.$element.attr('tabindex') != null) {
      this._tabindex = this.$element.attr('tabindex');
    }

    $selection.attr('title', this.$element.attr('title'));
    $selection.attr('tabindex', this._tabindex);
    $selection.attr('aria-disabled', 'false');

    this.$selection = $selection;

    return $selection;
  };

  BaseSelection.prototype.bind = function (container, $container) {
    var self = this;

    var resultsId = container.id + '-results';

    this.container = container;

    this.$selection.on('focus', function (evt) {
      self.trigger('focus', evt);
    });

    this.$selection.on('blur', function (evt) {
      self._handleBlur(evt);
    });

    this.$selection.on('keydown', function (evt) {
      self.trigger('keypress', evt);

      if (evt.which === KEYS.SPACE) {
        evt.preventDefault();
      }
    });

    container.on('results:focus', function (params) {
      self.$selection.attr('aria-activedescendant', params.data._resultId);
    });

    container.on('selection:update', function (params) {
      self.update(params.data);
    });

    container.on('open', function () {
      // When the dropdown is open, aria-expanded="true"
      self.$selection.attr('aria-expanded', 'true');
      self.$selection.attr('aria-owns', resultsId);

      self._attachCloseHandler(container);
    });

    container.on('close', function () {
      // When the dropdown is closed, aria-expanded="false"
      self.$selection.attr('aria-expanded', 'false');
      self.$selection.removeAttr('aria-activedescendant');
      self.$selection.removeAttr('aria-owns');

      self.$selection.trigger('focus');

      self._detachCloseHandler(container);
    });

    container.on('enable', function () {
      self.$selection.attr('tabindex', self._tabindex);
      self.$selection.attr('aria-disabled', 'false');
    });

    container.on('disable', function () {
      self.$selection.attr('tabindex', '-1');
      self.$selection.attr('aria-disabled', 'true');
    });
  };

  BaseSelection.prototype._handleBlur = function (evt) {
    var self = this;

    // This needs to be delayed as the active element is the body when the tab
    // key is pressed, possibly along with others.
    window.setTimeout(function () {
      // Don't trigger `blur` if the focus is still in the selection
      if (
        (document.activeElement == self.$selection[0]) ||
        ($.contains(self.$selection[0], document.activeElement))
      ) {
        return;
      }

      self.trigger('blur', evt);
    }, 1);
  };

  BaseSelection.prototype._attachCloseHandler = function (container) {

    $(document.body).on('mousedown.select2.' + container.id, function (e) {
      var $target = $(e.target);

      var $select = $target.closest('.select2');

      var $all = $('.select2.select2-container--open');

      $all.each(function () {
        if (this == $select[0]) {
          return;
        }

        var $element = Utils.GetData(this, 'element');

        $element.select2('close');
      });
    });
  };

  BaseSelection.prototype._detachCloseHandler = function (container) {
    $(document.body).off('mousedown.select2.' + container.id);
  };

  BaseSelection.prototype.position = function ($selection, $container) {
    var $selectionContainer = $container.find('.selection');
    $selectionContainer.append($selection);
  };

  BaseSelection.prototype.destroy = function () {
    this._detachCloseHandler(this.container);
  };

  BaseSelection.prototype.update = function (data) {
    throw new Error('The `update` method must be defined in child classes.');
  };

  /**
   * Helper method to abstract the "enabled" (not "disabled") state of this
   * object.
   *
   * @return {true} if the instance is not disabled.
   * @return {false} if the instance is disabled.
   */
  BaseSelection.prototype.isEnabled = function () {
    return !this.isDisabled();
  };

  /**
   * Helper method to abstract the "disabled" state of this object.
   *
   * @return {true} if the disabled option is true.
   * @return {false} if the disabled option is false.
   */
  BaseSelection.prototype.isDisabled = function () {
    return this.options.get('disabled');
  };

  return BaseSelection;
});

S2.define('select2/selection/single',[
  'jquery',
  './base',
  '../utils',
  '../keys'
], function ($, BaseSelection, Utils, KEYS) {
  function SingleSelection () {
    SingleSelection.__super__.constructor.apply(this, arguments);
  }

  Utils.Extend(SingleSelection, BaseSelection);

  SingleSelection.prototype.render = function () {
    var $selection = SingleSelection.__super__.render.call(this);

    $selection[0].classList.add('select2-selection--single');

    $selection.html(
      '<span class="select2-selection__rendered"></span>' +
      '<span class="select2-selection__arrow" role="presentation">' +
        '<b role="presentation"></b>' +
      '</span>'
    );

    return $selection;
  };

  SingleSelection.prototype.bind = function (container, $container) {
    var self = this;

    SingleSelection.__super__.bind.apply(this, arguments);

    var id = container.id + '-container';

    this.$selection.find('.select2-selection__rendered')
      .attr('id', id)
      .attr('role', 'textbox')
      .attr('aria-readonly', 'true');
    this.$selection.attr('aria-labelledby', id);

    this.$selection.on('mousedown', function (evt) {
      // Only respond to left clicks
      if (evt.which !== 1) {
        return;
      }

      self.trigger('toggle', {
        originalEvent: evt
      });
    });

    this.$selection.on('focus', function (evt) {
      // User focuses on the container
    });

    this.$selection.on('blur', function (evt) {
      // User exits the container
    });

    container.on('focus', function (evt) {
      if (!container.isOpen()) {
        self.$selection.trigger('focus');
      }
    });
  };

  SingleSelection.prototype.clear = function () {
    var $rendered = this.$selection.find('.select2-selection__rendered');
    $rendered.empty();
    $rendered.removeAttr('title'); // clear tooltip on empty
  };

  SingleSelection.prototype.display = function (data, container) {
    var template = this.options.get('templateSelection');
    var escapeMarkup = this.options.get('escapeMarkup');

    return escapeMarkup(template(data, container));
  };

  SingleSelection.prototype.selectionContainer = function () {
    return $('<span></span>');
  };

  SingleSelection.prototype.update = function (data) {
    if (data.length === 0) {
      this.clear();
      return;
    }

    var selection = data[0];

    var $rendered = this.$selection.find('.select2-selection__rendered');
    var formatted = this.display(selection, $rendered);

    $rendered.empty().append(formatted);

    var title = selection.title || selection.text;

    if (title) {
      $rendered.attr('title', title);
    } else {
      $rendered.removeAttr('title');
    }
  };

  return SingleSelection;
});

S2.define('select2/selection/multiple',[
  'jquery',
  './base',
  '../utils'
], function ($, BaseSelection, Utils) {
  function MultipleSelection ($element, options) {
    MultipleSelection.__super__.constructor.apply(this, arguments);
  }

  Utils.Extend(MultipleSelection, BaseSelection);

  MultipleSelection.prototype.render = function () {
    var $selection = MultipleSelection.__super__.render.call(this);

    $selection[0].classList.add('select2-selection--multiple');

    $selection.html(
      '<ul class="select2-selection__rendered"></ul>'
    );

    return $selection;
  };

  MultipleSelection.prototype.bind = function (container, $container) {
    var self = this;

    MultipleSelection.__super__.bind.apply(this, arguments);

    var id = container.id + '-container';
    this.$selection.find('.select2-selection__rendered').attr('id', id);

    this.$selection.on('click', function (evt) {
      self.trigger('toggle', {
        originalEvent: evt
      });
    });

    this.$selection.on(
      'click',
      '.select2-selection__choice__remove',
      function (evt) {
        // Ignore the event if it is disabled
        if (self.isDisabled()) {
          return;
        }

        var $remove = $(this);
        var $selection = $remove.parent();

        var data = Utils.GetData($selection[0], 'data');

        self.trigger('unselect', {
          originalEvent: evt,
          data: data
        });
      }
    );

    this.$selection.on(
      'keydown',
      '.select2-selection__choice__remove',
      function (evt) {
        // Ignore the event if it is disabled
        if (self.isDisabled()) {
          return;
        }

        evt.stopPropagation();
      }
    );
  };

  MultipleSelection.prototype.clear = function () {
    var $rendered = this.$selection.find('.select2-selection__rendered');
    $rendered.empty();
    $rendered.removeAttr('title');
  };

  MultipleSelection.prototype.display = function (data, container) {
    var template = this.options.get('templateSelection');
    var escapeMarkup = this.options.get('escapeMarkup');

    return escapeMarkup(template(data, container));
  };

  MultipleSelection.prototype.selectionContainer = function () {
    var $container = $(
      '<li class="select2-selection__choice">' +
        '<button type="button" class="select2-selection__choice__remove" ' +
        'tabindex="-1">' +
          '<span aria-hidden="true">&times;</span>' +
        '</button>' +
        '<span class="select2-selection__choice__display"></span>' +
      '</li>'
    );

    return $container;
  };

  MultipleSelection.prototype.update = function (data) {
    this.clear();

    if (data.length === 0) {
      return;
    }

    var $selections = [];

    var selectionIdPrefix = this.$selection.find('.select2-selection__rendered')
      .attr('id') + '-choice-';

    for (var d = 0; d < data.length; d++) {
      var selection = data[d];

      var $selection = this.selectionContainer();
      var formatted = this.display(selection, $selection);

      var selectionId = selectionIdPrefix + Utils.generateChars(4) + '-';

      if (selection.id) {
        selectionId += selection.id;
      } else {
        selectionId += Utils.generateChars(4);
      }

      $selection.find('.select2-selection__choice__display')
        .append(formatted)
        .attr('id', selectionId);

      var title = selection.title || selection.text;

      if (title) {
        $selection.attr('title', title);
      }

      var removeItem = this.options.get('translations').get('removeItem');

      var $remove = $selection.find('.select2-selection__choice__remove');

      $remove.attr('title', removeItem());
      $remove.attr('aria-label', removeItem());
      $remove.attr('aria-describedby', selectionId);

      Utils.StoreData($selection[0], 'data', selection);

      $selections.push($selection);
    }

    var $rendered = this.$selection.find('.select2-selection__rendered');

    $rendered.append($selections);
  };

  return MultipleSelection;
});

S2.define('select2/selection/placeholder',[

], function () {
  function Placeholder (decorated, $element, options) {
    this.placeholder = this.normalizePlaceholder(options.get('placeholder'));

    decorated.call(this, $element, options);
  }

  Placeholder.prototype.normalizePlaceholder = function (_, placeholder) {
    if (typeof placeholder === 'string') {
      placeholder = {
        id: '',
        text: placeholder
      };
    }

    return placeholder;
  };

  Placeholder.prototype.createPlaceholder = function (decorated, placeholder) {
    var $placeholder = this.selectionContainer();

    $placeholder.html(this.display(placeholder));
    $placeholder[0].classList.add('select2-selection__placeholder');
    $placeholder[0].classList.remove('select2-selection__choice');

    return $placeholder;
  };

  Placeholder.prototype.update = function (decorated, data) {
    var singlePlaceholder = (
      data.length == 1 && data[0].id != this.placeholder.id
    );
    var multipleSelections = data.length > 1;

    if (multipleSelections || singlePlaceholder) {
      return decorated.call(this, data);
    }

    this.clear();

    var $placeholder = this.createPlaceholder(this.placeholder);

    this.$selection.find('.select2-selection__rendered').append($placeholder);
  };

  return Placeholder;
});

S2.define('select2/selection/allowClear',[
  'jquery',
  '../keys',
  '../utils'
], function ($, KEYS, Utils) {
  function AllowClear () { }

  AllowClear.prototype.bind = function (decorated, container, $container) {
    var self = this;

    decorated.call(this, container, $container);

    if (this.placeholder == null) {
      if (this.options.get('debug') && window.console && console.error) {
        console.error(
          'Select2: The `allowClear` option should be used in combination ' +
          'with the `placeholder` option.'
        );
      }
    }

    this.$selection.on('mousedown', '.select2-selection__clear',
      function (evt) {
        self._handleClear(evt);
    });

    container.on('keypress', function (evt) {
      self._handleKeyboardClear(evt, container);
    });
  };

  AllowClear.prototype._handleClear = function (_, evt) {
    // Ignore the event if it is disabled
    if (this.isDisabled()) {
      return;
    }

    var $clear = this.$selection.find('.select2-selection__clear');

    // Ignore the event if nothing has been selected
    if ($clear.length === 0) {
      return;
    }

    evt.stopPropagation();

    var data = Utils.GetData($clear[0], 'data');

    var previousVal = this.$element.val();
    this.$element.val(this.placeholder.id);

    var unselectData = {
      data: data
    };
    this.trigger('clear', unselectData);
    if (unselectData.prevented) {
      this.$element.val(previousVal);
      return;
    }

    for (var d = 0; d < data.length; d++) {
      unselectData = {
        data: data[d]
      };

      // Trigger the `unselect` event, so people can prevent it from being
      // cleared.
      this.trigger('unselect', unselectData);

      // If the event was prevented, don't clear it out.
      if (unselectData.prevented) {
        this.$element.val(previousVal);
        return;
      }
    }

    this.$element.trigger('input').trigger('change');

    this.trigger('toggle', {});
  };

  AllowClear.prototype._handleKeyboardClear = function (_, evt, container) {
    if (container.isOpen()) {
      return;
    }

    if (evt.which == KEYS.DELETE || evt.which == KEYS.BACKSPACE) {
      this._handleClear(evt);
    }
  };

  AllowClear.prototype.update = function (decorated, data) {
    decorated.call(this, data);

    this.$selection.find('.select2-selection__clear').remove();

    if (this.$selection.find('.select2-selection__placeholder').length > 0 ||
        data.length === 0) {
      return;
    }

    var selectionId = this.$selection.find('.select2-selection__rendered')
      .attr('id');

    var removeAll = this.options.get('translations').get('removeAllItems');

    var $remove = $(
      '<button type="button" class="select2-selection__clear" tabindex="-1">' +
        '<span aria-hidden="true">&times;</span>' +
      '</button>'
    );
    $remove.attr('title', removeAll());
    $remove.attr('aria-label', removeAll());
    $remove.attr('aria-describedby', selectionId);
    Utils.StoreData($remove[0], 'data', data);

    this.$selection.prepend($remove);
  };

  return AllowClear;
});

S2.define('select2/selection/search',[
  'jquery',
  '../utils',
  '../keys'
], function ($, Utils, KEYS) {
  function Search (decorated, $element, options) {
    decorated.call(this, $element, options);
  }

  Search.prototype.render = function (decorated) {
    var $search = $(
      '<span class="select2-search select2-search--inline">' +
        '<input class="select2-search__field" type="search" tabindex="-1"' +
        ' autocorrect="off" autocapitalize="none"' +
        ' spellcheck="false" role="searchbox" aria-autocomplete="list" />' +
      '</span>'
    );

    this.$searchContainer = $search;
    this.$search = $search.find('input');

    this.$search.prop('autocomplete', this.options.get('autocomplete'));

    var $rendered = decorated.call(this);

    this._transferTabIndex();
    $rendered.append(this.$searchContainer);

    return $rendered;
  };

  Search.prototype.bind = function (decorated, container, $container) {
    var self = this;

    var resultsId = container.id + '-results';
    var selectionId = container.id + '-container';

    decorated.call(this, container, $container);

    self.$search.attr('aria-describedby', selectionId);

    container.on('open', function () {
      self.$search.attr('aria-controls', resultsId);
      self.$search.trigger('focus');
    });

    container.on('close', function () {
      self.$search.val('');
      self.resizeSearch();
      self.$search.removeAttr('aria-controls');
      self.$search.removeAttr('aria-activedescendant');
      self.$search.trigger('focus');
    });

    container.on('enable', function () {
      self.$search.prop('disabled', false);

      self._transferTabIndex();
    });

    container.on('disable', function () {
      self.$search.prop('disabled', true);
    });

    container.on('focus', function (evt) {
      self.$search.trigger('focus');
    });

    container.on('results:focus', function (params) {
      if (params.data._resultId) {
        self.$search.attr('aria-activedescendant', params.data._resultId);
      } else {
        self.$search.removeAttr('aria-activedescendant');
      }
    });

    this.$selection.on('focusin', '.select2-search--inline', function (evt) {
      self.trigger('focus', evt);
    });

    this.$selection.on('focusout', '.select2-search--inline', function (evt) {
      self._handleBlur(evt);
    });

    this.$selection.on('keydown', '.select2-search--inline', function (evt) {
      evt.stopPropagation();

      self.trigger('keypress', evt);

      self._keyUpPrevented = evt.isDefaultPrevented();

      var key = evt.which;

      if (key === KEYS.BACKSPACE && self.$search.val() === '') {
        var $previousChoice = self.$selection
          .find('.select2-selection__choice').last();

        if ($previousChoice.length > 0) {
          var item = Utils.GetData($previousChoice[0], 'data');

          self.searchRemoveChoice(item);

          evt.preventDefault();
        }
      }
    });

    this.$selection.on('click', '.select2-search--inline', function (evt) {
      if (self.$search.val()) {
        evt.stopPropagation();
      }
    });

    // Try to detect the IE version should the `documentMode` property that
    // is stored on the document. This is only implemented in IE and is
    // slightly cleaner than doing a user agent check.
    // This property is not available in Edge, but Edge also doesn't have
    // this bug.
    var msie = document.documentMode;
    var disableInputEvents = msie && msie <= 11;

    // Workaround for browsers which do not support the `input` event
    // This will prevent double-triggering of events for browsers which support
    // both the `keyup` and `input` events.
    this.$selection.on(
      'input.searchcheck',
      '.select2-search--inline',
      function (evt) {
        // IE will trigger the `input` event when a placeholder is used on a
        // search box. To get around this issue, we are forced to ignore all
        // `input` events in IE and keep using `keyup`.
        if (disableInputEvents) {
          self.$selection.off('input.search input.searchcheck');
          return;
        }

        // Unbind the duplicated `keyup` event
        self.$selection.off('keyup.search');
      }
    );

    this.$selection.on(
      'keyup.search input.search',
      '.select2-search--inline',
      function (evt) {
        // IE will trigger the `input` event when a placeholder is used on a
        // search box. To get around this issue, we are forced to ignore all
        // `input` events in IE and keep using `keyup`.
        if (disableInputEvents && evt.type === 'input') {
          self.$selection.off('input.search input.searchcheck');
          return;
        }

        var key = evt.which;

        // We can freely ignore events from modifier keys
        if (key == KEYS.SHIFT || key == KEYS.CTRL || key == KEYS.ALT) {
          return;
        }

        // Tabbing will be handled during the `keydown` phase
        if (key == KEYS.TAB) {
          return;
        }

        self.handleSearch(evt);
      }
    );
  };

  /**
   * This method will transfer the tabindex attribute from the rendered
   * selection to the search box. This allows for the search box to be used as
   * the primary focus instead of the selection container.
   *
   * @private
   */
  Search.prototype._transferTabIndex = function (decorated) {
    this.$search.attr('tabindex', this.$selection.attr('tabindex'));
    this.$selection.attr('tabindex', '-1');
  };

  Search.prototype.createPlaceholder = function (decorated, placeholder) {
    this.$search.attr('placeholder', placeholder.text);
  };

  Search.prototype.update = function (decorated, data) {
    var searchHadFocus = this.$search[0] == document.activeElement;

    this.$search.attr('placeholder', '');

    decorated.call(this, data);

    this.resizeSearch();
    if (searchHadFocus) {
      this.$search.trigger('focus');
    }
  };

  Search.prototype.handleSearch = function () {
    this.resizeSearch();

    if (!this._keyUpPrevented) {
      var input = this.$search.val();

      this.trigger('query', {
        term: input
      });
    }

    this._keyUpPrevented = false;
  };

  Search.prototype.searchRemoveChoice = function (decorated, item) {
    this.trigger('unselect', {
      data: item
    });

    this.$search.val(item.text);
    this.handleSearch();
  };

  Search.prototype.resizeSearch = function () {
    this.$search.css('width', '25px');

    var width = '100%';

    if (this.$search.attr('placeholder') === '') {
      var minimumWidth = this.$search.val().length + 1;

      width = (minimumWidth * 0.75) + 'em';
    }

    this.$search.css('width', width);
  };

  return Search;
});

S2.define('select2/selection/selectionCss',[
  '../utils'
], function (Utils) {
  function SelectionCSS () { }

  SelectionCSS.prototype.render = function (decorated) {
    var $selection = decorated.call(this);

    var selectionCssClass = this.options.get('selectionCssClass') || '';

    if (selectionCssClass.indexOf(':all:') !== -1) {
      selectionCssClass = selectionCssClass.replace(':all:', '');

      Utils.copyNonInternalCssClasses($selection[0], this.$element[0]);
    }

    $selection.addClass(selectionCssClass);

    return $selection;
  };

  return SelectionCSS;
});

S2.define('select2/selection/eventRelay',[
  'jquery'
], function ($) {
  function EventRelay () { }

  EventRelay.prototype.bind = function (decorated, container, $container) {
    var self = this;
    var relayEvents = [
      'open', 'opening',
      'close', 'closing',
      'select', 'selecting',
      'unselect', 'unselecting',
      'clear', 'clearing'
    ];

    var preventableEvents = [
      'opening', 'closing', 'selecting', 'unselecting', 'clearing'
    ];

    decorated.call(this, container, $container);

    container.on('*', function (name, params) {
      // Ignore events that should not be relayed
      if (relayEvents.indexOf(name) === -1) {
        return;
      }

      // The parameters should always be an object
      params = params || {};

      // Generate the jQuery event for the Select2 event
      var evt = $.Event('select2:' + name, {
        params: params
      });

      self.$element.trigger(evt);

      // Only handle preventable events if it was one
      if (preventableEvents.indexOf(name) === -1) {
        return;
      }

      params.prevented = evt.isDefaultPrevented();
    });
  };

  return EventRelay;
});

S2.define('select2/translation',[
  'jquery',
  'require'
], function ($, require) {
  function Translation (dict) {
    this.dict = dict || {};
  }

  Translation.prototype.all = function () {
    return this.dict;
  };

  Translation.prototype.get = function (key) {
    return this.dict[key];
  };

  Translation.prototype.extend = function (translation) {
    this.dict = $.extend({}, translation.all(), this.dict);
  };

  // Static functions

  Translation._cache = {};

  Translation.loadPath = function (path) {
    if (!(path in Translation._cache)) {
      var translations = require(path);

      Translation._cache[path] = translations;
    }

    return new Translation(Translation._cache[path]);
  };

  return Translation;
});

S2.define('select2/diacritics',[

], function () {
  var diacritics = {
    '\u24B6': 'A',
    '\uFF21': 'A',
    '\u00C0': 'A',
    '\u00C1': 'A',
    '\u00C2': 'A',
    '\u1EA6': 'A',
    '\u1EA4': 'A',
    '\u1EAA': 'A',
    '\u1EA8': 'A',
    '\u00C3': 'A',
    '\u0100': 'A',
    '\u0102': 'A',
    '\u1EB0': 'A',
    '\u1EAE': 'A',
    '\u1EB4': 'A',
    '\u1EB2': 'A',
    '\u0226': 'A',
    '\u01E0': 'A',
    '\u00C4': 'A',
    '\u01DE': 'A',
    '\u1EA2': 'A',
    '\u00C5': 'A',
    '\u01FA': 'A',
    '\u01CD': 'A',
    '\u0200': 'A',
    '\u0202': 'A',
    '\u1EA0': 'A',
    '\u1EAC': 'A',
    '\u1EB6': 'A',
    '\u1E00': 'A',
    '\u0104': 'A',
    '\u023A': 'A',
    '\u2C6F': 'A',
    '\uA732': 'AA',
    '\u00C6': 'AE',
    '\u01FC': 'AE',
    '\u01E2': 'AE',
    '\uA734': 'AO',
    '\uA736': 'AU',
    '\uA738': 'AV',
    '\uA73A': 'AV',
    '\uA73C': 'AY',
    '\u24B7': 'B',
    '\uFF22': 'B',
    '\u1E02': 'B',
    '\u1E04': 'B',
    '\u1E06': 'B',
    '\u0243': 'B',
    '\u0182': 'B',
    '\u0181': 'B',
    '\u24B8': 'C',
    '\uFF23': 'C',
    '\u0106': 'C',
    '\u0108': 'C',
    '\u010A': 'C',
    '\u010C': 'C',
    '\u00C7': 'C',
    '\u1E08': 'C',
    '\u0187': 'C',
    '\u023B': 'C',
    '\uA73E': 'C',
    '\u24B9': 'D',
    '\uFF24': 'D',
    '\u1E0A': 'D',
    '\u010E': 'D',
    '\u1E0C': 'D',
    '\u1E10': 'D',
    '\u1E12': 'D',
    '\u1E0E': 'D',
    '\u0110': 'D',
    '\u018B': 'D',
    '\u018A': 'D',
    '\u0189': 'D',
    '\uA779': 'D',
    '\u01F1': 'DZ',
    '\u01C4': 'DZ',
    '\u01F2': 'Dz',
    '\u01C5': 'Dz',
    '\u24BA': 'E',
    '\uFF25': 'E',
    '\u00C8': 'E',
    '\u00C9': 'E',
    '\u00CA': 'E',
    '\u1EC0': 'E',
    '\u1EBE': 'E',
    '\u1EC4': 'E',
    '\u1EC2': 'E',
    '\u1EBC': 'E',
    '\u0112': 'E',
    '\u1E14': 'E',
    '\u1E16': 'E',
    '\u0114': 'E',
    '\u0116': 'E',
    '\u00CB': 'E',
    '\u1EBA': 'E',
    '\u011A': 'E',
    '\u0204': 'E',
    '\u0206': 'E',
    '\u1EB8': 'E',
    '\u1EC6': 'E',
    '\u0228': 'E',
    '\u1E1C': 'E',
    '\u0118': 'E',
    '\u1E18': 'E',
    '\u1E1A': 'E',
    '\u0190': 'E',
    '\u018E': 'E',
    '\u24BB': 'F',
    '\uFF26': 'F',
    '\u1E1E': 'F',
    '\u0191': 'F',
    '\uA77B': 'F',
    '\u24BC': 'G',
    '\uFF27': 'G',
    '\u01F4': 'G',
    '\u011C': 'G',
    '\u1E20': 'G',
    '\u011E': 'G',
    '\u0120': 'G',
    '\u01E6': 'G',
    '\u0122': 'G',
    '\u01E4': 'G',
    '\u0193': 'G',
    '\uA7A0': 'G',
    '\uA77D': 'G',
    '\uA77E': 'G',
    '\u24BD': 'H',
    '\uFF28': 'H',
    '\u0124': 'H',
    '\u1E22': 'H',
    '\u1E26': 'H',
    '\u021E': 'H',
    '\u1E24': 'H',
    '\u1E28': 'H',
    '\u1E2A': 'H',
    '\u0126': 'H',
    '\u2C67': 'H',
    '\u2C75': 'H',
    '\uA78D': 'H',
    '\u24BE': 'I',
    '\uFF29': 'I',
    '\u00CC': 'I',
    '\u00CD': 'I',
    '\u00CE': 'I',
    '\u0128': 'I',
    '\u012A': 'I',
    '\u012C': 'I',
    '\u0130': 'I',
    '\u00CF': 'I',
    '\u1E2E': 'I',
    '\u1EC8': 'I',
    '\u01CF': 'I',
    '\u0208': 'I',
    '\u020A': 'I',
    '\u1ECA': 'I',
    '\u012E': 'I',
    '\u1E2C': 'I',
    '\u0197': 'I',
    '\u24BF': 'J',
    '\uFF2A': 'J',
    '\u0134': 'J',
    '\u0248': 'J',
    '\u24C0': 'K',
    '\uFF2B': 'K',
    '\u1E30': 'K',
    '\u01E8': 'K',
    '\u1E32': 'K',
    '\u0136': 'K',
    '\u1E34': 'K',
    '\u0198': 'K',
    '\u2C69': 'K',
    '\uA740': 'K',
    '\uA742': 'K',
    '\uA744': 'K',
    '\uA7A2': 'K',
    '\u24C1': 'L',
    '\uFF2C': 'L',
    '\u013F': 'L',
    '\u0139': 'L',
    '\u013D': 'L',
    '\u1E36': 'L',
    '\u1E38': 'L',
    '\u013B': 'L',
    '\u1E3C': 'L',
    '\u1E3A': 'L',
    '\u0141': 'L',
    '\u023D': 'L',
    '\u2C62': 'L',
    '\u2C60': 'L',
    '\uA748': 'L',
    '\uA746': 'L',
    '\uA780': 'L',
    '\u01C7': 'LJ',
    '\u01C8': 'Lj',
    '\u24C2': 'M',
    '\uFF2D': 'M',
    '\u1E3E': 'M',
    '\u1E40': 'M',
    '\u1E42': 'M',
    '\u2C6E': 'M',
    '\u019C': 'M',
    '\u24C3': 'N',
    '\uFF2E': 'N',
    '\u01F8': 'N',
    '\u0143': 'N',
    '\u00D1': 'N',
    '\u1E44': 'N',
    '\u0147': 'N',
    '\u1E46': 'N',
    '\u0145': 'N',
    '\u1E4A': 'N',
    '\u1E48': 'N',
    '\u0220': 'N',
    '\u019D': 'N',
    '\uA790': 'N',
    '\uA7A4': 'N',
    '\u01CA': 'NJ',
    '\u01CB': 'Nj',
    '\u24C4': 'O',
    '\uFF2F': 'O',
    '\u00D2': 'O',
    '\u00D3': 'O',
    '\u00D4': 'O',
    '\u1ED2': 'O',
    '\u1ED0': 'O',
    '\u1ED6': 'O',
    '\u1ED4': 'O',
    '\u00D5': 'O',
    '\u1E4C': 'O',
    '\u022C': 'O',
    '\u1E4E': 'O',
    '\u014C': 'O',
    '\u1E50': 'O',
    '\u1E52': 'O',
    '\u014E': 'O',
    '\u022E': 'O',
    '\u0230': 'O',
    '\u00D6': 'O',
    '\u022A': 'O',
    '\u1ECE': 'O',
    '\u0150': 'O',
    '\u01D1': 'O',
    '\u020C': 'O',
    '\u020E': 'O',
    '\u01A0': 'O',
    '\u1EDC': 'O',
    '\u1EDA': 'O',
    '\u1EE0': 'O',
    '\u1EDE': 'O',
    '\u1EE2': 'O',
    '\u1ECC': 'O',
    '\u1ED8': 'O',
    '\u01EA': 'O',
    '\u01EC': 'O',
    '\u00D8': 'O',
    '\u01FE': 'O',
    '\u0186': 'O',
    '\u019F': 'O',
    '\uA74A': 'O',
    '\uA74C': 'O',
    '\u0152': 'OE',
    '\u01A2': 'OI',
    '\uA74E': 'OO',
    '\u0222': 'OU',
    '\u24C5': 'P',
    '\uFF30': 'P',
    '\u1E54': 'P',
    '\u1E56': 'P',
    '\u01A4': 'P',
    '\u2C63': 'P',
    '\uA750': 'P',
    '\uA752': 'P',
    '\uA754': 'P',
    '\u24C6': 'Q',
    '\uFF31': 'Q',
    '\uA756': 'Q',
    '\uA758': 'Q',
    '\u024A': 'Q',
    '\u24C7': 'R',
    '\uFF32': 'R',
    '\u0154': 'R',
    '\u1E58': 'R',
    '\u0158': 'R',
    '\u0210': 'R',
    '\u0212': 'R',
    '\u1E5A': 'R',
    '\u1E5C': 'R',
    '\u0156': 'R',
    '\u1E5E': 'R',
    '\u024C': 'R',
    '\u2C64': 'R',
    '\uA75A': 'R',
    '\uA7A6': 'R',
    '\uA782': 'R',
    '\u24C8': 'S',
    '\uFF33': 'S',
    '\u1E9E': 'S',
    '\u015A': 'S',
    '\u1E64': 'S',
    '\u015C': 'S',
    '\u1E60': 'S',
    '\u0160': 'S',
    '\u1E66': 'S',
    '\u1E62': 'S',
    '\u1E68': 'S',
    '\u0218': 'S',
    '\u015E': 'S',
    '\u2C7E': 'S',
    '\uA7A8': 'S',
    '\uA784': 'S',
    '\u24C9': 'T',
    '\uFF34': 'T',
    '\u1E6A': 'T',
    '\u0164': 'T',
    '\u1E6C': 'T',
    '\u021A': 'T',
    '\u0162': 'T',
    '\u1E70': 'T',
    '\u1E6E': 'T',
    '\u0166': 'T',
    '\u01AC': 'T',
    '\u01AE': 'T',
    '\u023E': 'T',
    '\uA786': 'T',
    '\uA728': 'TZ',
    '\u24CA': 'U',
    '\uFF35': 'U',
    '\u00D9': 'U',
    '\u00DA': 'U',
    '\u00DB': 'U',
    '\u0168': 'U',
    '\u1E78': 'U',
    '\u016A': 'U',
    '\u1E7A': 'U',
    '\u016C': 'U',
    '\u00DC': 'U',
    '\u01DB': 'U',
    '\u01D7': 'U',
    '\u01D5': 'U',
    '\u01D9': 'U',
    '\u1EE6': 'U',
    '\u016E': 'U',
    '\u0170': 'U',
    '\u01D3': 'U',
    '\u0214': 'U',
    '\u0216': 'U',
    '\u01AF': 'U',
    '\u1EEA': 'U',
    '\u1EE8': 'U',
    '\u1EEE': 'U',
    '\u1EEC': 'U',
    '\u1EF0': 'U',
    '\u1EE4': 'U',
    '\u1E72': 'U',
    '\u0172': 'U',
    '\u1E76': 'U',
    '\u1E74': 'U',
    '\u0244': 'U',
    '\u24CB': 'V',
    '\uFF36': 'V',
    '\u1E7C': 'V',
    '\u1E7E': 'V',
    '\u01B2': 'V',
    '\uA75E': 'V',
    '\u0245': 'V',
    '\uA760': 'VY',
    '\u24CC': 'W',
    '\uFF37': 'W',
    '\u1E80': 'W',
    '\u1E82': 'W',
    '\u0174': 'W',
    '\u1E86': 'W',
    '\u1E84': 'W',
    '\u1E88': 'W',
    '\u2C72': 'W',
    '\u24CD': 'X',
    '\uFF38': 'X',
    '\u1E8A': 'X',
    '\u1E8C': 'X',
    '\u24CE': 'Y',
    '\uFF39': 'Y',
    '\u1EF2': 'Y',
    '\u00DD': 'Y',
    '\u0176': 'Y',
    '\u1EF8': 'Y',
    '\u0232': 'Y',
    '\u1E8E': 'Y',
    '\u0178': 'Y',
    '\u1EF6': 'Y',
    '\u1EF4': 'Y',
    '\u01B3': 'Y',
    '\u024E': 'Y',
    '\u1EFE': 'Y',
    '\u24CF': 'Z',
    '\uFF3A': 'Z',
    '\u0179': 'Z',
    '\u1E90': 'Z',
    '\u017B': 'Z',
    '\u017D': 'Z',
    '\u1E92': 'Z',
    '\u1E94': 'Z',
    '\u01B5': 'Z',
    '\u0224': 'Z',
    '\u2C7F': 'Z',
    '\u2C6B': 'Z',
    '\uA762': 'Z',
    '\u24D0': 'a',
    '\uFF41': 'a',
    '\u1E9A': 'a',
    '\u00E0': 'a',
    '\u00E1': 'a',
    '\u00E2': 'a',
    '\u1EA7': 'a',
    '\u1EA5': 'a',
    '\u1EAB': 'a',
    '\u1EA9': 'a',
    '\u00E3': 'a',
    '\u0101': 'a',
    '\u0103': 'a',
    '\u1EB1': 'a',
    '\u1EAF': 'a',
    '\u1EB5': 'a',
    '\u1EB3': 'a',
    '\u0227': 'a',
    '\u01E1': 'a',
    '\u00E4': 'a',
    '\u01DF': 'a',
    '\u1EA3': 'a',
    '\u00E5': 'a',
    '\u01FB': 'a',
    '\u01CE': 'a',
    '\u0201': 'a',
    '\u0203': 'a',
    '\u1EA1': 'a',
    '\u1EAD': 'a',
    '\u1EB7': 'a',
    '\u1E01': 'a',
    '\u0105': 'a',
    '\u2C65': 'a',
    '\u0250': 'a',
    '\uA733': 'aa',
    '\u00E6': 'ae',
    '\u01FD': 'ae',
    '\u01E3': 'ae',
    '\uA735': 'ao',
    '\uA737': 'au',
    '\uA739': 'av',
    '\uA73B': 'av',
    '\uA73D': 'ay',
    '\u24D1': 'b',
    '\uFF42': 'b',
    '\u1E03': 'b',
    '\u1E05': 'b',
    '\u1E07': 'b',
    '\u0180': 'b',
    '\u0183': 'b',
    '\u0253': 'b',
    '\u24D2': 'c',
    '\uFF43': 'c',
    '\u0107': 'c',
    '\u0109': 'c',
    '\u010B': 'c',
    '\u010D': 'c',
    '\u00E7': 'c',
    '\u1E09': 'c',
    '\u0188': 'c',
    '\u023C': 'c',
    '\uA73F': 'c',
    '\u2184': 'c',
    '\u24D3': 'd',
    '\uFF44': 'd',
    '\u1E0B': 'd',
    '\u010F': 'd',
    '\u1E0D': 'd',
    '\u1E11': 'd',
    '\u1E13': 'd',
    '\u1E0F': 'd',
    '\u0111': 'd',
    '\u018C': 'd',
    '\u0256': 'd',
    '\u0257': 'd',
    '\uA77A': 'd',
    '\u01F3': 'dz',
    '\u01C6': 'dz',
    '\u24D4': 'e',
    '\uFF45': 'e',
    '\u00E8': 'e',
    '\u00E9': 'e',
    '\u00EA': 'e',
    '\u1EC1': 'e',
    '\u1EBF': 'e',
    '\u1EC5': 'e',
    '\u1EC3': 'e',
    '\u1EBD': 'e',
    '\u0113': 'e',
    '\u1E15': 'e',
    '\u1E17': 'e',
    '\u0115': 'e',
    '\u0117': 'e',
    '\u00EB': 'e',
    '\u1EBB': 'e',
    '\u011B': 'e',
    '\u0205': 'e',
    '\u0207': 'e',
    '\u1EB9': 'e',
    '\u1EC7': 'e',
    '\u0229': 'e',
    '\u1E1D': 'e',
    '\u0119': 'e',
    '\u1E19': 'e',
    '\u1E1B': 'e',
    '\u0247': 'e',
    '\u025B': 'e',
    '\u01DD': 'e',
    '\u24D5': 'f',
    '\uFF46': 'f',
    '\u1E1F': 'f',
    '\u0192': 'f',
    '\uA77C': 'f',
    '\u24D6': 'g',
    '\uFF47': 'g',
    '\u01F5': 'g',
    '\u011D': 'g',
    '\u1E21': 'g',
    '\u011F': 'g',
    '\u0121': 'g',
    '\u01E7': 'g',
    '\u0123': 'g',
    '\u01E5': 'g',
    '\u0260': 'g',
    '\uA7A1': 'g',
    '\u1D79': 'g',
    '\uA77F': 'g',
    '\u24D7': 'h',
    '\uFF48': 'h',
    '\u0125': 'h',
    '\u1E23': 'h',
    '\u1E27': 'h',
    '\u021F': 'h',
    '\u1E25': 'h',
    '\u1E29': 'h',
    '\u1E2B': 'h',
    '\u1E96': 'h',
    '\u0127': 'h',
    '\u2C68': 'h',
    '\u2C76': 'h',
    '\u0265': 'h',
    '\u0195': 'hv',
    '\u24D8': 'i',
    '\uFF49': 'i',
    '\u00EC': 'i',
    '\u00ED': 'i',
    '\u00EE': 'i',
    '\u0129': 'i',
    '\u012B': 'i',
    '\u012D': 'i',
    '\u00EF': 'i',
    '\u1E2F': 'i',
    '\u1EC9': 'i',
    '\u01D0': 'i',
    '\u0209': 'i',
    '\u020B': 'i',
    '\u1ECB': 'i',
    '\u012F': 'i',
    '\u1E2D': 'i',
    '\u0268': 'i',
    '\u0131': 'i',
    '\u24D9': 'j',
    '\uFF4A': 'j',
    '\u0135': 'j',
    '\u01F0': 'j',
    '\u0249': 'j',
    '\u24DA': 'k',
    '\uFF4B': 'k',
    '\u1E31': 'k',
    '\u01E9': 'k',
    '\u1E33': 'k',
    '\u0137': 'k',
    '\u1E35': 'k',
    '\u0199': 'k',
    '\u2C6A': 'k',
    '\uA741': 'k',
    '\uA743': 'k',
    '\uA745': 'k',
    '\uA7A3': 'k',
    '\u24DB': 'l',
    '\uFF4C': 'l',
    '\u0140': 'l',
    '\u013A': 'l',
    '\u013E': 'l',
    '\u1E37': 'l',
    '\u1E39': 'l',
    '\u013C': 'l',
    '\u1E3D': 'l',
    '\u1E3B': 'l',
    '\u017F': 'l',
    '\u0142': 'l',
    '\u019A': 'l',
    '\u026B': 'l',
    '\u2C61': 'l',
    '\uA749': 'l',
    '\uA781': 'l',
    '\uA747': 'l',
    '\u01C9': 'lj',
    '\u24DC': 'm',
    '\uFF4D': 'm',
    '\u1E3F': 'm',
    '\u1E41': 'm',
    '\u1E43': 'm',
    '\u0271': 'm',
    '\u026F': 'm',
    '\u24DD': 'n',
    '\uFF4E': 'n',
    '\u01F9': 'n',
    '\u0144': 'n',
    '\u00F1': 'n',
    '\u1E45': 'n',
    '\u0148': 'n',
    '\u1E47': 'n',
    '\u0146': 'n',
    '\u1E4B': 'n',
    '\u1E49': 'n',
    '\u019E': 'n',
    '\u0272': 'n',
    '\u0149': 'n',
    '\uA791': 'n',
    '\uA7A5': 'n',
    '\u01CC': 'nj',
    '\u24DE': 'o',
    '\uFF4F': 'o',
    '\u00F2': 'o',
    '\u00F3': 'o',
    '\u00F4': 'o',
    '\u1ED3': 'o',
    '\u1ED1': 'o',
    '\u1ED7': 'o',
    '\u1ED5': 'o',
    '\u00F5': 'o',
    '\u1E4D': 'o',
    '\u022D': 'o',
    '\u1E4F': 'o',
    '\u014D': 'o',
    '\u1E51': 'o',
    '\u1E53': 'o',
    '\u014F': 'o',
    '\u022F': 'o',
    '\u0231': 'o',
    '\u00F6': 'o',
    '\u022B': 'o',
    '\u1ECF': 'o',
    '\u0151': 'o',
    '\u01D2': 'o',
    '\u020D': 'o',
    '\u020F': 'o',
    '\u01A1': 'o',
    '\u1EDD': 'o',
    '\u1EDB': 'o',
    '\u1EE1': 'o',
    '\u1EDF': 'o',
    '\u1EE3': 'o',
    '\u1ECD': 'o',
    '\u1ED9': 'o',
    '\u01EB': 'o',
    '\u01ED': 'o',
    '\u00F8': 'o',
    '\u01FF': 'o',
    '\u0254': 'o',
    '\uA74B': 'o',
    '\uA74D': 'o',
    '\u0275': 'o',
    '\u0153': 'oe',
    '\u01A3': 'oi',
    '\u0223': 'ou',
    '\uA74F': 'oo',
    '\u24DF': 'p',
    '\uFF50': 'p',
    '\u1E55': 'p',
    '\u1E57': 'p',
    '\u01A5': 'p',
    '\u1D7D': 'p',
    '\uA751': 'p',
    '\uA753': 'p',
    '\uA755': 'p',
    '\u24E0': 'q',
    '\uFF51': 'q',
    '\u024B': 'q',
    '\uA757': 'q',
    '\uA759': 'q',
    '\u24E1': 'r',
    '\uFF52': 'r',
    '\u0155': 'r',
    '\u1E59': 'r',
    '\u0159': 'r',
    '\u0211': 'r',
    '\u0213': 'r',
    '\u1E5B': 'r',
    '\u1E5D': 'r',
    '\u0157': 'r',
    '\u1E5F': 'r',
    '\u024D': 'r',
    '\u027D': 'r',
    '\uA75B': 'r',
    '\uA7A7': 'r',
    '\uA783': 'r',
    '\u24E2': 's',
    '\uFF53': 's',
    '\u00DF': 's',
    '\u015B': 's',
    '\u1E65': 's',
    '\u015D': 's',
    '\u1E61': 's',
    '\u0161': 's',
    '\u1E67': 's',
    '\u1E63': 's',
    '\u1E69': 's',
    '\u0219': 's',
    '\u015F': 's',
    '\u023F': 's',
    '\uA7A9': 's',
    '\uA785': 's',
    '\u1E9B': 's',
    '\u24E3': 't',
    '\uFF54': 't',
    '\u1E6B': 't',
    '\u1E97': 't',
    '\u0165': 't',
    '\u1E6D': 't',
    '\u021B': 't',
    '\u0163': 't',
    '\u1E71': 't',
    '\u1E6F': 't',
    '\u0167': 't',
    '\u01AD': 't',
    '\u0288': 't',
    '\u2C66': 't',
    '\uA787': 't',
    '\uA729': 'tz',
    '\u24E4': 'u',
    '\uFF55': 'u',
    '\u00F9': 'u',
    '\u00FA': 'u',
    '\u00FB': 'u',
    '\u0169': 'u',
    '\u1E79': 'u',
    '\u016B': 'u',
    '\u1E7B': 'u',
    '\u016D': 'u',
    '\u00FC': 'u',
    '\u01DC': 'u',
    '\u01D8': 'u',
    '\u01D6': 'u',
    '\u01DA': 'u',
    '\u1EE7': 'u',
    '\u016F': 'u',
    '\u0171': 'u',
    '\u01D4': 'u',
    '\u0215': 'u',
    '\u0217': 'u',
    '\u01B0': 'u',
    '\u1EEB': 'u',
    '\u1EE9': 'u',
    '\u1EEF': 'u',
    '\u1EED': 'u',
    '\u1EF1': 'u',
    '\u1EE5': 'u',
    '\u1E73': 'u',
    '\u0173': 'u',
    '\u1E77': 'u',
    '\u1E75': 'u',
    '\u0289': 'u',
    '\u24E5': 'v',
    '\uFF56': 'v',
    '\u1E7D': 'v',
    '\u1E7F': 'v',
    '\u028B': 'v',
    '\uA75F': 'v',
    '\u028C': 'v',
    '\uA761': 'vy',
    '\u24E6': 'w',
    '\uFF57': 'w',
    '\u1E81': 'w',
    '\u1E83': 'w',
    '\u0175': 'w',
    '\u1E87': 'w',
    '\u1E85': 'w',
    '\u1E98': 'w',
    '\u1E89': 'w',
    '\u2C73': 'w',
    '\u24E7': 'x',
    '\uFF58': 'x',
    '\u1E8B': 'x',
    '\u1E8D': 'x',
    '\u24E8': 'y',
    '\uFF59': 'y',
    '\u1EF3': 'y',
    '\u00FD': 'y',
    '\u0177': 'y',
    '\u1EF9': 'y',
    '\u0233': 'y',
    '\u1E8F': 'y',
    '\u00FF': 'y',
    '\u1EF7': 'y',
    '\u1E99': 'y',
    '\u1EF5': 'y',
    '\u01B4': 'y',
    '\u024F': 'y',
    '\u1EFF': 'y',
    '\u24E9': 'z',
    '\uFF5A': 'z',
    '\u017A': 'z',
    '\u1E91': 'z',
    '\u017C': 'z',
    '\u017E': 'z',
    '\u1E93': 'z',
    '\u1E95': 'z',
    '\u01B6': 'z',
    '\u0225': 'z',
    '\u0240': 'z',
    '\u2C6C': 'z',
    '\uA763': 'z',
    '\u0386': '\u0391',
    '\u0388': '\u0395',
    '\u0389': '\u0397',
    '\u038A': '\u0399',
    '\u03AA': '\u0399',
    '\u038C': '\u039F',
    '\u038E': '\u03A5',
    '\u03AB': '\u03A5',
    '\u038F': '\u03A9',
    '\u03AC': '\u03B1',
    '\u03AD': '\u03B5',
    '\u03AE': '\u03B7',
    '\u03AF': '\u03B9',
    '\u03CA': '\u03B9',
    '\u0390': '\u03B9',
    '\u03CC': '\u03BF',
    '\u03CD': '\u03C5',
    '\u03CB': '\u03C5',
    '\u03B0': '\u03C5',
    '\u03CE': '\u03C9',
    '\u03C2': '\u03C3',
    '\u2019': '\''
  };

  return diacritics;
});

S2.define('select2/data/base',[
  '../utils'
], function (Utils) {
  function BaseAdapter ($element, options) {
    BaseAdapter.__super__.constructor.call(this);
  }

  Utils.Extend(BaseAdapter, Utils.Observable);

  BaseAdapter.prototype.current = function (callback) {
    throw new Error('The `current` method must be defined in child classes.');
  };

  BaseAdapter.prototype.query = function (params, callback) {
    throw new Error('The `query` method must be defined in child classes.');
  };

  BaseAdapter.prototype.bind = function (container, $container) {
    // Can be implemented in subclasses
  };

  BaseAdapter.prototype.destroy = function () {
    // Can be implemented in subclasses
  };

  BaseAdapter.prototype.generateResultId = function (container, data) {
    var id = container.id + '-result-';

    id += Utils.generateChars(4);

    if (data.id != null) {
      id += '-' + data.id.toString();
    } else {
      id += '-' + Utils.generateChars(4);
    }
    return id;
  };

  return BaseAdapter;
});

S2.define('select2/data/select',[
  './base',
  '../utils',
  'jquery'
], function (BaseAdapter, Utils, $) {
  function SelectAdapter ($element, options) {
    this.$element = $element;
    this.options = options;

    SelectAdapter.__super__.constructor.call(this);
  }

  Utils.Extend(SelectAdapter, BaseAdapter);

  SelectAdapter.prototype.current = function (callback) {
    var self = this;

    var data = Array.prototype.map.call(
      this.$element[0].querySelectorAll(':checked'),
      function (selectedElement) {
        return self.item($(selectedElement));
      }
    );

    callback(data);
  };

  SelectAdapter.prototype.select = function (data) {
    var self = this;

    data.selected = true;

    // If data.element is a DOM node, use it instead
    if (
      data.element != null && data.element.tagName.toLowerCase() === 'option'
    ) {
      data.element.selected = true;

      this.$element.trigger('input').trigger('change');

      return;
    }

    if (this.$element.prop('multiple')) {
      this.current(function (currentData) {
        var val = [];

        data = [data];
        data.push.apply(data, currentData);

        for (var d = 0; d < data.length; d++) {
          var id = data[d].id;

          if (val.indexOf(id) === -1) {
            val.push(id);
          }
        }

        self.$element.val(val);
        self.$element.trigger('input').trigger('change');
      });
    } else {
      var val = data.id;

      this.$element.val(val);
      this.$element.trigger('input').trigger('change');
    }
  };

  SelectAdapter.prototype.unselect = function (data) {
    var self = this;

    if (!this.$element.prop('multiple')) {
      return;
    }

    data.selected = false;

    if (
      data.element != null &&
      data.element.tagName.toLowerCase() === 'option'
    ) {
      data.element.selected = false;

      this.$element.trigger('input').trigger('change');

      return;
    }

    this.current(function (currentData) {
      var val = [];

      for (var d = 0; d < currentData.length; d++) {
        var id = currentData[d].id;

        if (id !== data.id && val.indexOf(id) === -1) {
          val.push(id);
        }
      }

      self.$element.val(val);

      self.$element.trigger('input').trigger('change');
    });
  };

  SelectAdapter.prototype.bind = function (container, $container) {
    var self = this;

    this.container = container;

    container.on('select', function (params) {
      self.select(params.data);
    });

    container.on('unselect', function (params) {
      self.unselect(params.data);
    });
  };

  SelectAdapter.prototype.destroy = function () {
    // Remove anything added to child elements
    this.$element.find('*').each(function () {
      // Remove any custom data set by Select2
      Utils.RemoveData(this);
    });
  };

  SelectAdapter.prototype.query = function (params, callback) {
    var data = [];
    var self = this;

    var $options = this.$element.children();

    $options.each(function () {
      if (
        this.tagName.toLowerCase() !== 'option' &&
        this.tagName.toLowerCase() !== 'optgroup'
      ) {
        return;
      }

      var $option = $(this);

      var option = self.item($option);

      var matches = self.matches(params, option);

      if (matches !== null) {
        data.push(matches);
      }
    });

    callback({
      results: data
    });
  };

  SelectAdapter.prototype.addOptions = function ($options) {
    this.$element.append($options);
  };

  SelectAdapter.prototype.option = function (data) {
    var option;

    if (data.children) {
      option = document.createElement('optgroup');
      option.label = data.text;
    } else {
      option = document.createElement('option');

      if (option.textContent !== undefined) {
        option.textContent = data.text;
      } else {
        option.innerText = data.text;
      }
    }

    if (data.id !== undefined) {
      option.value = data.id;
    }

    if (data.disabled) {
      option.disabled = true;
    }

    if (data.selected) {
      option.selected = true;
    }

    if (data.title) {
      option.title = data.title;
    }

    var normalizedData = this._normalizeItem(data);
    normalizedData.element = option;

    // Override the option's data with the combined data
    Utils.StoreData(option, 'data', normalizedData);

    return $(option);
  };

  SelectAdapter.prototype.item = function ($option) {
    var data = {};

    data = Utils.GetData($option[0], 'data');

    if (data != null) {
      return data;
    }

    var option = $option[0];

    if (option.tagName.toLowerCase() === 'option') {
      data = {
        id: $option.val(),
        text: $option.text(),
        disabled: $option.prop('disabled'),
        selected: $option.prop('selected'),
        title: $option.prop('title')
      };
    } else if (option.tagName.toLowerCase() === 'optgroup') {
      data = {
        text: $option.prop('label'),
        children: [],
        title: $option.prop('title')
      };

      var $children = $option.children('option');
      var children = [];

      for (var c = 0; c < $children.length; c++) {
        var $child = $($children[c]);

        var child = this.item($child);

        children.push(child);
      }

      data.children = children;
    }

    data = this._normalizeItem(data);
    data.element = $option[0];

    Utils.StoreData($option[0], 'data', data);

    return data;
  };

  SelectAdapter.prototype._normalizeItem = function (item) {
    if (item !== Object(item)) {
      item = {
        id: item,
        text: item
      };
    }

    item = $.extend({}, {
      text: ''
    }, item);

    var defaults = {
      selected: false,
      disabled: false
    };

    if (item.id != null) {
      item.id = item.id.toString();
    }

    if (item.text != null) {
      item.text = item.text.toString();
    }

    if (item._resultId == null && item.id && this.container != null) {
      item._resultId = this.generateResultId(this.container, item);
    }

    return $.extend({}, defaults, item);
  };

  SelectAdapter.prototype.matches = function (params, data) {
    var matcher = this.options.get('matcher');

    return matcher(params, data);
  };

  return SelectAdapter;
});

S2.define('select2/data/array',[
  './select',
  '../utils',
  'jquery'
], function (SelectAdapter, Utils, $) {
  function ArrayAdapter ($element, options) {
    this._dataToConvert = options.get('data') || [];

    ArrayAdapter.__super__.constructor.call(this, $element, options);
  }

  Utils.Extend(ArrayAdapter, SelectAdapter);

  ArrayAdapter.prototype.bind = function (container, $container) {
    ArrayAdapter.__super__.bind.call(this, container, $container);

    this.addOptions(this.convertToOptions(this._dataToConvert));
  };

  ArrayAdapter.prototype.select = function (data) {
    var $option = this.$element.find('option').filter(function (i, elm) {
      return elm.value == data.id.toString();
    });

    if ($option.length === 0) {
      $option = this.option(data);

      this.addOptions($option);
    }

    ArrayAdapter.__super__.select.call(this, data);
  };

  ArrayAdapter.prototype.convertToOptions = function (data) {
    var self = this;

    var $existing = this.$element.find('option');
    var existingIds = $existing.map(function () {
      return self.item($(this)).id;
    }).get();

    var $options = [];

    // Filter out all items except for the one passed in the argument
    function onlyItem (item) {
      return function () {
        return $(this).val() == item.id;
      };
    }

    for (var d = 0; d < data.length; d++) {
      var item = this._normalizeItem(data[d]);

      // Skip items which were pre-loaded, only merge the data
      if (existingIds.indexOf(item.id) >= 0) {
        var $existingOption = $existing.filter(onlyItem(item));

        var existingData = this.item($existingOption);
        var newData = $.extend(true, {}, item, existingData);

        var $newOption = this.option(newData);

        $existingOption.replaceWith($newOption);

        continue;
      }

      var $option = this.option(item);

      if (item.children) {
        var $children = this.convertToOptions(item.children);

        $option.append($children);
      }

      $options.push($option);
    }

    return $options;
  };

  return ArrayAdapter;
});

S2.define('select2/data/ajax',[
  './array',
  '../utils',
  'jquery'
], function (ArrayAdapter, Utils, $) {
  function AjaxAdapter ($element, options) {
    this.ajaxOptions = this._applyDefaults(options.get('ajax'));

    if (this.ajaxOptions.processResults != null) {
      this.processResults = this.ajaxOptions.processResults;
    }

    AjaxAdapter.__super__.constructor.call(this, $element, options);
  }

  Utils.Extend(AjaxAdapter, ArrayAdapter);

  AjaxAdapter.prototype._applyDefaults = function (options) {
    var defaults = {
      data: function (params) {
        return $.extend({}, params, {
          q: params.term
        });
      },
      transport: function (params, success, failure) {
        var $request = $.ajax(params);

        $request.then(success);
        $request.fail(failure);

        return $request;
      }
    };

    return $.extend({}, defaults, options, true);
  };

  AjaxAdapter.prototype.processResults = function (results) {
    return results;
  };

  AjaxAdapter.prototype.query = function (params, callback) {
    var matches = [];
    var self = this;

    if (this._request != null) {
      // JSONP requests cannot always be aborted
      if ($.isFunction(this._request.abort)) {
        this._request.abort();
      }

      this._request = null;
    }

    var options = $.extend({
      type: 'GET'
    }, this.ajaxOptions);

    if (typeof options.url === 'function') {
      options.url = options.url.call(this.$element, params);
    }

    if (typeof options.data === 'function') {
      options.data = options.data.call(this.$element, params);
    }

    function request () {
      var $request = options.transport(options, function (data) {
        var results = self.processResults(data, params);

        if (self.options.get('debug') && window.console && console.error) {
          // Check to make sure that the response included a `results` key.
          if (!results || !results.results || !Array.isArray(results.results)) {
            console.error(
              'Select2: The AJAX results did not return an array in the ' +
              '`results` key of the response.'
            );
          }
        }

        callback(results);
      }, function () {
        // Attempt to detect if a request was aborted
        // Only works if the transport exposes a status property
        if ('status' in $request &&
            ($request.status === 0 || $request.status === '0')) {
          return;
        }

        self.trigger('results:message', {
          message: 'errorLoading'
        });
      });

      self._request = $request;
    }

    if (this.ajaxOptions.delay && params.term != null) {
      if (this._queryTimeout) {
        window.clearTimeout(this._queryTimeout);
      }

      this._queryTimeout = window.setTimeout(request, this.ajaxOptions.delay);
    } else {
      request();
    }
  };

  return AjaxAdapter;
});

S2.define('select2/data/tags',[
  'jquery'
], function ($) {
  function Tags (decorated, $element, options) {
    var tags = options.get('tags');

    var createTag = options.get('createTag');

    if (createTag !== undefined) {
      this.createTag = createTag;
    }

    var insertTag = options.get('insertTag');

    if (insertTag !== undefined) {
        this.insertTag = insertTag;
    }

    decorated.call(this, $element, options);

    if (Array.isArray(tags)) {
      for (var t = 0; t < tags.length; t++) {
        var tag = tags[t];
        var item = this._normalizeItem(tag);

        var $option = this.option(item);

        this.$element.append($option);
      }
    }
  }

  Tags.prototype.query = function (decorated, params, callback) {
    var self = this;

    this._removeOldTags();

    if (params.term == null || params.page != null) {
      decorated.call(this, params, callback);
      return;
    }

    function wrapper (obj, child) {
      var data = obj.results;

      for (var i = 0; i < data.length; i++) {
        var option = data[i];

        var checkChildren = (
          option.children != null &&
          !wrapper({
            results: option.children
          }, true)
        );

        var optionText = (option.text || '').toUpperCase();
        var paramsTerm = (params.term || '').toUpperCase();

        var checkText = optionText === paramsTerm;

        if (checkText || checkChildren) {
          if (child) {
            return false;
          }

          obj.data = data;
          callback(obj);

          return;
        }
      }

      if (child) {
        return true;
      }

      var tag = self.createTag(params);

      if (tag != null) {
        var $option = self.option(tag);
        $option.attr('data-select2-tag', true);

        self.addOptions([$option]);

        self.insertTag(data, tag);
      }

      obj.results = data;

      callback(obj);
    }

    decorated.call(this, params, wrapper);
  };

  Tags.prototype.createTag = function (decorated, params) {
    if (params.term == null) {
      return null;
    }

    var term = params.term.trim();

    if (term === '') {
      return null;
    }

    return {
      id: term,
      text: term
    };
  };

  Tags.prototype.insertTag = function (_, data, tag) {
    data.unshift(tag);
  };

  Tags.prototype._removeOldTags = function (_) {
    var $options = this.$element.find('option[data-select2-tag]');

    $options.each(function () {
      if (this.selected) {
        return;
      }

      $(this).remove();
    });
  };

  return Tags;
});

S2.define('select2/data/tokenizer',[
  'jquery'
], function ($) {
  function Tokenizer (decorated, $element, options) {
    var tokenizer = options.get('tokenizer');

    if (tokenizer !== undefined) {
      this.tokenizer = tokenizer;
    }

    decorated.call(this, $element, options);
  }

  Tokenizer.prototype.bind = function (decorated, container, $container) {
    decorated.call(this, container, $container);

    this.$search =  container.dropdown.$search || container.selection.$search ||
      $container.find('.select2-search__field');
  };

  Tokenizer.prototype.query = function (decorated, params, callback) {
    var self = this;

    function createAndSelect (data) {
      // Normalize the data object so we can use it for checks
      var item = self._normalizeItem(data);

      // Check if the data object already exists as a tag
      // Select it if it doesn't
      var $existingOptions = self.$element.find('option').filter(function () {
        return $(this).val() === item.id;
      });

      // If an existing option wasn't found for it, create the option
      if (!$existingOptions.length) {
        var $option = self.option(item);
        $option.attr('data-select2-tag', true);

        self._removeOldTags();
        self.addOptions([$option]);
      }

      // Select the item, now that we know there is an option for it
      select(item);
    }

    function select (data) {
      self.trigger('select', {
        data: data
      });
    }

    params.term = params.term || '';

    var tokenData = this.tokenizer(params, this.options, createAndSelect);

    if (tokenData.term !== params.term) {
      // Replace the search term if we have the search box
      if (this.$search.length) {
        this.$search.val(tokenData.term);
        this.$search.trigger('focus');
      }

      params.term = tokenData.term;
    }

    decorated.call(this, params, callback);
  };

  Tokenizer.prototype.tokenizer = function (_, params, options, callback) {
    var separators = options.get('tokenSeparators') || [];
    var term = params.term;
    var i = 0;

    var createTag = this.createTag || function (params) {
      return {
        id: params.term,
        text: params.term
      };
    };

    while (i < term.length) {
      var termChar = term[i];

      if (separators.indexOf(termChar) === -1) {
        i++;

        continue;
      }

      var part = term.substr(0, i);
      var partParams = $.extend({}, params, {
        term: part
      });

      var data = createTag(partParams);

      if (data == null) {
        i++;
        continue;
      }

      callback(data);

      // Reset the term to not include the tokenized portion
      term = term.substr(i + 1) || '';
      i = 0;
    }

    return {
      term: term
    };
  };

  return Tokenizer;
});

S2.define('select2/data/minimumInputLength',[

], function () {
  function MinimumInputLength (decorated, $e, options) {
    this.minimumInputLength = options.get('minimumInputLength');

    decorated.call(this, $e, options);
  }

  MinimumInputLength.prototype.query = function (decorated, params, callback) {
    params.term = params.term || '';

    if (params.term.length < this.minimumInputLength) {
      this.trigger('results:message', {
        message: 'inputTooShort',
        args: {
          minimum: this.minimumInputLength,
          input: params.term,
          params: params
        }
      });

      return;
    }

    decorated.call(this, params, callback);
  };

  return MinimumInputLength;
});

S2.define('select2/data/maximumInputLength',[

], function () {
  function MaximumInputLength (decorated, $e, options) {
    this.maximumInputLength = options.get('maximumInputLength');

    decorated.call(this, $e, options);
  }

  MaximumInputLength.prototype.query = function (decorated, params, callback) {
    params.term = params.term || '';

    if (this.maximumInputLength > 0 &&
        params.term.length > this.maximumInputLength) {
      this.trigger('results:message', {
        message: 'inputTooLong',
        args: {
          maximum: this.maximumInputLength,
          input: params.term,
          params: params
        }
      });

      return;
    }

    decorated.call(this, params, callback);
  };

  return MaximumInputLength;
});

S2.define('select2/data/maximumSelectionLength',[

], function (){
  function MaximumSelectionLength (decorated, $e, options) {
    this.maximumSelectionLength = options.get('maximumSelectionLength');

    decorated.call(this, $e, options);
  }

  MaximumSelectionLength.prototype.bind =
    function (decorated, container, $container) {
      var self = this;

      decorated.call(this, container, $container);

      container.on('select', function () {
        self._checkIfMaximumSelected();
      });
  };

  MaximumSelectionLength.prototype.query =
    function (decorated, params, callback) {
      var self = this;

      this._checkIfMaximumSelected(function () {
        decorated.call(self, params, callback);
      });
  };

  MaximumSelectionLength.prototype._checkIfMaximumSelected =
    function (_, successCallback) {
      var self = this;

      this.current(function (currentData) {
        var count = currentData != null ? currentData.length : 0;
        if (self.maximumSelectionLength > 0 &&
          count >= self.maximumSelectionLength) {
          self.trigger('results:message', {
            message: 'maximumSelected',
            args: {
              maximum: self.maximumSelectionLength
            }
          });
          return;
        }

        if (successCallback) {
          successCallback();
        }
      });
  };

  return MaximumSelectionLength;
});

S2.define('select2/dropdown',[
  'jquery',
  './utils'
], function ($, Utils) {
  function Dropdown ($element, options) {
    this.$element = $element;
    this.options = options;

    Dropdown.__super__.constructor.call(this);
  }

  Utils.Extend(Dropdown, Utils.Observable);

  Dropdown.prototype.render = function () {
    var $dropdown = $(
      '<span class="select2-dropdown">' +
        '<span class="select2-results"></span>' +
      '</span>'
    );

    $dropdown.attr('dir', this.options.get('dir'));

    this.$dropdown = $dropdown;

    return $dropdown;
  };

  Dropdown.prototype.bind = function () {
    // Should be implemented in subclasses
  };

  Dropdown.prototype.position = function ($dropdown, $container) {
    // Should be implemented in subclasses
  };

  Dropdown.prototype.destroy = function () {
    // Remove the dropdown from the DOM
    this.$dropdown.remove();
  };

  return Dropdown;
});

S2.define('select2/dropdown/search',[
  'jquery'
], function ($) {
  function Search () { }

  Search.prototype.render = function (decorated) {
    var $rendered = decorated.call(this);

    var $search = $(
      '<span class="select2-search select2-search--dropdown">' +
        '<input class="select2-search__field" type="search" tabindex="-1"' +
        ' autocorrect="off" autocapitalize="none"' +
        ' spellcheck="false" role="searchbox" aria-autocomplete="list" />' +
      '</span>'
    );

    this.$searchContainer = $search;
    this.$search = $search.find('input');

    this.$search.prop('autocomplete', this.options.get('autocomplete'));

    $rendered.prepend($search);

    return $rendered;
  };

  Search.prototype.bind = function (decorated, container, $container) {
    var self = this;

    var resultsId = container.id + '-results';

    decorated.call(this, container, $container);

    this.$search.on('keydown', function (evt) {
      self.trigger('keypress', evt);

      self._keyUpPrevented = evt.isDefaultPrevented();
    });

    // Workaround for browsers which do not support the `input` event
    // This will prevent double-triggering of events for browsers which support
    // both the `keyup` and `input` events.
    this.$search.on('input', function (evt) {
      // Unbind the duplicated `keyup` event
      $(this).off('keyup');
    });

    this.$search.on('keyup input', function (evt) {
      self.handleSearch(evt);
    });

    container.on('open', function () {
      self.$search.attr('tabindex', 0);
      self.$search.attr('aria-controls', resultsId);

      self.$search.trigger('focus');

      window.setTimeout(function () {
        self.$search.trigger('focus');
      }, 0);
    });

    container.on('close', function () {
      self.$search.attr('tabindex', -1);
      self.$search.removeAttr('aria-controls');
      self.$search.removeAttr('aria-activedescendant');

      self.$search.val('');
      self.$search.trigger('blur');
    });

    container.on('focus', function () {
      if (!container.isOpen()) {
        self.$search.trigger('focus');
      }
    });

    container.on('results:all', function (params) {
      if (params.query.term == null || params.query.term === '') {
        var showSearch = self.showSearch(params);

        if (showSearch) {
          self.$searchContainer[0].classList.remove('select2-search--hide');
        } else {
          self.$searchContainer[0].classList.add('select2-search--hide');
        }
      }
    });

    container.on('results:focus', function (params) {
      if (params.data._resultId) {
        self.$search.attr('aria-activedescendant', params.data._resultId);
      } else {
        self.$search.removeAttr('aria-activedescendant');
      }
    });
  };

  Search.prototype.handleSearch = function (evt) {
    if (!this._keyUpPrevented) {
      var input = this.$search.val();

      this.trigger('query', {
        term: input
      });
    }

    this._keyUpPrevented = false;
  };

  Search.prototype.showSearch = function (_, params) {
    return true;
  };

  return Search;
});

S2.define('select2/dropdown/hidePlaceholder',[

], function () {
  function HidePlaceholder (decorated, $element, options, dataAdapter) {
    this.placeholder = this.normalizePlaceholder(options.get('placeholder'));

    decorated.call(this, $element, options, dataAdapter);
  }

  HidePlaceholder.prototype.append = function (decorated, data) {
    data.results = this.removePlaceholder(data.results);

    decorated.call(this, data);
  };

  HidePlaceholder.prototype.normalizePlaceholder = function (_, placeholder) {
    if (typeof placeholder === 'string') {
      placeholder = {
        id: '',
        text: placeholder
      };
    }

    return placeholder;
  };

  HidePlaceholder.prototype.removePlaceholder = function (_, data) {
    var modifiedData = data.slice(0);

    for (var d = data.length - 1; d >= 0; d--) {
      var item = data[d];

      if (this.placeholder.id === item.id) {
        modifiedData.splice(d, 1);
      }
    }

    return modifiedData;
  };

  return HidePlaceholder;
});

S2.define('select2/dropdown/infiniteScroll',[
  'jquery'
], function ($) {
  function InfiniteScroll (decorated, $element, options, dataAdapter) {
    this.lastParams = {};

    decorated.call(this, $element, options, dataAdapter);

    this.$loadingMore = this.createLoadingMore();
    this.loading = false;
  }

  InfiniteScroll.prototype.append = function (decorated, data) {
    this.$loadingMore.remove();
    this.loading = false;

    decorated.call(this, data);

    if (this.showLoadingMore(data)) {
      this.$results.append(this.$loadingMore);
      this.loadMoreIfNeeded();
    }
  };

  InfiniteScroll.prototype.bind = function (decorated, container, $container) {
    var self = this;

    decorated.call(this, container, $container);

    container.on('query', function (params) {
      self.lastParams = params;
      self.loading = true;
    });

    container.on('query:append', function (params) {
      self.lastParams = params;
      self.loading = true;
    });

    this.$results.on('scroll', this.loadMoreIfNeeded.bind(this));
  };

  InfiniteScroll.prototype.loadMoreIfNeeded = function () {
    var isLoadMoreVisible = $.contains(
      document.documentElement,
      this.$loadingMore[0]
    );

    if (this.loading || !isLoadMoreVisible) {
      return;
    }

    var currentOffset = this.$results.offset().top +
      this.$results.outerHeight(false);
    var loadingMoreOffset = this.$loadingMore.offset().top +
      this.$loadingMore.outerHeight(false);

    if (currentOffset + 50 >= loadingMoreOffset) {
      this.loadMore();
    }
  };

  InfiniteScroll.prototype.loadMore = function () {
    this.loading = true;

    var params = $.extend({}, {page: 1}, this.lastParams);

    params.page++;

    this.trigger('query:append', params);
  };

  InfiniteScroll.prototype.showLoadingMore = function (_, data) {
    return data.pagination && data.pagination.more;
  };

  InfiniteScroll.prototype.createLoadingMore = function () {
    var $option = $(
      '<li ' +
      'class="select2-results__option select2-results__option--load-more"' +
      'role="option" aria-disabled="true"></li>'
    );

    var message = this.options.get('translations').get('loadingMore');

    $option.html(message(this.lastParams));

    return $option;
  };

  return InfiniteScroll;
});

S2.define('select2/dropdown/attachBody',[
  'jquery',
  '../utils'
], function ($, Utils) {
  function AttachBody (decorated, $element, options) {
    this.$dropdownParent = $(options.get('dropdownParent') || document.body);

    decorated.call(this, $element, options);
  }

  AttachBody.prototype.bind = function (decorated, container, $container) {
    var self = this;

    decorated.call(this, container, $container);

    container.on('open', function () {
      self._showDropdown();
      self._attachPositioningHandler(container);

      // Must bind after the results handlers to ensure correct sizing
      self._bindContainerResultHandlers(container);
    });

    container.on('close', function () {
      self._hideDropdown();
      self._detachPositioningHandler(container);
    });

    this.$dropdownContainer.on('mousedown', function (evt) {
      evt.stopPropagation();
    });
  };

  AttachBody.prototype.destroy = function (decorated) {
    decorated.call(this);

    this.$dropdownContainer.remove();
  };

  AttachBody.prototype.position = function (decorated, $dropdown, $container) {
    // Clone all of the container classes
    $dropdown.attr('class', $container.attr('class'));

    $dropdown[0].classList.remove('select2');
    $dropdown[0].classList.add('select2-container--open');

    $dropdown.css({
      position: 'absolute',
      top: -999999
    });

    this.$container = $container;
  };

  AttachBody.prototype.render = function (decorated) {
    var $container = $('<span></span>');

    var $dropdown = decorated.call(this);
    $container.append($dropdown);

    this.$dropdownContainer = $container;

    return $container;
  };

  AttachBody.prototype._hideDropdown = function (decorated) {
    this.$dropdownContainer.detach();
  };

  AttachBody.prototype._bindContainerResultHandlers =
      function (decorated, container) {

    // These should only be bound once
    if (this._containerResultsHandlersBound) {
      return;
    }

    var self = this;

    container.on('results:all', function () {
      self._positionDropdown();
      self._resizeDropdown();
    });

    container.on('results:append', function () {
      self._positionDropdown();
      self._resizeDropdown();
    });

    container.on('results:message', function () {
      self._positionDropdown();
      self._resizeDropdown();
    });

    container.on('select', function () {
      self._positionDropdown();
      self._resizeDropdown();
    });

    container.on('unselect', function () {
      self._positionDropdown();
      self._resizeDropdown();
    });

    this._containerResultsHandlersBound = true;
  };

  AttachBody.prototype._attachPositioningHandler =
      function (decorated, container) {
    var self = this;

    var scrollEvent = 'scroll.select2.' + container.id;
    var resizeEvent = 'resize.select2.' + container.id;
    var orientationEvent = 'orientationchange.select2.' + container.id;

    var $watchers = this.$container.parents().filter(Utils.hasScroll);
    $watchers.each(function () {
      Utils.StoreData(this, 'select2-scroll-position', {
        x: $(this).scrollLeft(),
        y: $(this).scrollTop()
      });
    });

    $watchers.on(scrollEvent, function (ev) {
      var position = Utils.GetData(this, 'select2-scroll-position');
      $(this).scrollTop(position.y);
    });

    $(window).on(scrollEvent + ' ' + resizeEvent + ' ' + orientationEvent,
      function (e) {
      self._positionDropdown();
      self._resizeDropdown();
    });
  };

  AttachBody.prototype._detachPositioningHandler =
      function (decorated, container) {
    var scrollEvent = 'scroll.select2.' + container.id;
    var resizeEvent = 'resize.select2.' + container.id;
    var orientationEvent = 'orientationchange.select2.' + container.id;

    var $watchers = this.$container.parents().filter(Utils.hasScroll);
    $watchers.off(scrollEvent);

    $(window).off(scrollEvent + ' ' + resizeEvent + ' ' + orientationEvent);
  };

  AttachBody.prototype._positionDropdown = function () {
    var $window = $(window);

    var isCurrentlyAbove = this.$dropdown[0].classList
      .contains('select2-dropdown--above');
    var isCurrentlyBelow = this.$dropdown[0].classList
      .contains('select2-dropdown--below');

    var newDirection = null;

    var offset = this.$container.offset();

    offset.bottom = offset.top + this.$container.outerHeight(false);

    var container = {
      height: this.$container.outerHeight(false)
    };

    container.top = offset.top;
    container.bottom = offset.top + container.height;

    var dropdown = {
      height: this.$dropdown.outerHeight(false)
    };

    var viewport = {
      top: $window.scrollTop(),
      bottom: $window.scrollTop() + $window.height()
    };

    var enoughRoomAbove = viewport.top < (offset.top - dropdown.height);
    var enoughRoomBelow = viewport.bottom > (offset.bottom + dropdown.height);

    var css = {
      left: offset.left,
      top: container.bottom
    };

    // Determine what the parent element is to use for calculating the offset
    var $offsetParent = this.$dropdownParent;

    // For statically positioned elements, we need to get the element
    // that is determining the offset
    if ($offsetParent.css('position') === 'static') {
      $offsetParent = $offsetParent.offsetParent();
    }

    var parentOffset = {
      top: 0,
      left: 0
    };

    if (
      $.contains(document.body, $offsetParent[0]) ||
      $offsetParent[0].isConnected
      ) {
      parentOffset = $offsetParent.offset();
    }

    css.top -= parentOffset.top;
    css.left -= parentOffset.left;

    if (!isCurrentlyAbove && !isCurrentlyBelow) {
      newDirection = 'below';
    }

    if (!enoughRoomBelow && enoughRoomAbove && !isCurrentlyAbove) {
      newDirection = 'above';
    } else if (!enoughRoomAbove && enoughRoomBelow && isCurrentlyAbove) {
      newDirection = 'below';
    }

    if (newDirection == 'above' ||
      (isCurrentlyAbove && newDirection !== 'below')) {
      css.top = container.top - parentOffset.top - dropdown.height;
    }

    if (newDirection != null) {
      this.$dropdown[0].classList.remove('select2-dropdown--below');
      this.$dropdown[0].classList.remove('select2-dropdown--above');
      this.$dropdown[0].classList.add('select2-dropdown--' + newDirection);

      this.$container[0].classList.remove('select2-container--below');
      this.$container[0].classList.remove('select2-container--above');
      this.$container[0].classList.add('select2-container--' + newDirection);
    }

    this.$dropdownContainer.css(css);
  };

  AttachBody.prototype._resizeDropdown = function () {
    var css = {
      width: this.$container.outerWidth(false) + 'px'
    };

    if (this.options.get('dropdownAutoWidth')) {
      css.minWidth = css.width;
      css.position = 'relative';
      css.width = 'auto';
    }

    this.$dropdown.css(css);
  };

  AttachBody.prototype._showDropdown = function (decorated) {
    this.$dropdownContainer.appendTo(this.$dropdownParent);

    this._positionDropdown();
    this._resizeDropdown();
  };

  return AttachBody;
});

S2.define('select2/dropdown/minimumResultsForSearch',[

], function () {
  function countResults (data) {
    var count = 0;

    for (var d = 0; d < data.length; d++) {
      var item = data[d];

      if (item.children) {
        count += countResults(item.children);
      } else {
        count++;
      }
    }

    return count;
  }

  function MinimumResultsForSearch (decorated, $element, options, dataAdapter) {
    this.minimumResultsForSearch = options.get('minimumResultsForSearch');

    if (this.minimumResultsForSearch < 0) {
      this.minimumResultsForSearch = Infinity;
    }

    decorated.call(this, $element, options, dataAdapter);
  }

  MinimumResultsForSearch.prototype.showSearch = function (decorated, params) {
    if (countResults(params.data.results) < this.minimumResultsForSearch) {
      return false;
    }

    return decorated.call(this, params);
  };

  return MinimumResultsForSearch;
});

S2.define('select2/dropdown/selectOnClose',[
  '../utils'
], function (Utils) {
  function SelectOnClose () { }

  SelectOnClose.prototype.bind = function (decorated, container, $container) {
    var self = this;

    decorated.call(this, container, $container);

    container.on('close', function (params) {
      self._handleSelectOnClose(params);
    });
  };

  SelectOnClose.prototype._handleSelectOnClose = function (_, params) {
    if (params && params.originalSelect2Event != null) {
      var event = params.originalSelect2Event;

      // Don't select an item if the close event was triggered from a select or
      // unselect event
      if (event._type === 'select' || event._type === 'unselect') {
        return;
      }
    }

    var $highlightedResults = this.getHighlightedResults();

    // Only select highlighted results
    if ($highlightedResults.length < 1) {
      return;
    }

    var data = Utils.GetData($highlightedResults[0], 'data');

    // Don't re-select already selected resulte
    if (
      (data.element != null && data.element.selected) ||
      (data.element == null && data.selected)
    ) {
      return;
    }

    this.trigger('select', {
        data: data
    });
  };

  return SelectOnClose;
});

S2.define('select2/dropdown/closeOnSelect',[

], function () {
  function CloseOnSelect () { }

  CloseOnSelect.prototype.bind = function (decorated, container, $container) {
    var self = this;

    decorated.call(this, container, $container);

    container.on('select', function (evt) {
      self._selectTriggered(evt);
    });

    container.on('unselect', function (evt) {
      self._selectTriggered(evt);
    });
  };

  CloseOnSelect.prototype._selectTriggered = function (_, evt) {
    var originalEvent = evt.originalEvent;

    // Don't close if the control key is being held
    if (originalEvent && (originalEvent.ctrlKey || originalEvent.metaKey)) {
      return;
    }

    this.trigger('close', {
      originalEvent: originalEvent,
      originalSelect2Event: evt
    });
  };

  return CloseOnSelect;
});

S2.define('select2/dropdown/dropdownCss',[
  '../utils'
], function (Utils) {
  function DropdownCSS () { }

  DropdownCSS.prototype.render = function (decorated) {
    var $dropdown = decorated.call(this);

    var dropdownCssClass = this.options.get('dropdownCssClass') || '';

    if (dropdownCssClass.indexOf(':all:') !== -1) {
      dropdownCssClass = dropdownCssClass.replace(':all:', '');

      Utils.copyNonInternalCssClasses($dropdown[0], this.$element[0]);
    }

    $dropdown.addClass(dropdownCssClass);

    return $dropdown;
  };

  return DropdownCSS;
});

S2.define('select2/i18n/en',[],function () {
  // English
  return {
    errorLoading: function () {
      return 'The results could not be loaded.';
    },
    inputTooLong: function (args) {
      var overChars = args.input.length - args.maximum;

      var message = 'Please delete ' + overChars + ' character';

      if (overChars != 1) {
        message += 's';
      }

      return message;
    },
    inputTooShort: function (args) {
      var remainingChars = args.minimum - args.input.length;

      var message = 'Please enter ' + remainingChars + ' or more characters';

      return message;
    },
    loadingMore: function () {
      return 'Loading more results…';
    },
    maximumSelected: function (args) {
      var message = 'You can only select ' + args.maximum + ' item';

      if (args.maximum != 1) {
        message += 's';
      }

      return message;
    },
    noResults: function () {
      return 'No results found';
    },
    searching: function () {
      return 'Searching…';
    },
    removeAllItems: function () {
      return 'Remove all items';
    },
    removeItem: function () {
      return 'Remove item';
    }
  };
});

S2.define('select2/defaults',[
  'jquery',

  './results',

  './selection/single',
  './selection/multiple',
  './selection/placeholder',
  './selection/allowClear',
  './selection/search',
  './selection/selectionCss',
  './selection/eventRelay',

  './utils',
  './translation',
  './diacritics',

  './data/select',
  './data/array',
  './data/ajax',
  './data/tags',
  './data/tokenizer',
  './data/minimumInputLength',
  './data/maximumInputLength',
  './data/maximumSelectionLength',

  './dropdown',
  './dropdown/search',
  './dropdown/hidePlaceholder',
  './dropdown/infiniteScroll',
  './dropdown/attachBody',
  './dropdown/minimumResultsForSearch',
  './dropdown/selectOnClose',
  './dropdown/closeOnSelect',
  './dropdown/dropdownCss',

  './i18n/en'
], function ($,

             ResultsList,

             SingleSelection, MultipleSelection, Placeholder, AllowClear,
             SelectionSearch, SelectionCSS, EventRelay,

             Utils, Translation, DIACRITICS,

             SelectData, ArrayData, AjaxData, Tags, Tokenizer,
             MinimumInputLength, MaximumInputLength, MaximumSelectionLength,

             Dropdown, DropdownSearch, HidePlaceholder, InfiniteScroll,
             AttachBody, MinimumResultsForSearch, SelectOnClose, CloseOnSelect,
             DropdownCSS,

             EnglishTranslation) {
  function Defaults () {
    this.reset();
  }

  Defaults.prototype.apply = function (options) {
    options = $.extend(true, {}, this.defaults, options);

    if (options.dataAdapter == null) {
      if (options.ajax != null) {
        options.dataAdapter = AjaxData;
      } else if (options.data != null) {
        options.dataAdapter = ArrayData;
      } else {
        options.dataAdapter = SelectData;
      }

      if (options.minimumInputLength > 0) {
        options.dataAdapter = Utils.Decorate(
          options.dataAdapter,
          MinimumInputLength
        );
      }

      if (options.maximumInputLength > 0) {
        options.dataAdapter = Utils.Decorate(
          options.dataAdapter,
          MaximumInputLength
        );
      }

      if (options.maximumSelectionLength > 0) {
        options.dataAdapter = Utils.Decorate(
          options.dataAdapter,
          MaximumSelectionLength
        );
      }

      if (options.tags) {
        options.dataAdapter = Utils.Decorate(options.dataAdapter, Tags);
      }

      if (options.tokenSeparators != null || options.tokenizer != null) {
        options.dataAdapter = Utils.Decorate(
          options.dataAdapter,
          Tokenizer
        );
      }
    }

    if (options.resultsAdapter == null) {
      options.resultsAdapter = ResultsList;

      if (options.ajax != null) {
        options.resultsAdapter = Utils.Decorate(
          options.resultsAdapter,
          InfiniteScroll
        );
      }

      if (options.placeholder != null) {
        options.resultsAdapter = Utils.Decorate(
          options.resultsAdapter,
          HidePlaceholder
        );
      }

      if (options.selectOnClose) {
        options.resultsAdapter = Utils.Decorate(
          options.resultsAdapter,
          SelectOnClose
        );
      }
    }

    if (options.dropdownAdapter == null) {
      if (options.multiple) {
        options.dropdownAdapter = Dropdown;
      } else {
        var SearchableDropdown = Utils.Decorate(Dropdown, DropdownSearch);

        options.dropdownAdapter = SearchableDropdown;
      }

      if (options.minimumResultsForSearch !== 0) {
        options.dropdownAdapter = Utils.Decorate(
          options.dropdownAdapter,
          MinimumResultsForSearch
        );
      }

      if (options.closeOnSelect) {
        options.dropdownAdapter = Utils.Decorate(
          options.dropdownAdapter,
          CloseOnSelect
        );
      }

      if (options.dropdownCssClass != null) {
        options.dropdownAdapter = Utils.Decorate(
          options.dropdownAdapter,
          DropdownCSS
        );
      }

      options.dropdownAdapter = Utils.Decorate(
        options.dropdownAdapter,
        AttachBody
      );
    }

    if (options.selectionAdapter == null) {
      if (options.multiple) {
        options.selectionAdapter = MultipleSelection;
      } else {
        options.selectionAdapter = SingleSelection;
      }

      // Add the placeholder mixin if a placeholder was specified
      if (options.placeholder != null) {
        options.selectionAdapter = Utils.Decorate(
          options.selectionAdapter,
          Placeholder
        );
      }

      if (options.allowClear) {
        options.selectionAdapter = Utils.Decorate(
          options.selectionAdapter,
          AllowClear
        );
      }

      if (options.multiple) {
        options.selectionAdapter = Utils.Decorate(
          options.selectionAdapter,
          SelectionSearch
        );
      }

      if (options.selectionCssClass != null) {
        options.selectionAdapter = Utils.Decorate(
          options.selectionAdapter,
          SelectionCSS
        );
      }

      options.selectionAdapter = Utils.Decorate(
        options.selectionAdapter,
        EventRelay
      );
    }

    // If the defaults were not previously applied from an element, it is
    // possible for the language option to have not been resolved
    options.language = this._resolveLanguage(options.language);

    // Always fall back to English since it will always be complete
    options.language.push('en');

    var uniqueLanguages = [];

    for (var l = 0; l < options.language.length; l++) {
      var language = options.language[l];

      if (uniqueLanguages.indexOf(language) === -1) {
        uniqueLanguages.push(language);
      }
    }

    options.language = uniqueLanguages;

    options.translations = this._processTranslations(
      options.language,
      options.debug
    );

    return options;
  };

  Defaults.prototype.reset = function () {
    function stripDiacritics (text) {
      // Used 'uni range + named function' from http://jsperf.com/diacritics/18
      function match(a) {
        return DIACRITICS[a] || a;
      }

      return text.replace(/[^\u0000-\u007E]/g, match);
    }

    function matcher (params, data) {
      // Always return the object if there is nothing to compare
      if (params.term == null || params.term.trim() === '') {
        return data;
      }

      // Do a recursive check for options with children
      if (data.children && data.children.length > 0) {
        // Clone the data object if there are children
        // This is required as we modify the object to remove any non-matches
        var match = $.extend(true, {}, data);

        // Check each child of the option
        for (var c = data.children.length - 1; c >= 0; c--) {
          var child = data.children[c];

          var matches = matcher(params, child);

          // If there wasn't a match, remove the object in the array
          if (matches == null) {
            match.children.splice(c, 1);
          }
        }

        // If any children matched, return the new object
        if (match.children.length > 0) {
          return match;
        }

        // If there were no matching children, check just the plain object
        return matcher(params, match);
      }

      var original = stripDiacritics(data.text).toUpperCase();
      var term = stripDiacritics(params.term).toUpperCase();

      // Check if the text contains the term
      if (original.indexOf(term) > -1) {
        return data;
      }

      // If it doesn't contain the term, don't return anything
      return null;
    }

    this.defaults = {
      amdLanguageBase: './i18n/',
      autocomplete: 'off',
      closeOnSelect: true,
      debug: false,
      dropdownAutoWidth: false,
      escapeMarkup: Utils.escapeMarkup,
      language: {},
      matcher: matcher,
      minimumInputLength: 0,
      maximumInputLength: 0,
      maximumSelectionLength: 0,
      minimumResultsForSearch: 0,
      selectOnClose: false,
      scrollAfterSelect: false,
      sorter: function (data) {
        return data;
      },
      templateResult: function (result) {
        return result.text;
      },
      templateSelection: function (selection) {
        return selection.text;
      },
      theme: 'default',
      width: 'resolve'
    };
  };

  Defaults.prototype.applyFromElement = function (options, $element) {
    var optionLanguage = options.language;
    var defaultLanguage = this.defaults.language;
    var elementLanguage = $element.prop('lang');
    var parentLanguage = $element.closest('[lang]').prop('lang');

    var languages = Array.prototype.concat.call(
      this._resolveLanguage(elementLanguage),
      this._resolveLanguage(optionLanguage),
      this._resolveLanguage(defaultLanguage),
      this._resolveLanguage(parentLanguage)
    );

    options.language = languages;

    return options;
  };

  Defaults.prototype._resolveLanguage = function (language) {
    if (!language) {
      return [];
    }

    if ($.isEmptyObject(language)) {
      return [];
    }

    if ($.isPlainObject(language)) {
      return [language];
    }

    var languages;

    if (!Array.isArray(language)) {
      languages = [language];
    } else {
      languages = language;
    }

    var resolvedLanguages = [];

    for (var l = 0; l < languages.length; l++) {
      resolvedLanguages.push(languages[l]);

      if (typeof languages[l] === 'string' && languages[l].indexOf('-') > 0) {
        // Extract the region information if it is included
        var languageParts = languages[l].split('-');
        var baseLanguage = languageParts[0];

        resolvedLanguages.push(baseLanguage);
      }
    }

    return resolvedLanguages;
  };

  Defaults.prototype._processTranslations = function (languages, debug) {
    var translations = new Translation();

    for (var l = 0; l < languages.length; l++) {
      var languageData = new Translation();

      var language = languages[l];

      if (typeof language === 'string') {
        try {
          // Try to load it with the original name
          languageData = Translation.loadPath(language);
        } catch (e) {
          try {
            // If we couldn't load it, check if it wasn't the full path
            language = this.defaults.amdLanguageBase + language;
            languageData = Translation.loadPath(language);
          } catch (ex) {
            // The translation could not be loaded at all. Sometimes this is
            // because of a configuration problem, other times this can be
            // because of how Select2 helps load all possible translation files
            if (debug && window.console && console.warn) {
              console.warn(
                'Select2: The language file for "' + language + '" could ' +
                'not be automatically loaded. A fallback will be used instead.'
              );
            }
          }
        }
      } else if ($.isPlainObject(language)) {
        languageData = new Translation(language);
      } else {
        languageData = language;
      }

      translations.extend(languageData);
    }

    return translations;
  };

  Defaults.prototype.set = function (key, value) {
    var camelKey = $.camelCase(key);

    var data = {};
    data[camelKey] = value;

    var convertedData = Utils._convertData(data);

    $.extend(true, this.defaults, convertedData);
  };

  var defaults = new Defaults();

  return defaults;
});

S2.define('select2/options',[
  'jquery',
  './defaults',
  './utils'
], function ($, Defaults, Utils) {
  function Options (options, $element) {
    this.options = options;

    if ($element != null) {
      this.fromElement($element);
    }

    if ($element != null) {
      this.options = Defaults.applyFromElement(this.options, $element);
    }

    this.options = Defaults.apply(this.options);
  }

  Options.prototype.fromElement = function ($e) {
    var excludedData = ['select2'];

    if (this.options.multiple == null) {
      this.options.multiple = $e.prop('multiple');
    }

    if (this.options.disabled == null) {
      this.options.disabled = $e.prop('disabled');
    }

    if (this.options.autocomplete == null && $e.prop('autocomplete')) {
      this.options.autocomplete = $e.prop('autocomplete');
    }

    if (this.options.dir == null) {
      if ($e.prop('dir')) {
        this.options.dir = $e.prop('dir');
      } else if ($e.closest('[dir]').prop('dir')) {
        this.options.dir = $e.closest('[dir]').prop('dir');
      } else {
        this.options.dir = 'ltr';
      }
    }

    $e.prop('disabled', this.options.disabled);
    $e.prop('multiple', this.options.multiple);

    if (Utils.GetData($e[0], 'select2Tags')) {
      if (this.options.debug && window.console && console.warn) {
        console.warn(
          'Select2: The `data-select2-tags` attribute has been changed to ' +
          'use the `data-data` and `data-tags="true"` attributes and will be ' +
          'removed in future versions of Select2.'
        );
      }

      Utils.StoreData($e[0], 'data', Utils.GetData($e[0], 'select2Tags'));
      Utils.StoreData($e[0], 'tags', true);
    }

    if (Utils.GetData($e[0], 'ajaxUrl')) {
      if (this.options.debug && window.console && console.warn) {
        console.warn(
          'Select2: The `data-ajax-url` attribute has been changed to ' +
          '`data-ajax--url` and support for the old attribute will be removed' +
          ' in future versions of Select2.'
        );
      }

      $e.attr('ajax--url', Utils.GetData($e[0], 'ajaxUrl'));
      Utils.StoreData($e[0], 'ajax-Url', Utils.GetData($e[0], 'ajaxUrl'));
    }

    var dataset = {};

    function upperCaseLetter(_, letter) {
      return letter.toUpperCase();
    }

    // Pre-load all of the attributes which are prefixed with `data-`
    for (var attr = 0; attr < $e[0].attributes.length; attr++) {
      var attributeName = $e[0].attributes[attr].name;
      var prefix = 'data-';

      if (attributeName.substr(0, prefix.length) == prefix) {
        // Get the contents of the attribute after `data-`
        var dataName = attributeName.substring(prefix.length);

        // Get the data contents from the consistent source
        // This is more than likely the jQuery data helper
        var dataValue = Utils.GetData($e[0], dataName);

        // camelCase the attribute name to match the spec
        var camelDataName = dataName.replace(/-([a-z])/g, upperCaseLetter);

        // Store the data attribute contents into the dataset since
        dataset[camelDataName] = dataValue;
      }
    }

    // Prefer the element's `dataset` attribute if it exists
    // jQuery 1.x does not correctly handle data attributes with multiple dashes
    if ($.fn.jquery && $.fn.jquery.substr(0, 2) == '1.' && $e[0].dataset) {
      dataset = $.extend(true, {}, $e[0].dataset, dataset);
    }

    // Prefer our internal data cache if it exists
    var data = $.extend(true, {}, Utils.GetData($e[0]), dataset);

    data = Utils._convertData(data);

    for (var key in data) {
      if (excludedData.indexOf(key) > -1) {
        continue;
      }

      if ($.isPlainObject(this.options[key])) {
        $.extend(this.options[key], data[key]);
      } else {
        this.options[key] = data[key];
      }
    }

    return this;
  };

  Options.prototype.get = function (key) {
    return this.options[key];
  };

  Options.prototype.set = function (key, val) {
    this.options[key] = val;
  };

  return Options;
});

S2.define('select2/core',[
  'jquery',
  './options',
  './utils',
  './keys'
], function ($, Options, Utils, KEYS) {
  var Select2 = function ($element, options) {
    if (Utils.GetData($element[0], 'select2') != null) {
      Utils.GetData($element[0], 'select2').destroy();
    }

    this.$element = $element;

    this.id = this._generateId($element);

    options = options || {};

    this.options = new Options(options, $element);

    Select2.__super__.constructor.call(this);

    // Set up the tabindex

    var tabindex = $element.attr('tabindex') || 0;
    Utils.StoreData($element[0], 'old-tabindex', tabindex);
    $element.attr('tabindex', '-1');

    // Set up containers and adapters

    var DataAdapter = this.options.get('dataAdapter');
    this.dataAdapter = new DataAdapter($element, this.options);

    var $container = this.render();

    this._placeContainer($container);

    var SelectionAdapter = this.options.get('selectionAdapter');
    this.selection = new SelectionAdapter($element, this.options);
    this.$selection = this.selection.render();

    this.selection.position(this.$selection, $container);

    var DropdownAdapter = this.options.get('dropdownAdapter');
    this.dropdown = new DropdownAdapter($element, this.options);
    this.$dropdown = this.dropdown.render();

    this.dropdown.position(this.$dropdown, $container);

    var ResultsAdapter = this.options.get('resultsAdapter');
    this.results = new ResultsAdapter($element, this.options, this.dataAdapter);
    this.$results = this.results.render();

    this.results.position(this.$results, this.$dropdown);

    // Bind events

    var self = this;

    // Bind the container to all of the adapters
    this._bindAdapters();

    // Register any DOM event handlers
    this._registerDomEvents();

    // Register any internal event handlers
    this._registerDataEvents();
    this._registerSelectionEvents();
    this._registerDropdownEvents();
    this._registerResultsEvents();
    this._registerEvents();

    // Set the initial state
    this.dataAdapter.current(function (initialData) {
      self.trigger('selection:update', {
        data: initialData
      });
    });

    // Hide the original select
    $element[0].classList.add('select2-hidden-accessible');
    $element.attr('aria-hidden', 'true');

    // Synchronize any monitored attributes
    this._syncAttributes();

    Utils.StoreData($element[0], 'select2', this);

    // Ensure backwards compatibility with $element.data('select2').
    $element.data('select2', this);
  };

  Utils.Extend(Select2, Utils.Observable);

  Select2.prototype._generateId = function ($element) {
    var id = '';

    if ($element.attr('id') != null) {
      id = $element.attr('id');
    } else if ($element.attr('name') != null) {
      id = $element.attr('name') + '-' + Utils.generateChars(2);
    } else {
      id = Utils.generateChars(4);
    }

    id = id.replace(/(:|\.|\[|\]|,)/g, '');
    id = 'select2-' + id;

    return id;
  };

  Select2.prototype._placeContainer = function ($container) {
    $container.insertAfter(this.$element);

    var width = this._resolveWidth(this.$element, this.options.get('width'));

    if (width != null) {
      $container.css('width', width);
    }
  };

  Select2.prototype._resolveWidth = function ($element, method) {
    var WIDTH = /^width:(([-+]?([0-9]*\.)?[0-9]+)(px|em|ex|%|in|cm|mm|pt|pc))/i;

    if (method == 'resolve') {
      var styleWidth = this._resolveWidth($element, 'style');

      if (styleWidth != null) {
        return styleWidth;
      }

      return this._resolveWidth($element, 'element');
    }

    if (method == 'element') {
      var elementWidth = $element.outerWidth(false);

      if (elementWidth <= 0) {
        return 'auto';
      }

      return elementWidth + 'px';
    }

    if (method == 'style') {
      var style = $element.attr('style');

      if (typeof(style) !== 'string') {
        return null;
      }

      var attrs = style.split(';');

      for (var i = 0, l = attrs.length; i < l; i = i + 1) {
        var attr = attrs[i].replace(/\s/g, '');
        var matches = attr.match(WIDTH);

        if (matches !== null && matches.length >= 1) {
          return matches[1];
        }
      }

      return null;
    }

    if (method == 'computedstyle') {
      var computedStyle = window.getComputedStyle($element[0]);

      return computedStyle.width;
    }

    return method;
  };

  Select2.prototype._bindAdapters = function () {
    this.dataAdapter.bind(this, this.$container);
    this.selection.bind(this, this.$container);

    this.dropdown.bind(this, this.$container);
    this.results.bind(this, this.$container);
  };

  Select2.prototype._registerDomEvents = function () {
    var self = this;

    this.$element.on('change.select2', function () {
      self.dataAdapter.current(function (data) {
        self.trigger('selection:update', {
          data: data
        });
      });
    });

    this.$element.on('focus.select2', function (evt) {
      self.trigger('focus', evt);
    });

    this._syncA = Utils.bind(this._syncAttributes, this);
    this._syncS = Utils.bind(this._syncSubtree, this);

    this._observer = new window.MutationObserver(function (mutations) {
      self._syncA();
      self._syncS(mutations);
    });
    this._observer.observe(this.$element[0], {
      attributes: true,
      childList: true,
      subtree: false
    });
  };

  Select2.prototype._registerDataEvents = function () {
    var self = this;

    this.dataAdapter.on('*', function (name, params) {
      self.trigger(name, params);
    });
  };

  Select2.prototype._registerSelectionEvents = function () {
    var self = this;
    var nonRelayEvents = ['toggle', 'focus'];

    this.selection.on('toggle', function () {
      self.toggleDropdown();
    });

    this.selection.on('focus', function (params) {
      self.focus(params);
    });

    this.selection.on('*', function (name, params) {
      if (nonRelayEvents.indexOf(name) !== -1) {
        return;
      }

      self.trigger(name, params);
    });
  };

  Select2.prototype._registerDropdownEvents = function () {
    var self = this;

    this.dropdown.on('*', function (name, params) {
      self.trigger(name, params);
    });
  };

  Select2.prototype._registerResultsEvents = function () {
    var self = this;

    this.results.on('*', function (name, params) {
      self.trigger(name, params);
    });
  };

  Select2.prototype._registerEvents = function () {
    var self = this;

    this.on('open', function () {
      self.$container[0].classList.add('select2-container--open');
    });

    this.on('close', function () {
      self.$container[0].classList.remove('select2-container--open');
    });

    this.on('enable', function () {
      self.$container[0].classList.remove('select2-container--disabled');
    });

    this.on('disable', function () {
      self.$container[0].classList.add('select2-container--disabled');
    });

    this.on('blur', function () {
      self.$container[0].classList.remove('select2-container--focus');
    });

    this.on('query', function (params) {
      if (!self.isOpen()) {
        self.trigger('open', {});
      }

      this.dataAdapter.query(params, function (data) {
        self.trigger('results:all', {
          data: data,
          query: params
        });
      });
    });

    this.on('query:append', function (params) {
      this.dataAdapter.query(params, function (data) {
        self.trigger('results:append', {
          data: data,
          query: params
        });
      });
    });

    this.on('keypress', function (evt) {
      var key = evt.which;

      if (self.isOpen()) {
        if (key === KEYS.ESC || key === KEYS.TAB ||
            (key === KEYS.UP && evt.altKey)) {
          self.close(evt);

          evt.preventDefault();
        } else if (key === KEYS.ENTER) {
          self.trigger('results:select', {});

          evt.preventDefault();
        } else if ((key === KEYS.SPACE && evt.ctrlKey)) {
          self.trigger('results:toggle', {});

          evt.preventDefault();
        } else if (key === KEYS.UP) {
          self.trigger('results:previous', {});

          evt.preventDefault();
        } else if (key === KEYS.DOWN) {
          self.trigger('results:next', {});

          evt.preventDefault();
        }
      } else {
        if (key === KEYS.ENTER || key === KEYS.SPACE ||
            (key === KEYS.DOWN && evt.altKey)) {
          self.open();

          evt.preventDefault();
        }
      }
    });
  };

  Select2.prototype._syncAttributes = function () {
    this.options.set('disabled', this.$element.prop('disabled'));

    if (this.isDisabled()) {
      if (this.isOpen()) {
        this.close();
      }

      this.trigger('disable', {});
    } else {
      this.trigger('enable', {});
    }
  };

  Select2.prototype._isChangeMutation = function (mutations) {
    var self = this;

    if (mutations.addedNodes && mutations.addedNodes.length > 0) {
      for (var n = 0; n < mutations.addedNodes.length; n++) {
        var node = mutations.addedNodes[n];

        if (node.selected) {
          return true;
        }
      }
    } else if (mutations.removedNodes && mutations.removedNodes.length > 0) {
      return true;
    } else if (Array.isArray(mutations)) {
      return mutations.some(function (mutation) {
        return self._isChangeMutation(mutation);
      });
    }

    return false;
  };

  Select2.prototype._syncSubtree = function (mutations) {
    var changed = this._isChangeMutation(mutations);
    var self = this;

    // Only re-pull the data if we think there is a change
    if (changed) {
      this.dataAdapter.current(function (currentData) {
        self.trigger('selection:update', {
          data: currentData
        });
      });
    }
  };

  /**
   * Override the trigger method to automatically trigger pre-events when
   * there are events that can be prevented.
   */
  Select2.prototype.trigger = function (name, args) {
    var actualTrigger = Select2.__super__.trigger;
    var preTriggerMap = {
      'open': 'opening',
      'close': 'closing',
      'select': 'selecting',
      'unselect': 'unselecting',
      'clear': 'clearing'
    };

    if (args === undefined) {
      args = {};
    }

    if (name in preTriggerMap) {
      var preTriggerName = preTriggerMap[name];
      var preTriggerArgs = {
        prevented: false,
        name: name,
        args: args
      };

      actualTrigger.call(this, preTriggerName, preTriggerArgs);

      if (preTriggerArgs.prevented) {
        args.prevented = true;

        return;
      }
    }

    actualTrigger.call(this, name, args);
  };

  Select2.prototype.toggleDropdown = function () {
    if (this.isDisabled()) {
      return;
    }

    if (this.isOpen()) {
      this.close();
    } else {
      this.open();
    }
  };

  Select2.prototype.open = function () {
    if (this.isOpen()) {
      return;
    }

    if (this.isDisabled()) {
      return;
    }

    this.trigger('query', {});
  };

  Select2.prototype.close = function (evt) {
    if (!this.isOpen()) {
      return;
    }

    this.trigger('close', { originalEvent : evt });
  };

  /**
   * Helper method to abstract the "enabled" (not "disabled") state of this
   * object.
   *
   * @return {true} if the instance is not disabled.
   * @return {false} if the instance is disabled.
   */
  Select2.prototype.isEnabled = function () {
    return !this.isDisabled();
  };

  /**
   * Helper method to abstract the "disabled" state of this object.
   *
   * @return {true} if the disabled option is true.
   * @return {false} if the disabled option is false.
   */
  Select2.prototype.isDisabled = function () {
    return this.options.get('disabled');
  };

  Select2.prototype.isOpen = function () {
    return this.$container[0].classList.contains('select2-container--open');
  };

  Select2.prototype.hasFocus = function () {
    return this.$container[0].classList.contains('select2-container--focus');
  };

  Select2.prototype.focus = function (data) {
    // No need to re-trigger focus events if we are already focused
    if (this.hasFocus()) {
      return;
    }

    this.$container[0].classList.add('select2-container--focus');
    this.trigger('focus', {});
  };

  Select2.prototype.enable = function (args) {
    if (this.options.get('debug') && window.console && console.warn) {
      console.warn(
        'Select2: The `select2("enable")` method has been deprecated and will' +
        ' be removed in later Select2 versions. Use $element.prop("disabled")' +
        ' instead.'
      );
    }

    if (args == null || args.length === 0) {
      args = [true];
    }

    var disabled = !args[0];

    this.$element.prop('disabled', disabled);
  };

  Select2.prototype.data = function () {
    if (this.options.get('debug') &&
        arguments.length > 0 && window.console && console.warn) {
      console.warn(
        'Select2: Data can no longer be set using `select2("data")`. You ' +
        'should consider setting the value instead using `$element.val()`.'
      );
    }

    var data = [];

    this.dataAdapter.current(function (currentData) {
      data = currentData;
    });

    return data;
  };

  Select2.prototype.val = function (args) {
    if (this.options.get('debug') && window.console && console.warn) {
      console.warn(
        'Select2: The `select2("val")` method has been deprecated and will be' +
        ' removed in later Select2 versions. Use $element.val() instead.'
      );
    }

    if (args == null || args.length === 0) {
      return this.$element.val();
    }

    var newVal = args[0];

    if (Array.isArray(newVal)) {
      newVal = newVal.map(function (obj) {
        return obj.toString();
      });
    }

    this.$element.val(newVal).trigger('input').trigger('change');
  };

  Select2.prototype.destroy = function () {
    this.$container.remove();

    this._observer.disconnect();
    this._observer = null;

    this._syncA = null;
    this._syncS = null;

    this.$element.off('.select2');
    this.$element.attr('tabindex',
    Utils.GetData(this.$element[0], 'old-tabindex'));

    this.$element[0].classList.remove('select2-hidden-accessible');
    this.$element.attr('aria-hidden', 'false');
    Utils.RemoveData(this.$element[0]);
    this.$element.removeData('select2');

    this.dataAdapter.destroy();
    this.selection.destroy();
    this.dropdown.destroy();
    this.results.destroy();

    this.dataAdapter = null;
    this.selection = null;
    this.dropdown = null;
    this.results = null;
  };

  Select2.prototype.render = function () {
    var $container = $(
      '<span class="select2 select2-container">' +
        '<span class="selection"></span>' +
        '<span class="dropdown-wrapper" aria-hidden="true"></span>' +
      '</span>'
    );

    $container.attr('dir', this.options.get('dir'));

    this.$container = $container;

    this.$container[0].classList
      .add('select2-container--' + this.options.get('theme'));

    Utils.StoreData($container[0], 'element', this.$element);

    return $container;
  };

  return Select2;
});

S2.define('select2/dropdown/attachContainer',[

], function () {
  function AttachContainer (decorated, $element, options) {
    decorated.call(this, $element, options);
  }

  AttachContainer.prototype.position =
    function (decorated, $dropdown, $container) {
    var $dropdownContainer = $container.find('.dropdown-wrapper');
    $dropdownContainer.append($dropdown);

    $dropdown[0].classList.add('select2-dropdown--below');
    $container[0].classList.add('select2-container--below');
  };

  return AttachContainer;
});

S2.define('select2/dropdown/stopPropagation',[

], function () {
  function StopPropagation () { }

  StopPropagation.prototype.bind = function (decorated, container, $container) {
    decorated.call(this, container, $container);

    var stoppedEvents = [
    'blur',
    'change',
    'click',
    'dblclick',
    'focus',
    'focusin',
    'focusout',
    'input',
    'keydown',
    'keyup',
    'keypress',
    'mousedown',
    'mouseenter',
    'mouseleave',
    'mousemove',
    'mouseover',
    'mouseup',
    'search',
    'touchend',
    'touchstart'
    ];

    this.$dropdown.on(stoppedEvents.join(' '), function (evt) {
      evt.stopPropagation();
    });
  };

  return StopPropagation;
});

S2.define('select2/selection/stopPropagation',[

], function () {
  function StopPropagation () { }

  StopPropagation.prototype.bind = function (decorated, container, $container) {
    decorated.call(this, container, $container);

    var stoppedEvents = [
      'blur',
      'change',
      'click',
      'dblclick',
      'focus',
      'focusin',
      'focusout',
      'input',
      'keydown',
      'keyup',
      'keypress',
      'mousedown',
      'mouseenter',
      'mouseleave',
      'mousemove',
      'mouseover',
      'mouseup',
      'search',
      'touchend',
      'touchstart'
    ];

    this.$selection.on(stoppedEvents.join(' '), function (evt) {
      evt.stopPropagation();
    });
  };

  return StopPropagation;
});

/*!
 * jQuery Mousewheel 3.1.13
 *
 * Copyright jQuery Foundation and other contributors
 * Released under the MIT license
 * http://jquery.org/license
 */

(function (factory) {
    if ( typeof S2.define === 'function' && S2.define.amd ) {
        // AMD. Register as an anonymous module.
        S2.define('jquery-mousewheel',['jquery'], factory);
    } else if (typeof exports === 'object') {
        // Node/CommonJS style for Browserify
        module.exports = factory;
    } else {
        // Browser globals
        factory(jQuery);
    }
}(function ($) {

    var toFix  = ['wheel', 'mousewheel', 'DOMMouseScroll', 'MozMousePixelScroll'],
        toBind = ( 'onwheel' in document || document.documentMode >= 9 ) ?
                    ['wheel'] : ['mousewheel', 'DomMouseScroll', 'MozMousePixelScroll'],
        slice  = Array.prototype.slice,
        nullLowestDeltaTimeout, lowestDelta;

    if ( $.event.fixHooks ) {
        for ( var i = toFix.length; i; ) {
            $.event.fixHooks[ toFix[--i] ] = $.event.mouseHooks;
        }
    }

    var special = $.event.special.mousewheel = {
        version: '3.1.12',

        setup: function() {
            if ( this.addEventListener ) {
                for ( var i = toBind.length; i; ) {
                    this.addEventListener( toBind[--i], handler, false );
                }
            } else {
                this.onmousewheel = handler;
            }
            // Store the line height and page height for this particular element
            $.data(this, 'mousewheel-line-height', special.getLineHeight(this));
            $.data(this, 'mousewheel-page-height', special.getPageHeight(this));
        },

        teardown: function() {
            if ( this.removeEventListener ) {
                for ( var i = toBind.length; i; ) {
                    this.removeEventListener( toBind[--i], handler, false );
                }
            } else {
                this.onmousewheel = null;
            }
            // Clean up the data we added to the element
            $.removeData(this, 'mousewheel-line-height');
            $.removeData(this, 'mousewheel-page-height');
        },

        getLineHeight: function(elem) {
            var $elem = $(elem),
                $parent = $elem['offsetParent' in $.fn ? 'offsetParent' : 'parent']();
            if (!$parent.length) {
                $parent = $('body');
            }
            return parseInt($parent.css('fontSize'), 10) || parseInt($elem.css('fontSize'), 10) || 16;
        },

        getPageHeight: function(elem) {
            return $(elem).height();
        },

        settings: {
            adjustOldDeltas: true, // see shouldAdjustOldDeltas() below
            normalizeOffset: true  // calls getBoundingClientRect for each event
        }
    };

    $.fn.extend({
        mousewheel: function(fn) {
            return fn ? this.bind('mousewheel', fn) : this.trigger('mousewheel');
        },

        unmousewheel: function(fn) {
            return this.unbind('mousewheel', fn);
        }
    });


    function handler(event) {
        var orgEvent   = event || window.event,
            args       = slice.call(arguments, 1),
            delta      = 0,
            deltaX     = 0,
            deltaY     = 0,
            absDelta   = 0,
            offsetX    = 0,
            offsetY    = 0;
        event = $.event.fix(orgEvent);
        event.type = 'mousewheel';

        // Old school scrollwheel delta
        if ( 'detail'      in orgEvent ) { deltaY = orgEvent.detail * -1;      }
        if ( 'wheelDelta'  in orgEvent ) { deltaY = orgEvent.wheelDelta;       }
        if ( 'wheelDeltaY' in orgEvent ) { deltaY = orgEvent.wheelDeltaY;      }
        if ( 'wheelDeltaX' in orgEvent ) { deltaX = orgEvent.wheelDeltaX * -1; }

        // Firefox < 17 horizontal scrolling related to DOMMouseScroll event
        if ( 'axis' in orgEvent && orgEvent.axis === orgEvent.HORIZONTAL_AXIS ) {
            deltaX = deltaY * -1;
            deltaY = 0;
        }

        // Set delta to be deltaY or deltaX if deltaY is 0 for backwards compatabilitiy
        delta = deltaY === 0 ? deltaX : deltaY;

        // New school wheel delta (wheel event)
        if ( 'deltaY' in orgEvent ) {
            deltaY = orgEvent.deltaY * -1;
            delta  = deltaY;
        }
        if ( 'deltaX' in orgEvent ) {
            deltaX = orgEvent.deltaX;
            if ( deltaY === 0 ) { delta  = deltaX * -1; }
        }

        // No change actually happened, no reason to go any further
        if ( deltaY === 0 && deltaX === 0 ) { return; }

        // Need to convert lines and pages to pixels if we aren't already in pixels
        // There are three delta modes:
        //   * deltaMode 0 is by pixels, nothing to do
        //   * deltaMode 1 is by lines
        //   * deltaMode 2 is by pages
        if ( orgEvent.deltaMode === 1 ) {
            var lineHeight = $.data(this, 'mousewheel-line-height');
            delta  *= lineHeight;
            deltaY *= lineHeight;
            deltaX *= lineHeight;
        } else if ( orgEvent.deltaMode === 2 ) {
            var pageHeight = $.data(this, 'mousewheel-page-height');
            delta  *= pageHeight;
            deltaY *= pageHeight;
            deltaX *= pageHeight;
        }

        // Store lowest absolute delta to normalize the delta values
        absDelta = Math.max( Math.abs(deltaY), Math.abs(deltaX) );

        if ( !lowestDelta || absDelta < lowestDelta ) {
            lowestDelta = absDelta;

            // Adjust older deltas if necessary
            if ( shouldAdjustOldDeltas(orgEvent, absDelta) ) {
                lowestDelta /= 40;
            }
        }

        // Adjust older deltas if necessary
        if ( shouldAdjustOldDeltas(orgEvent, absDelta) ) {
            // Divide all the things by 40!
            delta  /= 40;
            deltaX /= 40;
            deltaY /= 40;
        }

        // Get a whole, normalized value for the deltas
        delta  = Math[ delta  >= 1 ? 'floor' : 'ceil' ](delta  / lowestDelta);
        deltaX = Math[ deltaX >= 1 ? 'floor' : 'ceil' ](deltaX / lowestDelta);
        deltaY = Math[ deltaY >= 1 ? 'floor' : 'ceil' ](deltaY / lowestDelta);

        // Normalise offsetX and offsetY properties
        if ( special.settings.normalizeOffset && this.getBoundingClientRect ) {
            var boundingRect = this.getBoundingClientRect();
            offsetX = event.clientX - boundingRect.left;
            offsetY = event.clientY - boundingRect.top;
        }

        // Add information to the event object
        event.deltaX = deltaX;
        event.deltaY = deltaY;
        event.deltaFactor = lowestDelta;
        event.offsetX = offsetX;
        event.offsetY = offsetY;
        // Go ahead and set deltaMode to 0 since we converted to pixels
        // Although this is a little odd since we overwrite the deltaX/Y
        // properties with normalized deltas.
        event.deltaMode = 0;

        // Add event and delta to the front of the arguments
        args.unshift(event, delta, deltaX, deltaY);

        // Clearout lowestDelta after sometime to better
        // handle multiple device types that give different
        // a different lowestDelta
        // Ex: trackpad = 3 and mouse wheel = 120
        if (nullLowestDeltaTimeout) { clearTimeout(nullLowestDeltaTimeout); }
        nullLowestDeltaTimeout = setTimeout(nullLowestDelta, 200);

        return ($.event.dispatch || $.event.handle).apply(this, args);
    }

    function nullLowestDelta() {
        lowestDelta = null;
    }

    function shouldAdjustOldDeltas(orgEvent, absDelta) {
        // If this is an older event and the delta is divisable by 120,
        // then we are assuming that the browser is treating this as an
        // older mouse wheel event and that we should divide the deltas
        // by 40 to try and get a more usable deltaFactor.
        // Side note, this actually impacts the reported scroll distance
        // in older browsers and can cause scrolling to be slower than native.
        // Turn this off by setting $.event.special.mousewheel.settings.adjustOldDeltas to false.
        return special.settings.adjustOldDeltas && orgEvent.type === 'mousewheel' && absDelta % 120 === 0;
    }

}));

S2.define('jquery.select2',[
  'jquery',
  'jquery-mousewheel',

  './select2/core',
  './select2/defaults',
  './select2/utils'
], function ($, _, Select2, Defaults, Utils) {
  if ($.fn.select2 == null) {
    // All methods that should return the element
    var thisMethods = ['open', 'close', 'destroy'];

    $.fn.select2 = function (options) {
      options = options || {};

      if (typeof options === 'object') {
        this.each(function () {
          var instanceOptions = $.extend(true, {}, options);

          var instance = new Select2($(this), instanceOptions);
        });

        return this;
      } else if (typeof options === 'string') {
        var ret;
        var args = Array.prototype.slice.call(arguments, 1);

        this.each(function () {
          var instance = Utils.GetData(this, 'select2');

          if (instance == null && window.console && console.error) {
            console.error(
              'The select2(\'' + options + '\') method was called on an ' +
              'element that is not using Select2.'
            );
          }

          ret = instance[options].apply(instance, args);
        });

        // Check if we should be returning `this`
        if (thisMethods.indexOf(options) > -1) {
          return this;
        }

        return ret;
      } else {
        throw new Error('Invalid arguments for Select2: ' + options);
      }
    };
  }

  if ($.fn.select2.defaults == null) {
    $.fn.select2.defaults = Defaults;
  }

  return Select2;
});

  // Return the AMD loader configuration so it can be used outside of this file
  return {
    define: S2.define,
    require: S2.require
  };
}());

  // Autoload the jQuery bindings
  // We know that all of the modules exist above this, so we're safe
  var select2 = S2.require('jquery.select2');

  // Hold the AMD module references on the jQuery function that was just loaded
  // This allows Select2 to use the internal loader outside of this file, such
  // as in the language files.
  jQuery.fn.select2.amd = S2;

  // Return the Select2 instance for anyone who is importing it.
  return select2;
}));

'use strict';

var _slicedToArray = function () { function sliceIterator(arr, i) { var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"]) _i["return"](); } finally { if (_d) throw _e; } } return _arr; } return function (arr, i) { if (Array.isArray(arr)) { return arr; } else if (Symbol.iterator in Object(arr)) { return sliceIterator(arr, i); } else { throw new TypeError("Invalid attempt to destructure non-iterable instance"); } }; }();

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };

var _get = function get(object, property, receiver) { if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { return get(parent, property, receiver); } } else if ("value" in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var _endpoint = window._endpoint || 'https://knime.bfr.berlin/backend/'; //http://localhost:8080/' //'https://knime.bfr.berlin/landingpage/';
window._endpoints = {
	metadata: _endpoint + 'metadata/',
	image: _endpoint + 'image/',
	download: _endpoint + 'download/',
	uploadDate: _endpoint + 'uploadDate/',
	executionTime: _endpoint + 'executionTime/',
	simulations: _endpoint + 'simulations/',
	execution: _endpoint + 'execute/',
	search: _endpoint + 'search/',
	filter: _endpoint + 'filter'
};
window._debug = true;
/*

version: 1.0.0
author: sascha obermüller
date: 08.12.2020

*/

var ModelHandler = function () {
	function ModelHandler(metadata, img) {
		_classCallCheck(this, ModelHandler);

		this._metadata = metadata;
		this._schema = {};
		this._menu = [];
		this._img = img;
		this.panels = {};
	}

	_createClass(ModelHandler, [{
		key: '_create',
		value: function _create() {
			console.log('here to be', this._metadata.generalInformation);
			this._panels = {
				generalInformation: {
					type: 'simple',
					schema: this._schema.generalInformation,
					metadata: this._metadata.generalInformation
				},
				modelCategory: {
					type: 'simple',
					schema: this._schema.modelCategory,
					metadata: this._metadata.generalInformation.modelCategory
				},
				author: {
					type: 'complex',
					schema: this._schema.contact,
					metadata: this._metadata.generalInformation.author
				},
				creator: {
					type: 'complex',
					schema: this._schema.contact,
					metadata: this._metadata.generalInformation.creator
				},
				reference: {
					type: 'complex',
					schema: this._schema.reference,
					metadata: this._metadata.generalInformation.reference
				},
				scopeGeneral: {
					type: 'simple',
					schema: this._schema.scope,
					metadata: this._metadata.scope
				},
				product: {
					type: 'complex',
					schema: this._schema.product,
					metadata: this._metadata.scope.product
				},
				hazard: {
					type: 'complex',
					schema: this._schema.hazard,
					metadata: this._metadata.scope.hazard
				},
				population: {
					type: 'complex',
					schema: this._schema.populationGroup,
					metadata: this._metadata.scope.populationGroup
				},
				study: {
					type: 'simple',
					schema: this._schema.study,
					metadata: this._metadata.dataBackground.study
				},
				studySample: {
					type: 'complex',
					schema: this._schema.studySample,
					metadata: this._metadata.dataBackground.studySample
				},
				dietaryAssessmentMethod: {
					type: 'complex',
					schema: this._schema.dietaryAssessmentMethod,
					metadata: this._metadata.dataBackground.dietaryAssessmentMethod
				},
				laboratory: {
					type: 'complex',
					schema: this._schema.laboratory,
					metadata: this._metadata.dataBackground.laboratory
				},
				assay: {
					type: 'complex',
					schema: this._schema.assay,
					metadata: this._metadata.dataBackground.assay
				},
				modelMath: {
					type: 'simple',
					schema: this._schema.modelMath,
					metadata: this._metadata.modelMath
				},
				parameter: {
					type: 'complex',
					schema: this._schema.parameter,
					metadata: this._metadata.modelMath.parameter
				},
				qualityMeasures: {
					type: 'complex',
					schema: this._schema.qualityMeasures,
					metadata: this._metadata.modelMath.qualityMeasures
				},
				modelEquation: {
					type: 'complex',
					schema: this._schema.modelEquation,
					metadata: this._metadata.modelMath.modelEquation
				},
				exposure: {
					type: 'complex',
					schema: this._schema.exposure,
					metadata: this._metadata.modelMath.exposure
				},
				plot: {
					type: 'plot'
				}
			};
		}
	}]);

	return ModelHandler;
}();

var GenericModel = function (_ModelHandler) {
	_inherits(GenericModel, _ModelHandler);

	function GenericModel(metadata, img, state) {
		_classCallCheck(this, GenericModel);

		var _this = _possibleConstructorReturn(this, (GenericModel.__proto__ || Object.getPrototypeOf(GenericModel)).call(this, metadata, img));

		_this._schema = schemas.genericModel;
		console.log(state);
		if (state) {
			_this.panels = _this._createPanels();
			console.log('logggg', _this.panels);
		}
		_this._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "dietaryAssessmentMethod",
				label: "Dietary assessment method"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "modelMath",
				label: "General"
			}, {
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}, {
				id: "exposure",
				label: "Exposure"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}];
		_get(GenericModel.prototype.__proto__ || Object.getPrototypeOf(GenericModel.prototype), '_create', _this).call(_this);
		return _this;
	}

	_createClass(GenericModel, [{
		key: 'validate',


		// Validate this.panels and return boolean
		value: function validate() {
			var isValid = true;
			if (!this.panels.generalInformation.validate()) isValid = false;
			if (!this.panels.modelCategory.validate()) isValid = false;
			if (!this.panels.scopeGeneral.validate()) isValid = false;
			if (!this.panels.study.validate()) isValid = false;
			return isValid;
		}
	}, {
		key: '_createPanels',
		value: function _createPanels() {
			var port = window.port || -1;
			var schema = schemas.genericModel;
			return {
				generalInformation: new FormPanel("General", schema.generalInformation, this._metadata.generalInformation, port),
				modelCategory: new FormPanel("Model category", schema.modelCategory, this._metadata.generalInformation.modelCategory, port),
				author: new TablePanel("Author", schema.contact, this._metadata.generalInformation.author, port),
				creator: new TablePanel("Creator", schema.contact, this._metadata.generalInformation.creator, port),
				reference: new TablePanel("Reference", schema.reference, this._metadata.generalInformation.reference, port),
				scopeGeneral: new FormPanel("General", schema.scope, this._metadata.scope, port),
				product: new TablePanel("Product", schema.product, this._metadata.scope.product, port),
				hazard: new TablePanel("Hazard", schema.hazard, this._metadata.scope.hazard, port),
				population: new TablePanel("Population", schema.populationGroup, this._metadata.scope.populationGroup, port),
				study: new FormPanel("Study", schema.study, this._metadata.dataBackground.study, port),
				studySample: new TablePanel("Study sample", schema.studySample, this._metadata.dataBackground.studySample, port),
				dietaryAssessmentMethod: new TablePanel("Dietary assessment method", schema.dietaryAssessmentMethod, this._metadata.dataBackground.dietaryAssessmentMethod, port),
				laboratory: new TablePanel("Laboratory", schema.laboratory, this._metadata.dataBackground.laboratory, port),
				assay: new TablePanel("Assay", schema.assay, this._metadata.dataBackground.assay, port),
				modelMath: new FormPanel("Model math", schema.modelMath, this._metadata.modelMath, port),
				parameter: new TablePanel("Parameter", schema.parameter, this._metadata.modelMath.parameter, port),
				qualityMeasures: new TablePanel("Quality measures", schema.qualityMeasures, this._metadata.modelMath.qualityMeasures, port),
				modelEquation: new TablePanel("Model equation", schema.modelEquation, this._metadata.modelMath.modelEquation, port),
				exposure: new TablePanel("Exposure", schema.exposure, this._metadata.modelMath.exposure, port)
			};
		}
	}, {
		key: 'metaData',
		get: function get() {
			try {
				// generalInformation
				this._metadata.generalInformation = this.panels.generalInformation.data;
				this._metadata.generalInformation.modelCategory = this.panels.modelCategory.data;
				this._metadata.generalInformation.author = this.panels.author.data;
				this._metadata.generalInformation.creator = this.panels.creator.data;
				this._metadata.generalInformation.reference = this.panels.reference.data;

				// Scope
				this._metadata.scope = this.panels.scopeGeneral.data;
				this._metadata.scope.product = this.panels.product.data;
				this._metadata.scope.hazard = this.panels.hazard.data;
				this._metadata.scope.populationGroup = this.panels.population.data;

				// Data background
				this._metadata.dataBackground.study = this.panels.study.data;
				this._metadata.dataBackground.studySample = this.panels.studySample.data;
				this._metadata.dataBackground.dietaryAssessmentMethod = this.panels.dietaryAssessmentMethod.data;
				this._metadata.dataBackground.laboratory = this.panels.laboratory.data;
				this._metadata.dataBackground.assay = this.panels.assay.data;

				// Model math
				this._metadata.modelMath = this.panels.modelMath.data;
				this._metadata.modelMath.parameter = this.panels.parameter.data;
				this._metadata.modelMath.parameter.forEach(function (param) {
					return delete param.reference;
				});

				this._metadata.modelMath.qualityMeasures = this.panels.qualityMeasures.data;
				this._metadata.modelMath.modelEquation = this.panels.modelEquation.data;
				this._metadata.modelMath.exposure = this.panels.exposure.data;

				this._metadata.modelType = "genericModel";

				this._metadata = metadataFix(this._metadata);
			} catch (error) {
				console.log(error);
			}
			return this._metadata;
		}
	}]);

	return GenericModel;
}(ModelHandler);

var DataModel = function (_ModelHandler2) {
	_inherits(DataModel, _ModelHandler2);

	function DataModel(metadata, img) {
		_classCallCheck(this, DataModel);

		var _this2 = _possibleConstructorReturn(this, (DataModel.__proto__ || Object.getPrototypeOf(DataModel)).call(this, metadata, img));

		_this2._schema = schemas.dataModel;
		_this2._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "dietaryAssessmentMethod",
				label: "Dietary assessment method"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "parameter",
				label: "Parameter"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}];
		_get(DataModel.prototype.__proto__ || Object.getPrototypeOf(DataModel.prototype), '_create', _this2).call(_this2);
		return _this2;
	}

	return DataModel;
}(ModelHandler);

var PredictiveModel = function (_ModelHandler3) {
	_inherits(PredictiveModel, _ModelHandler3);

	function PredictiveModel(metadata, img) {
		_classCallCheck(this, PredictiveModel);

		var _this3 = _possibleConstructorReturn(this, (PredictiveModel.__proto__ || Object.getPrototypeOf(PredictiveModel)).call(this, metadata, img));

		_this3._schema = schemas.predictiveModel;
		_this3._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "hazard",
				label: "Hazard"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "parameter",
				label: "Parameter"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}];
		_get(PredictiveModel.prototype.__proto__ || Object.getPrototypeOf(PredictiveModel.prototype), '_create', _this3).call(_this3);
		return _this3;
	}

	return PredictiveModel;
}(ModelHandler);

var OtherModel = function (_ModelHandler4) {
	_inherits(OtherModel, _ModelHandler4);

	function OtherModel(metadata, img) {
		_classCallCheck(this, OtherModel);

		var _this4 = _possibleConstructorReturn(this, (OtherModel.__proto__ || Object.getPrototypeOf(OtherModel)).call(this, metadata, img));

		_this4._schema = schemas.otherModel;
		_this4._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "modelMath",
				label: "General"
			}, {
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}];
		_get(OtherModel.prototype.__proto__ || Object.getPrototypeOf(OtherModel.prototype), '_create', _this4).call(_this4);
		return _this4;
	}

	return OtherModel;
}(ModelHandler);

var DoseResponseModel = function (_ModelHandler5) {
	_inherits(DoseResponseModel, _ModelHandler5);

	function DoseResponseModel(metadata, img) {
		_classCallCheck(this, DoseResponseModel);

		var _this5 = _possibleConstructorReturn(this, (DoseResponseModel.__proto__ || Object.getPrototypeOf(DoseResponseModel)).call(this, metadata, img));

		_this5._schema = schemas.doseResponseModel;
		_this5._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "modelMath",
				label: "General"
			}, {
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}, {
				id: "exposure",
				label: "Exposure"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}];
		// extend panels with specific data and schemas
		_this5._panels = $.extend(true, {}, _this5._panels, {
			study: {
				type: 'simple',
				schema: _this5._schema.study,
				metadata: _this5._metadata.scope.study
			},
			studySample: {
				type: 'complex',
				schema: _this5._schema.study,
				metadata: _this5._metadata.scope.studySample
			},
			laboratory: {
				type: 'complex',
				schema: _this5._schema.laboratory,
				metadata: _this5._metadata.scope.laboratory
			},
			assay: {
				type: 'complex',
				schema: _this5._schema.assay,
				metadata: _this5._metadata.scope.assay
			},
			exposure: {
				type: 'complex',
				schema: _this5._schema.exposure,
				metadata: _this5._metadata.modelMath.exposure
			}
		});
		return _this5;
	}

	return DoseResponseModel;
}(ModelHandler);

var ToxicologicalModel = function (_ModelHandler6) {
	_inherits(ToxicologicalModel, _ModelHandler6);

	function ToxicologicalModel(metadata, img) {
		_classCallCheck(this, ToxicologicalModel);

		var _this6 = _possibleConstructorReturn(this, (ToxicologicalModel.__proto__ || Object.getPrototypeOf(ToxicologicalModel)).call(this, metadata, img));

		_this6._schema = schemas.toxicologicalModel;
		_this6._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "modelMath",
				label: "General"
			}, {
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}, {
				id: "exposure",
				label: "Exposure"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}];

		// extend panels with specific data and schemas
		_this6._panels = $.extend(true, {}, _this6._panels, {
			study: {
				type: 'simple',
				schema: _this6._schema.study,
				metadata: _this6._metadata.scope.study
			},
			studySample: {
				type: 'complex',
				schema: _this6._schema.study,
				metadata: _this6._metadata.scope.studySample
			},
			laboratory: {
				type: 'complex',
				schema: _this6._schema.laboratory,
				metadata: _this6._metadata.scope.laboratory
			},
			assay: {
				type: 'complex',
				schema: _this6._schema.assay,
				metadata: _this6._metadata.scope.assay
			},
			exposure: {
				type: 'complex',
				schema: _this6._schema.exposure,
				metadata: _this6._metadata.modelMath.exposure
			}
		});
		return _this6;
	}

	return ToxicologicalModel;
}(ModelHandler);

var ExposureModel = function (_ModelHandler7) {
	_inherits(ExposureModel, _ModelHandler7);

	function ExposureModel(metadata, img) {
		_classCallCheck(this, ExposureModel);

		var _this7 = _possibleConstructorReturn(this, (ExposureModel.__proto__ || Object.getPrototypeOf(ExposureModel)).call(this, metadata, img));

		_this7._schema = schemas.exposureModel;
		_this7._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "dietaryAssessmentMethod",
				label: "Dietary assessment method"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "modelMath",
				label: "General"
			}, {
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}, {
				id: "exposure",
				label: "Exposure"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}];
		_get(ExposureModel.prototype.__proto__ || Object.getPrototypeOf(ExposureModel.prototype), '_create', _this7).call(_this7);
		return _this7;
	}

	return ExposureModel;
}(ModelHandler);

var ProcessModel = function (_ModelHandler8) {
	_inherits(ProcessModel, _ModelHandler8);

	function ProcessModel(metadata, img) {
		_classCallCheck(this, ProcessModel);

		var _this8 = _possibleConstructorReturn(this, (ProcessModel.__proto__ || Object.getPrototypeOf(ProcessModel)).call(this, metadata, img));

		_this8._schema = schemas.processModel;
		_this8._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "hazard",
				label: "Hazard"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}];
		_get(ProcessModel.prototype.__proto__ || Object.getPrototypeOf(ProcessModel.prototype), '_create', _this8).call(_this8);
		return _this8;
	}

	return ProcessModel;
}(ModelHandler);

var ConsumptionModel = function (_ModelHandler9) {
	_inherits(ConsumptionModel, _ModelHandler9);

	function ConsumptionModel(metadata, img) {
		_classCallCheck(this, ConsumptionModel);

		var _this9 = _possibleConstructorReturn(this, (ConsumptionModel.__proto__ || Object.getPrototypeOf(ConsumptionModel)).call(this, metadata, img));

		_this9._schema = schemas.consumptionModel;
		_this9._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "populationGroup",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}];
		_get(ConsumptionModel.prototype.__proto__ || Object.getPrototypeOf(ConsumptionModel.prototype), '_create', _this9).call(_this9);
		return _this9;
	}

	return ConsumptionModel;
}(ModelHandler);

var HealthModel = function (_ModelHandler10) {
	_inherits(HealthModel, _ModelHandler10);

	function HealthModel(metadata, img) {
		_classCallCheck(this, HealthModel);

		var _this10 = _possibleConstructorReturn(this, (HealthModel.__proto__ || Object.getPrototypeOf(HealthModel)).call(this, metadata, img));

		_this10._schema = schemas.healthModel;
		_this10._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "modelMath",
				label: "General"
			}, {
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}, {
				id: "exposure",
				label: "Exposure"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}];
		return _this10;
	}

	return HealthModel;
}(ModelHandler);

var RiskModel = function (_ModelHandler11) {
	_inherits(RiskModel, _ModelHandler11);

	function RiskModel(metadata, img) {
		_classCallCheck(this, RiskModel);

		var _this11 = _possibleConstructorReturn(this, (RiskModel.__proto__ || Object.getPrototypeOf(RiskModel)).call(this, metadata, img));

		_this11._schema = schemas.riskModel;
		_this11._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "dietaryAssessmentMethod",
				label: "Dietary assessment method"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "modelMath",
				label: "General"
			}, {
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}, {
				id: "exposure",
				label: "Exposure"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}];
		_get(RiskModel.prototype.__proto__ || Object.getPrototypeOf(RiskModel.prototype), '_create', _this11).call(_this11);
		return _this11;
	}

	return RiskModel;
}(ModelHandler);

var QraModel = function (_ModelHandler12) {
	_inherits(QraModel, _ModelHandler12);

	function QraModel(metadata, img) {
		_classCallCheck(this, QraModel);

		var _this12 = _possibleConstructorReturn(this, (QraModel.__proto__ || Object.getPrototypeOf(QraModel)).call(this, metadata, img));

		_this12._schema = schemas.qraModel;
		_this12._menu = [{
			label: "General information",
			submenus: [{
				id: "generalInformation",
				label: "General"
			}, {
				id: "modelCategory",
				label: "Model category"
			}, {
				id: "author",
				label: "Author"
			}, {
				id: "creator",
				label: "Creator"
			}, {
				id: "reference",
				label: "Reference"
			}]
		}, {
			label: "Scope",
			submenus: [{
				id: "scopeGeneral",
				label: "General"
			}, {
				id: "product",
				label: "Product"
			}, {
				id: "hazard",
				label: "Hazard"
			}, {
				id: "population",
				label: "Population group"
			}]
		}, {
			label: "Data Background",
			submenus: [{
				id: "study",
				label: "Study"
			}, {
				id: "studySample",
				label: "Study sample"
			}, {
				id: "dietaryAssessmentMethod",
				label: "Dietary assessment method"
			}, {
				id: "laboratory",
				label: "Laboratory"
			}, {
				id: "assay",
				label: "Assay"
			}]
		}, {
			label: "Model math",
			submenus: [{
				id: "modelMath",
				label: "General"
			}, {
				id: "parameter",
				label: "Parameter"
			}, {
				id: "qualityMeasures",
				label: "Quality measures"
			}, {
				id: "modelEquation",
				label: "Model equation"
			}, {
				id: "exposure",
				label: "Exposure"
			}]
		}, {
			label: "Plot",
			id: 'plot',
			submenus: []
		}];
		_get(QraModel.prototype.__proto__ || Object.getPrototypeOf(QraModel.prototype), '_create', _this12).call(_this12);
		return _this12;
	}

	return QraModel;
}(ModelHandler);
/** Temporary workaround for some metadata glitches. */


var metadataFix = function metadataFix(metadata) {
	// Ignore temporarily publication type
	// TODO: publicationType takes the abbreviation instead of the full string
	// used in the Reference dialog. Since KNIME runs getComponentValue twice,
	// the value cannot be converted here. The 1st call to getComponentValue
	// would get the abbreviation but the 2nd call would corrupt it. The HTML
	// select should instead use the full string as label and the abreviation
	// as value.
	metadata.generalInformation.reference.forEach(function (ref) {
		return delete ref.publicationType;
	});

	/* TODO: Ignore temporarily reference.
 The reference property is of type Reference in the schema. Unfortunately,
 nested dialogs are not supported in Bootstrap, so the type is changed
 in the UI schema to text. Since the text type cannot be deserialized to
 Reference, the values are discarded temporarily here.*/
	metadata.modelMath.parameter.forEach(function (param) {
		return delete param.reference;
	});

	return metadata;
};
/**
 * 
 * @param {*} name 
 * @param {*} isMandatory 
 * @param {*} description 
 */
var createLabel = function createLabel(name, isMandatory, description) {
	var label = document.createElement("label");
	label.classList.add("col-sm-2", "control-label");
	label.title = description;
	label.setAttribute("data-toggle", "tooltip");
	label.textContent = name + (isMandatory ? "*" : "");

	$(label).tooltip(); // Enable Bootstrap tooltip

	return label;
};

/**
 * Create a Bootstrap dropdown menu.
 * @param {string} name Menu name 
 * @param {array} submenus Array of hashes of id and name of the submenus.
 */
var createSubMenu = function createSubMenu(name, submenus) {
	return '<li class="dropdown">\n\t<a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button"\n\t\taria-haspopup="true" aria-expanded="false">' + name + '<span class="caret"></a>\n\t<ul class="dropdown-menu">\n\t' + submenus.map(function (entry) {
		return '<li><a href="#' + entry.id + '" aria-controls="#' + entry.id + '"\n\t\trole="button" data-toggle="tab">' + entry.label + '</a></li>';
	}).join("") + '\n\t</ul>\n\t</li>';
};

/**
 * Add controlled vocabulary to an input.
 * @param {Element} input Input element
 * @param {Array} vocabulary String array with vocabulary terms.
 */
var addControlledVocabulary = function addControlledVocabulary(input, vocabulary, port) {
	if (port >= 0) {
		fetch('http://localhost:' + port + '/getAllNames/' + vocabulary).then(function (response) {
			return response.json();
		}).then(function (data) {
			$(input).typeahead({
				source: data,
				autoSelect: true,
				fitToElement: true,
				showHintOnFocus: true
			});
		});
	}
};

/**
 * Create an horizontal form for a metadata property. Missing values with
 * *null* or *undefined* are replaced with an empty string.
 * 
 * @param {object} prop Metadata property. It can be of type: text, number,
 *  url, data, boolean, text-array and date-array.
 * @param {string} value Input value. It can be *null* or *undefined* for
 *  missing values.
 * 
 * @returns InputForm or ArrayForm for the supported type. If wrong type
 *  it returns undefined.
 */
var createForm = function createForm(prop, value, port) {
	var isMandatory = prop.required ? prop.required : false;

	if (prop.type === "text" || prop.type === "number" || prop.type === "url" || prop.type === "date" || prop.type === "email") return new InputForm(prop.label, isMandatory, prop.type, prop.description, value ? value : "", port, prop.vocabulary, prop.sid);else if (prop.type === "long-text") {
		return new TextareaForm(prop.label, isMandatory, prop.description, value ? value : "");
	} else if (prop.type === "enum") return new SelectForm(prop.label, isMandatory, prop.description, value, port, prop.vocabulary);else if (prop.type === "boolean") return new InputForm(prop.label, false, "checkbox", prop.description, value, port);else if (prop.type === "text-array") return new ArrayForm(prop.label, isMandatory, prop.type, value ? value : [], prop.description, port, prop.vocabulary);else if (prop.type === "date-array") return new ArrayForm(prop.label, isMandatory, prop.type, value ? value : [], prop.description, port, prop.vocabulary);
};

/**
 * LOG
 * logs message to console, if window._debug flag is set for this class
 * @param {string} log: log message
 */

var _log = function _log(log, style) {
	if (window._debug) {
		var styles = {
			primary: 'background: #000; color:#fff; padding: 1px 2px;',
			secondary: 'background: #aaa; color:#fff; padding: 1px 2px;',
			level1: 'padding-left: 10px',
			level2: 'padding-left: 20px',
			strong: 'font-weight: bold;',
			error: 'background: red; color: #ffffff; padding: 1px 2px;',
			warning: 'background: yellow; padding: 1px 2px;',
			hook: 'background: orange; padding: 1px 2px;'
		};
		if (style in styles) {
			console.log('%c' + log, styles[style]);
		} else {
			console.log(log);
		}
	}
};
/**
 * 
 */
var _createParamMetadataList = function _createParamMetadataList(helperText) {
	var O = undefined;

	// create table
	var $table = $('<table class="table table-sm table-hover table-params-metadata"></table>');
	// create rows
	if (helperText) {
		var $row = $('<tr></tr>').appendTo($table);
		$row.append('<td>' + helperText + '</td>'); // value
	}
	return $table;
};
/** 
  * is undefined
  */

var _isUndefined = function _isUndefined(val) {
	return (typeof val === 'undefined' ? 'undefined' : _typeof(val)) === (typeof undefined === 'undefined' ? 'undefined' : _typeof(undefined)) ? true : false;
};

/**
  * is null
  */

var _isNull = function _isNull(val) {
	return val == null ? true : false;
};

/**
  * sorter functions
  */

var _sorter = {
	_name: function _name(a, b) {
		// add a method called name
		return a.localeCompare(b);
		// aa = a.replace( /^the /i, '' ); // remove 'The' from start of parameter
		// bb = b.replace( /^the /i, '' ); // remove 'The' from start of parameter

		// if ( aa < bb ) { // if value a is less than value b
		// 	return -1;
		// }
		// else { // Otherwise
		// 	return aa > bb ? 1 : 0; // if a is greater than b return 1 OR
		// }
	},
	_execution: function _execution(a, b) {
		var aa = parseFloat(a); // remove s for seconds
		var bb = parseFloat(b); // remove s for seconds
		return aa - bb;
	},
	_date: function _date(a, b) {
		var aa = new Date(a);
		var bb = new Date(b);
		return aa - bb;
	}

	/**
   * formatter functions
   */

};var _formatter = {
	_list: function _list(val) {
		var $ul = $('<ul></ul>');
		if (val && (typeof val === 'undefined' ? 'undefined' : _typeof(val)) === 'object' && val !== null) {
			var _iteratorNormalCompletion = true;
			var _didIteratorError = false;
			var _iteratorError = undefined;

			try {
				for (var _iterator = val.values()[Symbol.iterator](), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
					var _item = _step.value;

					var $li = $('<li>' + _item + '</li>').appendTo($ul);
				}
			} catch (err) {
				_didIteratorError = true;
				_iteratorError = err;
			} finally {
				try {
					if (!_iteratorNormalCompletion && _iterator.return) {
						_iterator.return();
					}
				} finally {
					if (_didIteratorError) {
						throw _iteratorError;
					}
				}
			}

			;
			return $ul;
		} else if (val && $.isArray(val) && val.length > 0) {
			$.each(val, function (i, li) {
				var $li = $('<li>' + item + '</li>').appendTo($ul);
			});
			return $ul;
		}
		return val;
	},
	_uploadDate: function _uploadDate(val) {
		if (val && val.indexOf(' | ') >= 0) {
			var time = val.substring(val.indexOf(' | '));
			return val.replace(time, '<br><small>' + time + '</small>').replace('|', '');
		}
		return val;
	},
	_searchHighlight: function _searchHighlight(val, search) {
		// prevents highlighting of html parts that fit search 
		var rx = new RegExp('(?![^<]+>)' + search, 'gi');
		// return val.toString().replace( new RegExp('(<.*?>)(' + search + '?.)(</.*?>)', 'g'), '<mark>$2</mark>' );
		return val.replace(rx, '<mark>$&</mark>');
	}

	/**
   * fetch data from src by id and type
   */

};var _fetchData = {
	_json: async function _json(src, id) {
		_log('UTILS / _fetchData.json: ' + src + ', ' + id);
		var data = null;
		// append id if not type "set"
		src = !_isNull(id) ? src + id : src;

		var response = await fetch(src);
		data = await response.json();

		return data;
	},
	_blob: async function _blob(src, id) {
		_log('UTILS / _fetchData.blob: ' + src + ', ' + id);
		var data = null;
		// append id if not type "set"
		src = !_isNull(id) ? src + id : src;

		var response = await fetch(src);
		var blob = await response.blob();

		var urlCreator = window.URL || window.webkitURL || window;
		data = urlCreator.createObjectURL(blob);

		return data;
	},
	_content: async function _content(src, id) {
		_log('UTILS / _fetchData.content: ' + src + ', ' + id);
		var data = null;
		// append id if not type "set"
		src = !_isNull(id) ? src + id : src;

		var response = await fetch(src);
		data = await response.text();

		return data;
	},
	_array: async function _array(src, arrayLength) {
		_log('UTILS / _fetchData.json: ' + src + ', ' + arrayLength);
		var data = [];

		var _loop = async function _loop(i) {
			await fetch(src + i).then(function (resp) {
				return resp.text().then(function (text) {
					data[i] = text;
				});
			});
		};

		for (var i = 0; i < arrayLength; i++) {
			await _loop(i);
		}

		return data;
	}

	/**
  * CHECK UNDEFINED CONTENT
  * checks if value is empty and replace the empty value with default or custom placeholder for table views 
  * @param {string/object} value: any value
  * @param {string} placeholder: any string type as placeholder
  */

};var _checkUndefinedContent = function _checkUndefinedContent(value, placeholder) {
	// placeholder custom || default
	placeholder = placeholder || '-';

	// check empty criterions
	if (_isUndefined(value) || _isNull(value) || (typeof value === 'undefined' ? 'undefined' : _typeof(value)) == 'object' && Object.keys(value).length === 0 && value.constructor === Object || (typeof value === 'undefined' ? 'undefined' : _typeof(value)) == 'object' && value.length == 1 && _isNull(value[0]) || value.length <= 0) {

		value = placeholder;
	}
	return value;
};

/**
  * strip html tags
  */

var _stripHtmlTags = function _stripHtmlTags(str) {
	if (str === null || str === '') {
		return false;
	} else {
		str = str.toString();
		return str.replace(/<[^>]*>/g, '');
	}
};

/**
 * GET DOM TEXT
 * get pure text of dom elements
 * @param {element} element: dom element
 */

var _getDOMText = function _getDOMText(node) {
	var O = undefined;

	var text = void 0;

	if (node.outerText) {
		text = node.outerText.trim();
	} else if (node.innerText) {
		text = node.innerText.trim();
	} else {
		text = '';
	}

	if (node.childNodes) {
		node.childNodes.forEach(function (child) {
			return text += _getDOMText(child);
		});
	}

	return text;
};

/**
  * has attribute
  */

$.fn._hasAttr = function (attrName) {
	if ($(undefined)) {
		var attr = $(undefined).attr(attrName);
		if ((typeof attr === 'undefined' ? 'undefined' : _typeof(attr)) !== (typeof undefined === 'undefined' ? 'undefined' : _typeof(undefined)) && attr !== false) {
			// element has this attribute
			return true;
		}
	}
	return false;
};
/*

version: 1.0.0
author: sascha obermüller
date: 06.12.2020

*/

var APPModal = function () {
	function APPModal(settings, $container) {
		_classCallCheck(this, APPModal);

		var O = this;
		O._$container = $container;
		O._debug = true;
		O._initiated = false;
		// defaults
		O._opts = $.extend(true, {}, {
			classes: '',
			data: null,
			on: {
				afterInit: null, // function
				show: function show(O, event) {
					O._updateModal(event);
				}, // function
				hide: null // function
			}
		}, settings);
		// basic init actions
		O._create();
		O._initiated = true;
		// callback
		if ($.isFunction(O.opts.on.afterInit)) {
			O.opts.on.afterInit.call(O);
		}
	}

	_createClass(APPModal, [{
		key: '_create',


		/**
   * CREATE
   * build basic model parts: modal header, body
   */

		value: function _create() {
			var O = this;
			_log('MODAL / _create');

			O._id = O.opts.id || 'modal' + $.now();
			O._type = O.opts.type || 'default';

			O._$modal = $('<div class="modal fade" tabindex="-1" role="dialog" aria-hidden="true"></div>').attr('id', O._id).addClass(O.opts.classes).appendTo(O._$container);

			O._$modalContent = $('<div class="modal-content"></div>').appendTo(O._$modal).wrap('<div class="modal-dialog modal-xl" role="document"></div>');

			// loader
			O._loader = _appUI._createLoader({ classes: 'loader-modal' }, O._$modalContent);

			// create modal
			O._createModal();

			// create bs modal
			O._$modal.modal({
				show: false // initially hidden
			});

			// bind show event
			O._$modal.on('show.bs.modal', function (event) {
				// callback
				if ($.isFunction(O.opts.on.show)) {
					O.opts.on.show.call(O, O, event);
				}
			});

			// bind hide event
			O._$modal.on('hide.bs.modal', function (event) {
				// callback
				if ($.isFunction(O.opts.on.hide)) {
					O.opts.on.hide.call(O, O, event);
				}
			});
		}

		/**
   * CREATE MODAL
   * creates basic modal components: header and blank body
   */

	}, {
		key: '_createModal',
		value: function _createModal() {
			var O = this;
			_log('MODAL / _createModal');

			// modal head default
			O._createModalHead();

			// modal body default
			O._createModalBody().appendTo(O._$modalContent);
		}

		/**
   * CREATE MODAL HEAD
   * creates basic modal header with title and close
   */

	}, {
		key: '_createModalHead',
		value: function _createModalHead() {
			var O = this;
			_log('MODAL / _createModalHead');

			// modal head
			O._$modalHead = $('<div class="modal-header"></div>').appendTo(O._$modalContent);
			// modal head title
			O._$modalTitle = $('<h1 class="modal-title"></h1>').appendTo(O._$modalHead);
			// modal close
			$('<button type="button" class="action action-pure action-lg ml-2" data-dismiss="modal" aria-label="Close"><i class="feather icon-x"></i></button>').appendTo(O._$modalHead);
		}

		/**
   * CREATE MODAL
   * creates basic modal components: header and blank body
   */

	}, {
		key: '_createModalBody',
		value: function _createModalBody() {
			var O = this;
			_log('MODAL / _createBody');

			O._$modalBody = $('<div class="modal-body"></div>').appendTo(O._$modalContent);
		}

		/**
   * BUILD MODAL
   * build modal blank function
   */

	}, {
		key: '_updateModal',
		value: async function _updateModal(event) {
			var O = this;
			_log('MODAL / _updateModal');

			// let $trigger = $( event.relatedTarget );
			// let modalID = $trigger.data( 'modal-id' );
		}

		/**
   * SET TITLE
   * set modal title
   */

	}, {
		key: '_setTitle',
		value: function _setTitle(text) {
			var O = this;
			_log('MODAL / _setTitle: ' + text);

			if (O._$modalTitle) {
				O._$modalTitle.text(text);
			}
		}

		/**
   * SHOW
   * show modal
   */

	}, {
		key: '_show',
		value: function _show() {
			var O = this;
			O._$modal.modal('show');
		}

		/**
   * HIDE
   * hide modal
   */

	}, {
		key: '_hide',
		value: function _hide() {
			var O = this;
			O._$modal.modal('hide');
		}

		/**
   * CLEAR MODAL
   * removes table body rows
   */

	}, {
		key: '_clear',
		value: function _clear() {
			var O = this;
			_log('MODAL / _clear');

			if (O._$modalContent) {
				O._$modalContent.empty();
			}
		}
	}, {
		key: 'opts',
		get: function get() {
			return this._opts;
		},
		set: function set(settings) {
			this._opts = $.extend(true, {}, this.opts, settings);
		}
	}]);

	return APPModal;
}();
/*

version: 1.0.0
author: sascha obermüller
date: 07.12.2020

*/

var APPModalMTDetails = function (_APPModal) {
	_inherits(APPModalMTDetails, _APPModal);

	function APPModalMTDetails(settings, $container) {
		_classCallCheck(this, APPModalMTDetails);

		return _possibleConstructorReturn(this, (APPModalMTDetails.__proto__ || Object.getPrototypeOf(APPModalMTDetails)).call(this, settings, $container));
	}

	/**
  * CREATE
  * calls super class and sets _metadata
  */

	_createClass(APPModalMTDetails, [{
		key: '_create',
		value: function _create() {
			var O = this;
			_log('MODAL DETAILS / _create', 'primary');

			O._metadata = O.opts.data;

			_get(APPModalMTDetails.prototype.__proto__ || Object.getPrototypeOf(APPModalMTDetails.prototype), '_create', this).call(this);
		}

		/**
   * CREATE MODAL
   * creates basic modal components: header and blank body
   */

	}, {
		key: '_createModal',
		value: function _createModal() {
			var O = this;
			_log('MODAL DETAILS / _createModal');

			// modal head default
			O._createModalHead();

			O.ModelMTPanel = new APPMTEditableDetails(O.opts, O._$modalContent);
			O.ModelMTPanel._createModelMetadataContent();
		}

		/**
   * BUILD MODAL
   * build modal content
   */

	}, {
		key: '_updateModal',
		value: async function _updateModal(event) {
			var O = this;
			_log('MODAL DETAILS / _updateModal');
			_log(event);

			O._loader._setState(true); // set loader

			// get trigger
			var $trigger = $(event.relatedTarget);
			// get model id & data
			O._modelId = $trigger.data('modal-id');
			O._modelMetadata = O._metadata[O._modelId];

			// modal title
			if (O._modelMetadata.generalInformation && O._modelMetadata.generalInformation.name) {
				O._setTitle(O._modelMetadata.generalInformation.name);
			}
			await O.ModelMTPanel._updateContent(O._modelMetadata, O._modelId);
			O._loader._setState(false); // set loader
		}
	}]);

	return APPModalMTDetails;
}(APPModal);

var AdvancedTable = function () {
	function AdvancedTable(data, formData, dialog, panel) {
		var _this14 = this;

		_classCallCheck(this, AdvancedTable);

		this.formData = formData;
		this.dialog = dialog;
		this.panel = panel;

		this.table = document.createElement("table");
		this.table.className = "table";

		// Apply striped rows if table has over 10 rows.
		if (data && data.length > 10) {
			this.table.classList.add("table-striped");
		}

		// Create headers (1 extra columns at the end for buttons)
		var head = document.createElement("thead");
		head.innerHTML = '<tr>\n        ' + this.formData.map(function (prop) {
			return '<th>' + prop.label + '</th>';
		}).join("") + '\n        <th></th>\n      </tr>';

		this.body = document.createElement("tbody");
		this.table.appendChild(head);
		this.table.appendChild(this.body);

		if (data) {
			data.forEach(function (entry) {
				return _this14.add(entry);
			});
		}
	}

	/**
  * Create a new row with new metadata from a dialog.
  * 
  * @param {Object} data JSON object with new metadata.
  */


	_createClass(AdvancedTable, [{
		key: 'add',
		value: function add(data) {
			var _this15 = this;

			// Add new row (Order is fixed by formData)
			var newRow = document.createElement("tr");

			this.formData.forEach(function (prop) {
				// Get value for the current property
				var value = data[prop.id] ? data[prop.id] : "";

				var cell = document.createElement("td");
				if (prop.type === "boolean" && value) {
					cell.innerHTML = '<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>';
				} else {
					cell.title = value; // Set the whole value as tooltip
					cell.textContent = value.length > 20 ? value.substring(0, 24) + "..." : value;
				}
				newRow.appendChild(cell);
			});

			var editButton = document.createElement("button");
			editButton.classList.add("btn", "btn-primary", "btn-sm");
			editButton.innerHTML = '<i class="glyphicon glyphicon-edit"></i>';
			editButton.title = "Edit";
			editButton.onclick = function (e) {

				// Get current row (button > btn-group > td > tr). It starts at 1
				// (it counts the header)
				var rowIndex = e.currentTarget.parentNode.parentNode.parentNode.rowIndex - 1;

				// Update inputs in dialog
				var originalData = _this15.panel.data[rowIndex];
				for (var prop in originalData) {
					_this15.dialog.inputs[prop].value = originalData[prop];
				}

				_this15.dialog.editedRow = rowIndex;
				$(_this15.dialog.modal).modal('show');
			};

			// Remove button
			var removeButton = document.createElement("button");
			removeButton.classList.add("btn", "btn-warning", "btn-sm");
			removeButton.innerHTML = '<i class="glyphicon glyphicon-remove"></i>';
			removeButton.onclick = function (e) {
				// Get current row (button > btn-group > td > tr). It starts at 1
				// (it counts the header)
				var rowIndex = e.currentTarget.parentNode.parentNode.parentNode.rowIndex - 1;
				_this15.panel.remove(rowIndex);
			};

			removeButton.title = "Remove";

			var btnGroup = document.createElement("div");
			btnGroup.className = "btn-group";
			btnGroup.setAttribute("role", "group");
			btnGroup.appendChild(editButton);
			btnGroup.appendChild(removeButton);

			var buttonCell = document.createElement("td");
			buttonCell.appendChild(btnGroup);
			newRow.appendChild(buttonCell);

			this.body.appendChild(newRow);
		}
	}, {
		key: 'edit',
		value: function edit(rowNumber, data) {
			var row = this.body.childNodes[rowNumber];

			for (var i = 0; i < this.formData.length; i++) {
				var prop = this.formData[i];
				var cell = row.childNodes[i];

				var value = data[prop.id];
				cell.title = value;
				cell.textContent = value.length > 25 ? value.substring(0, 24) : value;
			}
		}

		/**
   * Remove row at the given index.
   */

	}, {
		key: 'remove',
		value: function remove(index) {
			this.body.removeChild(this.body.childNodes[index]);
		}

		/**
   * Remove every row in the table.
   */

	}, {
		key: 'trash',
		value: function trash() {
			this.body.innerHTML = "";
		}
	}]);

	return AdvancedTable;
}();
/**
 * Create a div to edit string arrays.
 * 
 * ```
 * <div class="panel panel-default">
 *   <div class="panel-heading clearfix">
 *     <h4 class="panel-title pull-left" style="padding-top:7.5px;">Title</h4>
 *     <div class="input-group">
 *       <p class="pull-right" /> <!-- gutter -->
 *       <div class="input-group-btn">
 *         <button type="button" class="btn btn-default" data-toggle="modal" data-target="#">
 *           <i class="glyphicon glyphicon-plus"></i>
 *         </button>
 *         <button class="btn btn-default"><i class="glyphicon glyphicon-remove"></i></button>
 *         <button class="btn btn-default"><i class="glyphicon glyphicon-trash"></i></button>
 *       </div>
 *      </div>
 *    </div>
 *   <table id="${table}" class="table"></table>
 * </div>
 * ```
 */


var ArrayForm = function () {
	function ArrayForm(name, mandatory, type, value, helperText, vocabulary, port) {
		_classCallCheck(this, ArrayForm);

		this.group = document.createElement("div");
		this.mandatory = mandatory;
		this.simpleTable = new SimpleTable(type, value, vocabulary, port);
		this._create(name, mandatory, helperText);
	}

	_createClass(ArrayForm, [{
		key: '_create',
		value: function _create(name, mandatory, helperText) {
			var _this16 = this;

			if (name) {

				// formgroup
				$formGroup = $('<div class="form-group row"></div>');
				// .appendTo( O._$simForm );

				// label
				var _$label = $('<label class="col-form-label col-form-label-sm col-9 col-xs-3 order-1 sim-param-label"></label>').attr('for', 'input_' + name).appendTo($formGroup);
				_$label.text(name + (mandatory ? "*" : ""));

				// field
				var $field = $('<div class="col-12 col-xs-7 col-md-6 order-3 order-xs-2 sim-param-field"></div>').appendTo($formGroup);

				// actions
				var $actions = $('<div class="col-3 col-xs-auto order-2 order-xs-3 sim-param-actions"></div>').appendTo($formGroup);

				// create param metadata action
				if (helperText) {
					// action metadata list
					var $actionMetadata = $('<button class="action action-pure float-right" type="button"><i class="feather icon-info"></i></button>').attr('data-toggle', 'collapse').attr('data-target', '#paramMetadata_' + name).attr('aria-expanded', false).attr('aria-controls', 'paramMetadata_' + name).attr('title', 'Show Metadata').appendTo($actions);
				}

				// create actions            
				var header = $('<div class="card-header"></div>');

				// Create card in group
				var panelDiv = document.createElement("div");
				panelDiv.classList.add("card");
				header.appendTo($(panelDiv));
				panelDiv.appendChild(this.simpleTable.table);

				_$actionTrash = $('<button type="button" class="action action-pure float-right"><i class="feather icon-trash-2"></i></button>').attr('id', 'simActionRemove').attr('data-tooltip', '').attr('title', 'Trash').appendTo(header).on('click', function (event) {
					_this16.simpleTable.trash();
				});

				// remove
				_$actionRemove = $('<button type="button" class="action action-pure float-right"><i class="feather icon-delete"></i></button>').attr('id', 'simActionRemove').attr('data-tooltip', '').attr('title', 'Remove').appendTo(header).on('click', function (event) {
					_this16.simpleTable.remove();
				});

				// add
				_$actionAdd = $('<button type="button" class="action action-pure float-right"><i class="feather icon-plus"></i></button>').attr('id', 'simActionAdd').attr('data-tooltip', '').attr('title', 'Add').appendTo(header).on('click', function (event) {
					_this16.simpleTable.add();
				});

				$(panelDiv).appendTo($field);
				// create validation container
				this.$validationContainer = $('<div class="validation-message mt-1"></div>').appendTo($field);

				// create validation container
				$('<div class="validation-message mt-1"></div>').appendTo($field);

				// create param metadata list
				if (helperText) {
					// metadata table
					var $metadataContainer = $('<div class="collapse param-metadata"></div>').attr('id', 'paramMetadata_' + name).attr('aria-expanded', false).appendTo($field);

					$metadataContainer.append(_createParamMetadataList(helperText));
				}

				this.group = $formGroup;
			}
		}
	}, {
		key: 'clear',
		value: function clear() {
			this.simpleTable.trash();
		}

		/**
   * @return {boolean} If the textarea is valid.
   */

	}, {
		key: 'validate',
		value: function validate() {
			var isValid = true;
			if (this.mandatory) {
				isValid = this.simpleTable.value.length > 0 ? true : false;
			}
			if (!isValid) {
				this.$validationContainer.text('At least one row is required');
				this.group.addClass('has-error');
				this.group.addClass('is-invalid');
				this.$validationContainer.css("display", "block");
			}
			return isValid;
		}
	}, {
		key: 'value',
		get: function get() {
			return this.simpleTable.value;
		},
		set: function set(newValue) {
			var _this17 = this;

			this.simpleTable.trash();
			newValue.forEach(function (item) {
				return _this17.simpleTable._createRow(item);
			});
		}
	}]);

	return ArrayForm;
}();
/*

version: 1.0.0
author: Ahmad Swaid
date: 17.12.2020

*/

var APPMTEditableDetails = function () {
	function APPMTEditableDetails(settings, $container) {
		_classCallCheck(this, APPMTEditableDetails);

		var O = this;
		// defaults maintable simulations modal
		O._$modalContent = $container;
		O._opts = $.extend(true, {}, {
			classes: '',
			data: null,
			on: {
				afterInit: null, // function
				show: function show(O, event) {
					O._updateModal(event);
				}, // function
				hide: null // function
			}
		}, settings);
		O._create();
	}

	_createClass(APPMTEditableDetails, [{
		key: '_create',

		/**
   * CREATE
   * calls super class and sets _metadata
   */

		value: function _create() {
			var O = this;
			_log('MODAL DETAILS / _create', 'primary');

			O._metadata = O.opts.data;
		}
		/**
   * CREATE MODAL
   * creates basic modal components: header and blank body
   */

	}, {
		key: '_createModelMetadataContent',
		value: function _createModelMetadataContent() {
			var O = this;
			_log('MODAL DETAILS / _createModelMetadataContent');
			// modal nav with tabs & search
			O._$modalNav = $('<div class="card-header"></div>').appendTo(O._$modalContent);

			O._navId = O._id + 'Nav';
			if (!O._$navBar) {
				O._$navBar = $('<nav class="navbar navbar-expand-sm row justify-content-start justify-content-md-between"></nav>').appendTo(O._$modalNav);

				// nav toggle
				var $navToggle = $('<button class="action action-pure mt-1 mb-1" type="button" data-toggle="collapse" aria-expanded="false" aria-label="Toggle navigation"><i class="feather icon-list"></i></button>').appendTo(O._$navBar).attr('data-target', '#' + O._navId).attr('aria-controls', O._navId).wrap('<div class="col-auto navbar-toggler order-1 modal-nav-toggler"></div>');

				// divider
				// O._$navBar.append('<div class="col-divider order-2 d-block d-sm-none d-md-block"></div>');

				// nav search
				/*O._$navBar._$search = $('<input class="form-control form-control-plaintext search-input" type="search" placeholder="Search Details" aria-label="Search Details" />')
        .appendTo(O._$navBar)
        .attr('id', O._id + 'NavSearch')
        .wrap('<div class="col col-xxs-auto order-2 modal-nav-search"></div>')
        .wrap('<div class="search"></div>');
    */

				// TO DO
				// search functionality


				// nav tabs
				O._$navBar._$nav = $('<ul class="nav nav-pointer pt-1 pt-md-0"></ul>').appendTo(O._$navBar).wrap('<div class="col-12 col-md-auto order-3 order-md-1 modal-nav-menu order-4"></div>').wrap('<div class="collapse navbar-collapse" id="' + O._navId + '"></div>');
			}

			// modal body
			O._createModalBody();
			O._$modalBody.addClass('p-0 modal-table');

			// content container
			O._$modalTabContent = $('<div class="tab-content h-100"></div>').appendTo(O._$modalBody);
		}

		/**
  * CREATE MODAL
  * creates basic modal components: header and blank body
  */

	}, {
		key: '_createModalBody',
		value: function _createModalBody() {
			var O = this;
			_log('MODAL / _createBody');

			O._$modalBody = $('<div class="modal-body"></div>').appendTo(O._$modalContent);
		}

		/**
  * BUILD PANEL
  * build PANEL content
  * @param {event} event 
  */

	}, {
		key: '_updateContent',
		value: async function _updateContent(_modelMetadata, _modelId) {
			var O = this;
			_log('PANEL MetaData / _updateContent');

			// clear tab-panes
			O._$modalTabContent.html('');

			// get appropiate modelMetadata modelHandler for the model type.
			O._modelHandler = await O._getModelHandler(_modelMetadata);
			// populate nav
			O._populateModalNav(O._modelHandler, O._$navBar._$nav);

			// populate panel
			O._populateModalPanel(O._modelHandler);

			// activate first pane
			O._$navBar._$nav.find('.nav-link').first().addClass('active');
			O._$modalTabContent.find('.tab-pane').first().addClass('active');
		}

		/**
   * POPULATE MODAL MENU
   * @param {object} Model
   */

	}, {
		key: '_populateModalNav',
		value: function _populateModalNav(modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _populateModalNav');
			_log(modelHandler);

			// clear nav
			O._$navBar._$nav.html('');

			// create nav items
			if (modelHandler && modelHandler._menu) {

				$.each(modelHandler._menu, function (i, menuMeta) {

					var $navItem = null;

					if (menuMeta.submenus && menuMeta.submenus.length > 0) {
						$navItem = O._createNavItemDropdown(menuMeta).appendTo(O._$navBar._$nav);
					} else {
						var _$navItem = O._createNavItem(menuMeta).appendTo(O._$navBar._$nav);
					}
				});
			}
			//init collapsable td
			$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
				var target = $(e.target).attr("href"); // activated tab
				var targetTable = $('div' + target + '.tab-pane.h-100.active').find('table');
				//if not initialized yet
				if (targetTable.find('.td-collapse-toggle').length == 0) {
					_appUI._initTdCollapse(targetTable);
				}
			});
		}

		/**
   * POPULATE MODAL PANEL
   * @param {object} Model
   */

	}, {
		key: '_populateModalPanel',
		value: function _populateModalPanel(modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _populateModalPanel');
			_log(modelHandler);

			// create panels
			if (modelHandler && modelHandler._menu && modelHandler.panels) {
				// get each menus id
				$.each(modelHandler._menu, function (i, menuMeta) {
					// dropdown nav item 
					if (menuMeta.submenus && menuMeta.submenus.length > 0) {
						// iterate over submenus
						$.each(menuMeta.submenus, function (j, submenuMeta) {
							// panel meta data exists in handler
							console.log(submenuMeta.id);
							if (submenuMeta.id in modelHandler.panels) {
								console.log(modelHandler.panels[submenuMeta.id]);
								O._preparePanel(submenuMeta, modelHandler, $(modelHandler.panels[submenuMeta.id].panel)).appendTo(O._$modalTabContent);
							}
						});
					}
					// single nav item ? create panel
					else {
							if (menuMeta.id) {
								console.log(menuMeta.id);
								if (menuMeta.id in modelHandler.panels) {
									console.log(modelHandler.panels[menuMeta.id]);
									//$(modelHandler.panels[menuMeta.id].panel).appendTo(O._$modalTabContent);
									O._preparePanel(submenuMeta, modelHandler, $(modelHandler.panels[menuMeta.id].panel)).appendTo(O._$modalTabContent);
									//O._createPanel(menuMeta, modelHandler)
									//    .appendTo(O._$modalTabContent);
								}
							}
						}
				});
			}
		}

		/**
   * CREATE NAV ITEM DROPDOWN
   * @param {array} menuMeta: array of dropdown-items width 'id' and 'label'
   */

	}, {
		key: '_createNavItemDropdown',
		value: function _createNavItemDropdown(menuMeta) {
			var O = this;
			_log('MODAL DETAILS / _createTabNavItemDropdown: ' + menuMeta.label);

			var $navItem = $('<li class="nav-item dropdown"></li>');

			var $navLink = $('<a class="nav-link dropdown-toggle" role="button">' + menuMeta.label + '</a>').attr('href', '#').attr('aria-haspopup', true).attr('aria-expanded', false).attr('data-toggle', 'dropdown').appendTo($navItem);
			var $dropdown = $('<div class="dropdown-menu"></div>').appendTo($navItem);

			$.each(menuMeta.submenus, function (i, submenuMeta) {

				var $dropdownItem = $('<a class="dropdown-item" role="button">' + submenuMeta.label + '</a>').attr('href', '#' + submenuMeta.id).attr('aria-controls', '#' + submenuMeta.id).attr('data-toggle', 'tab').appendTo($dropdown);
			});

			return $navItem;
		}

		/**
   * CREATE NAV ITEM
   * @param {array} menuMeta
   */

	}, {
		key: '_createNavItem',
		value: function _createNavItem(menuMeta) {
			var O = this;
			_log('MODAL DETAILS / _createNavItem: ' + menuMeta.label);

			var $navItem = $('<li class="nav-item"></li>');
			var $navLink = $('<a class="nav-link" role="button">' + menuMeta.label + '</a>').attr('href', '#' + menuMeta.id).attr('aria-controls', '#' + menuMeta.id).attr('data-toggle', 'tab').appendTo($navItem);

			return $navItem;
		}

		/**
   * CREATE PANEL
   * create tab-pane for specific menu by selecting type and calling specific creation (simple, complex, plot)
   * @param {array} menu
   * @param {object} modelHandler: object of type Model
   */

	}, {
		key: '_preparePanel',
		value: function _preparePanel(menu, modelHandler, handlerPanel) {
			var O = this;
			_log('MODAL DETAILS / _createPanel: ' + menu.id);

			var $panel = null;
			if (modelHandler && menu.id) {
				$panel = O._createPanelPan(menu, modelHandler, handlerPanel);
			}
			return $panel;
		}

		/**
   * CREATE PLOT PANEL
   * create plot tab-pane for specific menu
   * @param {array} menu
   * @param {object} modelHandler: object of class Model
   */

	}, {
		key: '_createPanelPan',
		value: function _createPanelPan(menu, modelHandler, handlerPanel) {
			var O = this;
			_log('MODAL DETAILS / _createPlotPanel');

			// tab-pane
			var $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>').attr('id', menu.id);

			if (modelHandler && menu.id) {
				// get panel meta
				var panelMeta = modelHandler._panels[menu.id];

				// title
				$panel.append('<div class="panel-heading">' + menu.label + '</div>');
				handlerPanel.appendTo($panel);
			}

			return $panel;
		}

		/**
   * GET MODEL HANDLER
   * returns model handler of class Model
   * @param {array} modelMetadata: metadata for specific id
   */

	}, {
		key: '_getModelHandler',
		value: async function _getModelHandler(modelMetadata) {
			var O = this;
			_log('MODAL DETAILS / _getModelHandler');
			console.log(modelMetadata);

			var modelHandler = null;

			if (modelMetadata) {

				// get plot image
				var imgUrl = void 0;
				// get appropiate modelMetadata modelHandler for the model type.

				if (modelMetadata.modelType === 'genericModel') {
					modelHandler = new GenericModel(modelMetadata, imgUrl, true);
				} else if (modelMetadata.modelType === 'dataModel') {
					modelHandler = new DataModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'predictiveModel') {
					modelHandler = new PredictiveModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'otherModel') {
					modelHandler = new OtherModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'toxicologicalModel') {
					modelHandler = new ToxicologicalModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'doseResponseModel') {
					modelHandler = new DoseResponseModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'exposureModel') {
					modelHandler = new ExposureModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'processModel') {
					modelHandler = new ProcessModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'consumptionModel') {
					modelHandler = new ConsumptionModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'healthModel') {
					modelHandler = new HealthModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'riskModel') {
					modelHandler = new RiskModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'qraModel') {
					modelHandler = new QraModel(modelMetadata, imgUrl);
				} else {
					modelHandler = new GenericModel(modelMetadata, imgUrl, true);
				}
			}

			return modelHandler;
		}
	}, {
		key: 'opts',
		get: function get() {
			return this._opts;
		},
		set: function set(settings) {
			this._opts = $.extend(true, {}, this.opts, settings);
		}
	}]);

	return APPMTEditableDetails;
}();

/**
 * Create a Bootstrap 3 modal dialog.
 */


var Dialog = function () {

	/**
  * Create a Bootstrap 3 modal dialog.
  * 
  * ```
  * <div class="modal-fade">
  *   <div class="modal-dialog" role="document">
  *     <div class="modal-content">
  *       <div class="modal-header">
  *         <button>
  *           <span>
  *         </button>
  *         <h4 class="modal-title">title</h4>
  *       </div>
  *       <div class="modal-body">
  *         <form>...</form>
  *       </div>
  *       <div class="modal-footer">
  *         <button type="button">Close</button>
  *         <button type="button">Save changes</button>
  *       </div>
  *     </div>
  *   </div>
  * </div>
  * ```
  * 
  * @param {id} id Dialog id
  * @param {title} title Dialog title
  * @param {formData} formData Object with form data
  */
	function Dialog(id, title, formData, port) {
		_classCallCheck(this, Dialog);

		this.inputs = {}; // Hash of inputs by id

		// Index of the row currently edited. It is -1 if no row is being edited.
		// This is the case of when a new row is added.
		this.editedRow = -1;

		this.modal = document.createElement("div");
		this.create(id, title, formData, port);
	}

	_createClass(Dialog, [{
		key: 'create',
		value: function create(id, title, formData, port) {
			var _this18 = this;

			// modal body
			var form = $('<form class="form-striped"></form>');
			formData.forEach(function (prop) {
				var inputForm = createForm(prop, null, port);
				if (inputForm) {
					$(inputForm.group).appendTo(form);
					_this18.inputs[prop.id] = inputForm;
				}
			});

			var modalBody = $('<div class="modal-body p-0 sim-params"></div>');
			var modalinnerBody = $('<div class="tab-content h-100"></div>');
			form.appendTo(modalinnerBody);
			modalinnerBody.appendTo(modalBody);

			// modal action
			// nav
			_$modalNav = $('<div class="modal-body sim-select"></div>');

			// navbar
			_$navBar = $('<nav class="navbar sim-select">').appendTo(_$modalNav).wrap('<form></form>');

			//  select label
			_$simSelectLabel = $('<label class="col-4 col-md-3 sim-select-label" >' + title.replace("Add", "") + '</label>').appendTo(_$navBar);

			//  select actions
			_$dialogActions = $('<div class="col-8"></div>').appendTo(_$navBar);

			var $actionGroup1 = $('<div class="col-12"></div>').appendTo(_$dialogActions);

			closeButton = $('<button type="button" class="btn btn-icon btn-outline-light"><i class="feather icon-x"></i></button>').attr('id', 'simActionclose').attr('data-tooltip', '').attr('title', 'close').attr('data-dismiss', 'modal').appendTo($actionGroup1);
			// col divider
			$('<div class="col-divider ml-auto ml-xs-0"></div>').appendTo($actionGroup1);
			saveButton = $('<button type="button" class="btn btn-icon btn-outline-light"><i class="feather icon-save"></i></button>').attr('id', 'save').attr('data-tooltip', '').attr('title', 'Save changes').appendTo($actionGroup1).on('click', function (event) {
				// Validate inputs and stop saving if errors are found.
				var hasError = false;
				Object.values(_this18.inputs).forEach(function (input) {
					if (!input.validate()) hasError = true;
				});
				if (hasError) return;

				$(_this18.modal).modal('hide');

				// Retrieve data and clear inputs
				var data = {};
				for (var inputId in _this18.inputs) {
					var currentInput = _this18.inputs[inputId];
					data[inputId] = currentInput.value; // Save input value
					currentInput.clear(); // Clear input
				}

				if (_this18.editedRow != -1) {
					_this18.panel.save(_this18.editedRow, data);
					_this18.editedRow = -1;
					Object.values(_this18.inputs).forEach(function (input) {
						return input.clear();
					}); // Clear inputs
				} else {
					_this18.panel.add(data);
				}
			});
			$actionGroup1.wrapInner('<div class="row justify-content-end align-items-center"></div>');

			var content = document.createElement("div");
			content.classList.add("modal-content");
			content.innerHTML = '<div class="modal-header">\n                                <h1 class="modal-title">' + title + '</h1>\n                                <button type="button" class="action action-pure action-lg ml-2" data-dismiss="modal" aria-label="Close"><i class="feather icon-x"></i></button>\n                                </div>';

			_$navBar.appendTo($(content));
			modalBody.appendTo($(content));

			var modalDialog = document.createElement("div");
			modalDialog.classList.add("modal-dialog", "modal-xl");
			modalDialog.setAttribute("role", "document");
			modalDialog.appendChild(content);

			this.modal.classList.add("modal", "fade", "modal-sim");
			this.modal.id = id;
			this.modal.tabIndex = -1;
			this.modal.setAttribute("role", "dialog");
			this.modal.appendChild(modalDialog);
		}
	}]);

	return Dialog;
}();
/**
 * Simple panel for non nested data like General information, study, etc.
 */


var FormPanel = function () {
	function FormPanel(title, formData, data, port) {
		_classCallCheck(this, FormPanel);

		_log('FormPanel /' + title, 'primary');
		this.panel = $('<div class="panel-body"></div>');
		this.inputs = {};

		this._create(title, formData, data, port);
	}

	/**
  * ```
  * <div class="panel panel-default">
  *   <div class="panel-heading">
  *     <h3 class="panel-title">Some title</h3>
  *   </div>
  *   <div class="panel-body">
  *     <form></form>
  *   </div>
  * </div>
  * ```
  * @param {*} title 
  * @param {*} formData 
  */


	_createClass(FormPanel, [{
		key: '_create',
		value: function _create(title, formData, data, port) {
			var _this19 = this;

			var form = $('<form class="form-striped"></form>');
			formData.forEach(function (prop) {
				var inputForm = createForm(prop, data ? data[prop.id] : null, port);
				if (inputForm) {
					$(inputForm.group).appendTo(form);
					_this19.inputs[prop.id] = inputForm;
				}
			});
			form.appendTo(this.panel);
		}
	}, {
		key: 'validate',
		value: function validate() {
			var isValid = true;
			Object.values(this.inputs).forEach(function (input) {
				if (!input.validate()) isValid = false;
			});
			return isValid;
		}
	}, {
		key: 'data',
		get: function get() {
			var data = {};
			Object.entries(this.inputs).forEach(function (_ref) {
				var _ref2 = _slicedToArray(_ref, 2),
				    id = _ref2[0],
				    input = _ref2[1];

				return data[id] = input.value;
			});
			return data;
		}
	}]);

	return FormPanel;
}();
/**
 * Bootstrap 3 form-group for an input.
 */


var InputForm = function () {

	/**
  * Create a Bootstrap 3 form-group.
  * 
  * ```
  * <div class="form-group row">
  *   <label>name</label>
  *   <div class="col-sm-10">
  *     <input type="text">
  *   </div>
  * </div>`;
  * ```
  * 
  * If type === checkbox
  * ```
  * <div class="form-group row">
  *   <label >name</label>
  *   <div class="col-sm-10">
  *     <input class="form-check-input" type="checkbox" checked="">
        *	 </div>
    * </div>
  * ```
  * 
  * @param {string} name Property name
  * @param {boolean} mandatory `true` if mandatory, `false` if optional.
  * @param {string} type Property type: text, url, checkbox, etc.
  * @param {string} helperText Tooltip
  * @param {string} value Initial value of the property.
  * @param {Array} vocabulary Vocabulary name.
  */
	function InputForm(name, mandatory, type, helperText, value, port) {
		var vocabulary = arguments.length > 6 && arguments[6] !== undefined ? arguments[6] : null;
		var sid = arguments[7];

		_classCallCheck(this, InputForm);

		this.name = name;
		this.mandatory = mandatory;
		this.type = type;
		this.helperText = helperText;
		this.isSID = !_isNull(sid) && !_isUndefined(sid);
		this.group = null;
		this._create(name, mandatory, type, helperText, value, vocabulary, port);
	}

	/**
  * @param {string} name Property name
  * @param {boolean} mandatory `true` if mandatory, `false` if optional.
  * @param {string} type Property type: text, url, checkbox, etc.
  * @param {string} helperText Tooltip
  * @param {string} value Initial value of the property.
  * @param {Array} vocabulary Vocabulary name.
  */


	_createClass(InputForm, [{
		key: '_create',
		value: function _create(name, mandatory, type, helperText, value, vocabulary, port) {
			this._createFormField(name, mandatory, type, helperText, value, vocabulary, port);

			// Create input
			/*this.input.className = type === "checkbox" ? "form-check-input" : "form-control";
   this.input.type = type;
    if (type === "date" && typeof (value) != "string") {
       let day = ("" + value[2]).length > 1 ? ("" + value[2]) : ("0" + value[2]);
       let month = ("" + value[1]).length > 1 ? ("" + value[1]) : ("0" + value[1]);
       this.input.value = value[0] + "-" + month + "-" + day;
   } else {
       this.input.value = value;
   }
    this.input.title = helperText;
    // Create div for input
   let inputDiv = document.createElement("div");
   inputDiv.classList.add("col-sm-10");
   inputDiv.appendChild(this.input);
   if (mandatory) {
       this.helpBlock = document.createElement("span");
       this.helpBlock.className = "help-block";
       this.helpBlock.style.display = "none";
       this.helpBlock.textContent = `${name} is a required property`;
       inputDiv.appendChild(this.helpBlock);
   }
   
   // Add autocomplete to input with vocabulary
   if (vocabulary) {
       addControlledVocabulary(this.input, vocabulary, port);
   }
    // Collect everything into group
   this.group.classList.add("form-group", "row");
   this.group.appendChild(createLabel(name, mandatory, helperText));
   this.group.appendChild(inputDiv);*/
		}
		/**
   * CREATE FORM FIELD
   * create field as form group
   * @param {array} param
   */

	}, {
		key: '_createFormField',
		value: function _createFormField(name, mandatory, type, helperText, value, vocabulary, port) {
			var _this20 = this;

			var O = this;
			_log('PANEL SIM / _createFormField');
			_log(name);

			if (name) {

				// formgroup
				var _$formGroup = $('<div class="form-group row"></div>');
				// .appendTo( O._$simForm );

				// label
				$label = $('<label class="col-form-label col-form-label-sm col-9 col-xs-3 order-1 sim-param-label"></label>').attr('for', 'input_' + name).appendTo(_$formGroup);
				$label.text(name + (mandatory ? "*" : ""));

				// field
				var $field = $('<div class="col-12 col-xs-7 col-md-6 order-3 order-xs-2 sim-param-field"></div>').appendTo(_$formGroup);

				// actions
				var $actions = $('<div class="col-3 col-xs-auto order-2 order-xs-3 sim-param-actions"></div>').appendTo(_$formGroup);

				// input item
				this.input = null;

				// create param metadata action
				if (helperText) {
					// action metadata list
					var $actionMetadata = $('<button class="action action-pure float-right" type="button"><i class="feather icon-info"></i></button>').attr('data-toggle', 'collapse').attr('data-target', '#metadata_' + name).attr('aria-expanded', false).attr('aria-controls', 'metadata_' + name).attr('title', 'Show Metadata').appendTo($actions);
				}

				if (type) {

					// numeric
					if (type == 'number') {

						var $inputGroup = $('<div class="input-group input-group-sm"></div>').appendTo($field);

						this.input = $('<input type="text" />').attr('id', 'input_' + name)
						//.data( 'param-input', param ) 
						.attr('aria-invalid', false).appendTo($inputGroup);

						// touchspin
						this.input.addClass('form-control form-control-sm').attr('data-touchspin', '');
						// add unit postfix to touchspin

						this.input.attr('data-touchspin-postfix', type);
					}
					// string or others
					//<input class="custom-control-input" type="checkbox" id="switchExample1" name="switchExample1" checked />
					else if (type == 'boolean') {
							this.input = $('<input type="checkbox" class="form-control form-control-sm" />').attr('id', 'input_' + name).appendTo($field);
						}
						// string or others
						else {
								this.input = $('<input type="text" class="form-control form-control-sm" />').attr('id', 'input_' + name).appendTo($field);
							}
				}
				if (type === "date" && typeof value != "string") {
					var day = ("" + value[2]).length > 1 ? "" + value[2] : "0" + value[2];
					var month = ("" + value[1]).length > 1 ? "" + value[1] : "0" + value[1];
					this.input.val(value[0] + "-" + month + "-" + day);
				} else {
					this.input.val(value);
				}
				this.input.on("blur", function () {
					O.validate(_this20.value);
				});
				// create validation container
				this.input.$validationContainer = $('<div class="validation-message mt-1"></div>').appendTo($field);

				// create  metadata list
				if (helperText) {
					// metadata table
					var $metadataContainer = $('<div class="collapse param-metadata"></div>').attr('id', 'metadata_' + name).attr('aria-expanded', false).appendTo($field);

					$metadataContainer.append(_createParamMetadataList(helperText));
				}

				this.group = _$formGroup;
			}
		}
	}, {
		key: 'clear',
		value: function clear() {
			this.input.val("");

			if (this.input.$validationContainer) {
				this.input.$validationContainer.css("display", "none");
			}

			// Remove validation classes
			this.group.removeClass("has-success has-error");
		}

		/**
   * @returns {boolean} If the input is valid.
   */

	}, {
		key: 'validate',
		value: function validate() {
			var O = this;
			_log('PANEL SIM / _validateSimForm');

			var validationErrors = [];
			// remove error classes
			this.input.find('.has-error').removeClass('has-error');
			this.input.find('.is-invalid').removeClass('is-invalid');
			this.input.find('.validation-message').empty();

			var isValid = true;
			if (!this.mandatory) {
				isValid = true;
			} else if (this.isSID) {
				var fieldValue = this.input.val();
				var idRegexp = /^[A-Za-z_^s]\w*$/;
				// name fits regexp
				if (!idRegexp.test(fieldValue)) {
					this.input.$validationContainer.text('Parameter ID is not a valid (SId)');
					isValid = false;
				}
			} else {
				isValid = this.input.val() ? true : false;
				if (!isValid) this.input.$validationContainer.text("required");
				// if email input is empty, reset the default error message
				if (!isValid && this.input.type === "email") {
					this.input.$validationContainer.text("Email is a required property");
				}
				// check if mail has correct structure
				if (isValid && this.input.type === "email") {

					var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\ ".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
					isValid = re.test(this.input.value);
					this.input.$validationContainer.text("required");
				}
			}

			if (!isValid) {
				this.input.parents('.form-group').addClass('has-error');
				this.input.$validationContainer.addClass('is-invalid');
				this.input.$validationContainer.css("display", "block");
			}

			return isValid;
		}
	}, {
		key: 'value',
		get: function get() {
			console.log(this.input.val());
			return this.type !== "checkbox" ? this.input.val() : this.input.checked;
		},
		set: function set(newValue) {
			this.input.val(newValue);
		}
	}]);

	return InputForm;
}();
/**
 * Bootstrap 3 form with a select.
 */


var SelectForm = function () {

	/**
  * Create a Bootstrap 3 form-group with a select.
  * 
  * ```
  * <div class="form-group row">
  *   <label>name</label>
  *   <select class="form-control">
  *     <option>1</option>
  *     <option>2</option>
  *   </select>
  * </div>```
  * <select id="select2ExampleS2" class="form-control form-control-sm" style="width: 100%;" data-sel2 data-placeholder="Select…">
 *									<option value="1">Option 1</option>
 *									<option value="2">Option 2</option>
 *									<option value="3">Option 3 with very long title lorem ipsum dolor sit amet</option>
 *								</select>
  */
	function SelectForm(name, mandatory, helperText, value, port) {
		var vocabulary = arguments.length > 5 && arguments[5] !== undefined ? arguments[5] : null;

		_classCallCheck(this, SelectForm);

		this.group = document.createElement("div");

		this._create(name, mandatory, helperText, value, vocabulary, port);
	}

	_createClass(SelectForm, [{
		key: '_create',
		value: function _create(name, mandatory, helperText, value, vocabulary, port) {
			var _this21 = this;

			var O = this;
			// formgroup
			var $formGroup = $('<div class="form-group row"></div>');

			// label
			var $label = $('<label class="col-form-label col-form-label-sm col-9 col-xs-3 order-1 sim-param-label"></label>').attr('for', 'selectInput_' + name).appendTo($formGroup);
			$label.text(name + (mandatory ? "*" : ""));

			// field
			var $field = $('<div class="col-12 col-xs-7 col-md-6 order-3 order-xs-2 "></div>').appendTo($formGroup);

			// actions
			var $actions = $('<div class="col-3 col-xs-auto order-2 order-xs-3 sim-param-actions"></div>').appendTo($formGroup);

			// input item
			this.input = null;

			// create param metadata action
			if (helperText) {
				// action metadata list
				var $actionMetadata = $('<button class="action action-pure float-right" type="button"><i class="feather icon-info"></i></button>').attr('data-toggle', 'collapse').attr('data-target', '#paramMetadata_' + name).attr('aria-expanded', false).attr('aria-controls', 'paramMetadata_' + name).attr('title', 'Show Metadata').appendTo($actions);
			}

			this.input = $('<select class="form-control form-control-sm" style="width: 100%;" data-sel2 data-placeholder="Select…"/>').attr('id', 'selectInput_' + name).appendTo($field);
			this.input.val(value);
			// Add options from vocabulary. The option matching value is selected.
			if (port >= 0) {
				fetch('http://localhost:' + port + '/getAllNames/' + vocabulary).then(function (response) {
					return response.json();
				}).then(function (data) {
					//console.log('data',data);
					_this21.input.append(data.map(function (item) {
						return '<option>' + item + '</option>';
					}).join(""));
					//console.log($(this.select));
				});
			}

			// create validation container
			this.input.$validationContainer = $('<div class="validation-message mt-1"></div>').appendTo($field);

			// create param metadata list
			if (helperText) {
				// metadata table
				var $metadataContainer = $('<div class="collapse param-metadata"></div>').attr('id', 'paramMetadata_' + name).attr('aria-expanded', false).appendTo($field);

				$metadataContainer.append(_createParamMetadataList(helperText));
			}
			this.group = $formGroup;
		}
	}, {
		key: 'clear',
		value: function clear() {
			this.input.val('');
		}

		/**
   * @returns {boolean} If the input is valid.
   */

	}, {
		key: 'validate',
		value: function validate() {

			var isValid = void 0;
			this.input.find('.has-error').removeClass('has-error');
			this.input.find('.is-invalid').removeClass('is-invalid');
			this.input.find('.validation-message').empty();
			if (!this.mandatory) {
				isValid = true;
			} else {
				isValid = this.input.value ? true : false;
			}

			if (!isValid) {
				this.input.$validationContainer.text('required');
				this.input.parents('.form-group').addClass('has-error');
				this.input.addClass('is-invalid');
				this.input.$validationContainer.css("display", "block");
			}
			return isValid;
		}
	}, {
		key: 'value',
		get: function get() {
			return this.input.val();
		},
		set: function set(newValue) {
			this.select.val(newValue);
		}
	}]);

	return SelectForm;
}();

var SimpleTable = function () {
	function SimpleTable(type, data, vocabulary, port) {
		var _this22 = this;

		_classCallCheck(this, SimpleTable);

		this.type = type === "text-array" ? "text" : "date";
		this.vocabulary = vocabulary;
		this.port = port;

		this.table = document.createElement("table");
		this.table.className = "table";
		this.table.innerHTML = '<thead><thead>';

		this.body = document.createElement("tbody");
		this.table.appendChild(this.body);

		data.forEach(function (value) {
			return _this22._createRow(value);
		});
	}

	/**
  * Create new row to enter data if the last row value is not empty.
  */


	_createClass(SimpleTable, [{
		key: 'add',
		value: function add() {
			// If it has no rows or the last row value is not empty
			if (!this.body.lastChild || this.body.lastChild.lastChild.firstChild.value) {
				this._createRow();
			}
		}
	}, {
		key: 'remove',
		value: function remove() {
			var _this23 = this;

			// Find checked rows and delete them
			Array.from(this.body.children).forEach(function (row) {
				// Get checkbox (tr > td > input)
				var checkbox = row.firstChild.firstChild;
				if (checkbox.checked) {
					_this23.body.removeChild(row);
				}
			});
		}

		/**
   * Remove every row in the table
   */

	}, {
		key: 'trash',
		value: function trash() {
			this.body.innerHTML = "";
		}
	}, {
		key: '_createRow',
		value: function _createRow() {
			var _this24 = this;

			var value = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : "";

			var input = document.createElement("input");
			input.type = this.type;
			input.className = "form-control";
			input.value = value;

			// Add autocomplete to input with vocabulary
			if (this.vocabulary) {
				addControlledVocabulary(input, this.vocabulary, this.port);
			}

			// If enter is pressed when the input if focused, lose focus and add a
			// new row (like clicking the add button). The new input from calling add
			// is focused.
			input.addEventListener("keyup", function (event) {
				if (event.key === "Enter") {
					input.blur();
					_this24.add();
				}
			});

			// Create cell with input
			var inputCell = document.createElement("td");
			inputCell.appendChild(input);

			// Create row with checkbox and input
			var newRow = document.createElement("tr");
			newRow.innerHTML = '<td><input type="checkbox"></td>';
			newRow.appendChild(inputCell);

			// Add row
			this.body.appendChild(newRow);

			input.focus(); // Focus the new input      
		}
	}, {
		key: 'value',
		get: function get() {
			var data = [];
			this.body.childNodes.forEach(function (tr) {
				var inputCell = tr.lastChild; // 2nd cell (with input)
				var input = inputCell.firstChild; // <input>
				data.push(input.value);
			});

			return data;
		}
	}]);

	return SimpleTable;
}();
/**
 * Create a Bootstrap 3 panel with controls in the heading and a table as body.
 * 
 * ```
 * <div class="panel panel-default">
 *   <div class="panel-heading clearfix">
 *     <h4 class="panel-title pull-left" style="padding-top:7.5px;">${title}</h4>
 *     <div class="input-group">
 *       <p class="pull-right" /> <!-- gutter -->
 *       <div class="input-group-btn">
 *         <button type="button" class="btn btn-default" data-toggle="modal" data-target="#${dialog}">
 *           <i class="glyphicon glyphicon-plus"></i>
 *         </button>
 *         <button class="btn btn-default"><i class="glyphicon glyphicon-remove"></i></button>
 *         <button class="btn btn-default"><i class="glyphicon glyphicon-trash"></i></button>
 *       </div>
 *     </div>
 *   </div>
 *   <table class="table">
 *     <tr>
 *       <th><input type="checkbox"></th>
 *     </tr>
 *   </table>
 * </div>`
 * ```
 */


var TablePanel = function () {

	/**
  * Create a TablePanel.
  * 
  * @param {string} title Panel title.
  * @param {object} formData Related data from the UI schema.
  * @param {object} data Initial data of the table.
  */
	function TablePanel(title, formData, data, port) {
		_classCallCheck(this, TablePanel);

		this.panel = document.createElement("div");

		// Register this panel in dialog (TODO: this should be done in Dialog's constr)
		// this.dialog = dialog;
		this.dialog = new Dialog(title + "Dialog", "Add " + title, formData, port);
		this.dialog.panel = this;
		this.tablePanel = this._createComplexPanel(data, formData, title, this.dialog);
		this.table = this.tablePanel.find("table.table-striped");
		this.data = data ? data : []; // Initialize null or undefined data
		this._create(title, this.dialog, formData);
	}

	/**
  * Create UI of the TablePanel.
  * 
  * @param {string} title Panel title.  
  * @param {Dialog} dialog Reference to Dialog object. This Dialog is later
  *   used for adding new entries and editing existing ones. 
  * @param {object} formData Related data from the UI schema.
  */


	_createClass(TablePanel, [{
		key: '_create',
		value: function _create(title, dialog, formData) {
			// panel
			this.panel.classList.add("panel", "panel-default");
			this.tablePanel.appendTo($(this.panel));
		}
		/**
   * CREATE COMPLEX PANEL
   * create complex tab-pane for specific menu
   * table has in metadata and schema defined cols
   * @param {array} menu
   * @param {object} modelHandler: object of class Model
   */

	}, {
		key: '_createComplexPanel',
		value: function _createComplexPanel(data, formData, title, dialog) {
			var _this25 = this;

			var O = this;

			// tab-pane
			var $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>').attr('id', 'table' + title);
			// Add button
			var addButton = $('<button class="btn btn-outline-secondary btn-sm btn-icon" type="button"><i class="feather icon-plus"></i></button>').attr('aria-label', "Add a " + title).attr('title', "Add a " + title);
			addButton.on('click', function (event) {
				Object.values(dialog.inputs).forEach(function (input) {
					return input.clear();
				});
				$(dialog.modal).modal('show');
			});

			var removeAllButton = $('<button class="btn btn-outline-secondary btn-sm btn-icon" type="button"><i class="feather icon-trash"></i></button>').attr('aria-label', 'Remove all ' + title + '(s)').attr('title', 'Remove all ' + title + '(s)');
			removeAllButton.on('click', function (event) {
				_this25.removeAll();
			});

			// table settings
			var tableSettings = {
				cols: [],
				tableData: [],
				responsive: true,
				showToggle: true,
				rowActions: [{
					type: 'link',
					idPrefix: 'mtActionMerge_',
					icon: 'icon-arrow-up',
					title: 'Move Up',
					on: {
						click: function click(O, $action, rowIndex, rowData) {
							_log('on > clicktrash', 'hook'); // example hook output
							_log(O);
							_log($action);
							_log(rowIndex);
							_log(rowData);
							_this25.moveTo(rowIndex, 'up');
						}
					}
				}, {
					type: 'link',
					idPrefix: 'mtActionMerge_',
					icon: 'icon-arrow-down',
					title: 'Move down',
					on: {
						click: function click(O, $action, rowIndex, rowData) {
							_log('on > clicktrash', 'hook'); // example hook output
							_log(O);
							_log($action);
							_log(rowIndex);
							_log(rowData);
							_this25.moveTo(rowIndex, 'down');
						}
					}
				}, {
					type: 'link',
					idPrefix: 'mtActionMerge_',
					icon: 'icon-trash',
					title: 'Trash',
					on: {
						click: function click(O, $action, rowIndex, rowData) {
							_log('on > clicktrash', 'hook'); // example hook output
							_log(O);
							_log($action);
							_log(rowIndex);
							_log(rowData);
							_this25.remove(rowIndex);
						}
					}
				}, {
					type: 'link',
					idPrefix: 'mtActionEdit_',
					icon: 'icon-edit-2',
					title: 'Edit',
					on: {
						click: function click(O, $action, rowIndex, rowData) {
							_log('on > clickEdit', 'hook'); // example hook output
							_log(O);
							_log($action);
							_log(rowIndex);
							_log(rowData);
							_this25.edit(rowIndex, rowData, dialog);
						}
					}
				}],
				editableToolbarbuttons: [addButton, removeAllButton]
			};

			// set table cols
			$.each(formData, function (i, prop) {
				tableSettings.cols.push({
					label: prop.label,
					field: prop.id,
					sortable: true,
					switchable: true
				});
			});

			// set table row data
			$.each(data, function (i, item) {
				// row each item
				var rowData = {
					cells: []
				};
				// cells
				$.each(formData, function (j, prop) {
					var data = item[prop.id];
					data = _checkUndefinedContent(data);
					// cell each prop
					rowData.cells.push(data);
				});

				tableSettings.tableData.push(rowData);
			});
			// create table
			this.panelTable = new APPTable(tableSettings, $panel);
			$panel.data('table', this.panelTable);
			return $panel;
		}
	}, {
		key: 'add',
		value: function add(data) {
			console.log('save', this.panelTable._tableData, data);
			this.panelTable._tableData.push(data); // add data
			this.data.push(data); // add data
			this.panelTable.addRow(this.panelTable._tableData.length - 1, data);
		}
	}, {
		key: 'edit',
		value: function edit(index, originalData, dialog) {
			var keys = [];
			$.each(this.panelTable.opts.cols, function (index, key) {
				keys.push(key.field);
			});
			for (indexx in keys) {
				dialog.inputs[keys[indexx]].input.val(originalData.cells[indexx]);
			}

			dialog.editedRow = index;
			$(dialog.modal).modal('show');
		}
	}, {
		key: 'save',
		value: function save(index, originalData) {
			this.data.splice(index, 1);
			this.panelTable._tableData.splice(index, 1);
			var row = $(this.panelTable._$tbody).find('tr').eq(index);
			row.find('td').each(function () {
				$(this).html(originalData[$(this).attr('data-id')]);
			});
			this.data.push(originalData); // add data
			this.panelTable._tableData.push(originalData);
		}
	}, {
		key: 'remove',
		value: function remove(index) {

			$(this.panelTable._$tbody).find('tr').eq(index).remove();;

			this.data.splice(index, 1);
			this.panelTable._tableData.splice(index, 1);

			$.each($(this.panelTable._$tbody).find('tr'), function (rowindex, row) {
				$(row).attr('data-row-id', rowindex);
			});
			console.log(index);
		}
	}, {
		key: 'moveTo',
		value: function moveTo(index, command) {
			var row = $(this.panelTable._$tbody).find('tr').eq(index);
			if (command === 'up') {
				row.insertBefore(row.prev());
			} else if (command === 'down') {
				row.insertAfter(row.next());
			}
			$.each($(this.panelTable._$tbody).find('tr'), function (rowindex, row) {
				$(row).attr('data-row-id', rowindex);
			});
		}
	}, {
		key: 'removeAll',
		value: function removeAll() {
			this.data = []; // Clear data
			this.panelTable._tableData = []; // Clear data
			this.panelTable._clear(); // Empty table
		}
	}]);

	return TablePanel;
}();
/**
 * Create a Bootstrap 3 form-group for a textarea. 
 */


var TextareaForm = function () {

	/**
  * Create a Bootstrap 3 form-group.
  * 
  * ```
  * <div class="form-group row">
  *   <label>name</label>
  *   <textarea class="form-control" rows="3"></textarea>
  * </div>
  * ```
  */

	function TextareaForm(name, mandatory, helperText, value) {
		_classCallCheck(this, TextareaForm);

		this.name = name;
		this.mandatory = mandatory;
		this.helperText = helperText;

		this.textarea = $('<textarea row="6" class="form-control form-control-sm" />').attr('id', 'area_' + name);
		this._create(name, mandatory, helperText, value);
	}

	/**
  * @param {string} name Property name
  * @param {boolean} mandatory `true` if mandatory, `false` if optional.
  * @param {string} helperText Tooltip
  * @param {string} value Initial value of the property.
  */


	_createClass(TextareaForm, [{
		key: '_create',
		value: function _create(name, mandatory, helperText, value) {
			var O = this;
			// formgroup
			var $formGroup = $('<div class="form-group row"></div>');

			// label
			var $label = $('<label class="col-form-label col-form-label-sm col-9 col-xs-3 order-1 sim-param-label"></label>').attr('for', 'areaInput_' + name).appendTo($formGroup);
			$label.text(name + (mandatory ? "*" : ""));

			// field
			var $field = $('<div class="col-12 col-xs-7 col-md-6 order-3 order-xs-2 "></div>').appendTo($formGroup);

			// actions
			var $actions = $('<div class="col-3 col-xs-auto order-2 order-xs-3 sim-param-actions"></div>').appendTo($formGroup);

			// input item
			this.input = null;

			// create param metadata action
			if (helperText) {
				// action metadata list
				var $actionMetadata = $('<button class="action action-pure float-right" type="button"><i class="feather icon-info"></i></button>').attr('data-toggle', 'collapse').attr('data-target', '#paramMetadata_' + name).attr('aria-expanded', false).attr('aria-controls', 'paramMetadata_' + name).attr('title', 'Show Metadata').appendTo($actions);
			}

			this.input = $('<textarea type="text" row="6" class="form-control" />').attr('id', 'areaInput_' + name).appendTo($field);
			this.input.val(value);

			// create validation container
			this.input.$validationContainer = $('<div class="validation-message mt-1"></div>').appendTo($field);

			// create param metadata list
			if (helperText) {
				// metadata table
				var $metadataContainer = $('<div class="collapse param-metadata"></div>').attr('id', 'paramMetadata_' + name).attr('aria-expanded', false).appendTo($field);

				$metadataContainer.append(_createParamMetadataList(helperText));
			}
			this.group = $formGroup;
		}
	}, {
		key: 'clear',
		value: function clear() {
			this.input.val("");
		}

		/**
   * @return {boolean} If the textarea is valid.
   */

	}, {
		key: 'validate',
		value: function validate() {
			var isValid = void 0;
			this.input.find('.has-error').removeClass('has-error');
			this.input.find('.is-invalid').removeClass('is-invalid');
			this.input.find('.validation-message').empty();
			if (!this.mandatory) {
				isValid = true;
			} else {
				isValid = this.input.val() ? true : false;
			}
			if (!isValid) {
				this.input.$validationContainer.text('required');
				this.input.parents('.form-group').addClass('has-error');
				this.input.addClass('is-invalid');
				this.input.$validationContainer.css("display", "block");
			}
			return isValid;
		}
	}, {
		key: 'value',
		get: function get() {
			console.log(this.input.val());
			return this.input.val();
		},
		set: function set(newValue) {
			this.input.val(newValue);
		}
	}]);

	return TextareaForm;
}();
/*

version: 1.0.0
author: sascha obermüller
date: 07.12.2020

*/

var APPModalMTSimulations = function (_APPModal2) {
	_inherits(APPModalMTSimulations, _APPModal2);

	function APPModalMTSimulations(settings, $container) {
		_classCallCheck(this, APPModalMTSimulations);

		// defaults maintable simulations modal
		var modalSettings = $.extend(true, {}, {
			on: {
				simRunModelView: null // function
			}
		}, settings);

		return _possibleConstructorReturn(this, (APPModalMTSimulations.__proto__ || Object.getPrototypeOf(APPModalMTSimulations)).call(this, modalSettings, $container));
	}

	/**
  * CREATE
  * calls super class and sets _metadata
  */

	_createClass(APPModalMTSimulations, [{
		key: '_create',
		value: function _create() {
			var O = this;
			_log('MODAL SIM / _create', 'primary');
			_log(O.opts);

			// global
			O._metadata = O.opts.data;
			O._state = 'params'; // default state: params form

			O._$simInputs = []; // inputs from params and customs
			O._simFields = {};
			O._simSelectedIndex = 0; // initial simulation

			_get(APPModalMTSimulations.prototype.__proto__ || Object.getPrototypeOf(APPModalMTSimulations.prototype), '_create', this).call(this);
		}

		/**
   * CREATE MODAL
   * creates basic modal components: header and blank body
   */

	}, {
		key: '_createModal',
		value: function _createModal() {
			var O = this;
			_log('MODAL SIM / _createModal');
			// modal head default
			O._createModalHead();
			O.opts._loader = O._loader;
			O._simulationPanel = new APPSimulation(O.opts, O._$modalContent);
			O._simulationPanel._createSimulationContent();
		}

		/**
   * BUILD MODAL
   * build modal content
   * @param {event} event 
   */

	}, {
		key: '_updateModal',
		value: async function _updateModal(event) {
			var O = this;
			_log('MODAL SIM / _updateModal');
			_log(event);
			O._loader._setState(true); // set loader

			// get trigger
			var $trigger = $(event.relatedTarget);
			// get model id & data
			O._modelId = $trigger.data('modal-id');
			O._modelMetadata = O._metadata[O._modelId];

			// modal title 
			if (O._modelMetadata.generalInformation && O._modelMetadata.generalInformation.name) {
				_log(O._modelMetadata.generalInformation.name);
				O._setTitle(O._modelMetadata.generalInformation.name);
			}
			// get simulations
			_simulations = await _fetchData._json(window._endpoints.simulations, O._modelId); //O._app._getSimulations( O._modelId );
			await O._simulationPanel._updateContent(O._modelMetadata, O._modelId, _simulations);
			O._loader._setState(false); // set loader
		}
	}]);

	return APPModalMTSimulations;
}(APPModal);
/*

version: 1.0.0
author: Ahmad Swaid
date: 17.12.2020

*/

var APPSimulation = function () {
	function APPSimulation(settings, $container) {
		_classCallCheck(this, APPSimulation);

		var O = this;
		// defaults maintable simulations modal
		O._$container = $container;
		O._opts = $.extend(true, {}, {
			classes: '',
			data: null,
			on: {
				afterInit: null, // function
				show: function show(O, event) {
					O._updateModal(event);
				}, // function
				hide: null // function
			}
		}, settings);
		O._create();
	}

	_createClass(APPSimulation, [{
		key: '_create',

		/**
  * CREATE
  * calls super class and sets _metadata
  */

		value: function _create() {
			var O = this;
			_log('SIM / _create', 'primary');
			_log(O.opts);

			// global
			O._metadata = O.opts.data;
			O._state = 'params'; // default state: params form

			O._$simInputs = []; // inputs from params and customs
			O._simFields = {};
			O._simSelectedIndex = 0; // initial simulation
		}
	}, {
		key: '_createSimulationContent',
		value: function _createSimulationContent() {
			var O = this;
			_log('panel SIM / _createSimulationContent');

			// nav
			O._$modalNav = $('<div class="modal-body sim-select"></div>').appendTo(O._$container);

			// navbar
			O._$navBar = $('<nav class="navbar">').appendTo(O._$modalNav).wrap('<form></form>');

			// sim select label
			O._$simSelectLabel = $('<label class="col-12 col-md-3 sim-select-label" for="simulationSelect">Simulations</label>').appendTo(O._$navBar);

			// sim select counter
			O._$simSelectCounter = $('<span class="badge badge-primary ml-1">x1</span>').appendTo(O._$simSelectLabel);

			// sim select
			O._$simSelect = $('<select id="simulationSelect" class="custom-control custom-select"></select>').attr('id', 'simulationSelect').appendTo(O._$navBar).wrap('<div class="col-12 col-xs-auto col-md-4 sim-select-field"></div>').on('change', function (event) {
				var selectedIndex = O._$simSelect[0].selectedIndex;
				if (selectedIndex >= 0) {
					O._updateSimIndex(selectedIndex);
				}
			});

			// create actions
			O._createSimActions();

			// panel bodys

			// panel params
			O._$modalParams = $('<div class="modal-body p-0 sim-params"></div>').appendTo(O._$container);
			// param content container
			O._$modalParams._$content = $('<div class="tab-content h-100"></div>').appendTo(O._$modalParams);

			// panel execution
			O._$modalExecution = $('<div class="modal-body p-0 sim-execution"></div>').appendTo(O._$container);
			// execution content container
			O._$modalExecution._$content = $('<div class="tab-content h-100"></div>').appendTo(O._$modalExecution);
		}

		/**
   * CREATE SIM ACTIONS
   * creates actionss
   */

	}, {
		key: '_createSimActions',
		value: function _createSimActions() {
			var O = this;
			_log('panel SIM / _createSimActions');

			// sim select actions
			O._$simSelectActions = $('<div class="col-12 col-xs-auto col-md-5 mt-2 mt-xs-0 sim-select-actions"></div>').appendTo(O._$navBar);

			// create actions
			// action group 1
			var $actionGroup1 = $('<div class="col-auto sim-select-actions-group"></div>').appendTo(O._$simSelectActions);

			// remove
			O._$simActionRemove = $('<button type="button" class="btn btn-icon btn-outline-light"><i class="feather icon-trash-2"></i></button>').attr('id', 'simActionRemove').attr('data-tooltip', '').attr('title', 'Remove simulation').appendTo($actionGroup1).on('click', function (event) {
				O._removeSimulation();
			});

			// add
			O._$simActionAdd = $('<button type="button" class="btn btn-icon btn-outline-light ml-1"><i class="feather icon-plus"></i></button>').attr('id', 'simActionAdd').attr('data-tooltip', '').attr('title', 'Add simulation').appendTo($actionGroup1).on('click', function (event) {
				O._addSimulation();
			});

			// save
			O._$simActionSave = $('<button type="button" class="btn btn-icon btn-outline-light ml-1"><i class="feather icon-save"></i></button>').attr('id', 'simActionSave').attr('data-tooltip', '').attr('title', 'Save changes').appendTo($actionGroup1).on('click', function (event) {
				O._saveSimulation();
			});

			// col divider
			$('<div class="col-divider ml-auto ml-xs-0"></div>').appendTo(O._$simSelectActions);

			// action group 2
			var $actionGroup2 = $('<div class="col-auto sim-select-actions-group"></div>').appendTo(O._$simSelectActions);

			O._$simActionRun = $('<button type="button" class="btn btn-icon btn-outline-light ml-1"><i class="feather icon-play"></i></button>').attr('id', 'simActionRun').attr('data-tooltip', '').attr('title', 'Run simulation').appendTo($actionGroup2).on('click', function (event) {
				O._runModelView();
			});

			O._$simSelectActions.wrapInner('<div class="row justify-content-end align-items-center"></div>');
		}

		/**
   * CREATE PARAM METADATA LIST
   * create table for metadata collapse container
   * @param {array} param 
   */

	}, {
		key: '_createParamMetadataList',
		value: function _createParamMetadataList(param) {
			var O = this;
			_log('PANEL SIM / _createParamMetadataList');

			var listData = {
				'ID': param.id,
				'Name': param.name,
				'Description': param.description,
				'Unit': param.unit,
				'Unit category': param.unitCategory,
				'Data type': param.dataType,
				'Source': param.source,
				'Subject': param.subject,
				'Distribution': param.distribution,
				'Reference': param.reference,
				'Variability subject': param.variabilitySubject,
				'Min value': param.minValue,
				'Max value': param.maxValue,
				'Error': param.error
			};
			_log(listData);

			// create table
			var $table = $('<table class="table table-sm table-hover table-params-metadata"></table>');

			// create rows
			$.each(listData, function (name, value) {

				if (value) {
					var $row = $('<tr></tr>').appendTo($table);
					$row.append('<td class="td-label">' + name + '</td>'); // label/name
					$row.append('<td>' + value + '</td>'); // value
				}
			});

			return $table;
		}

		/**
   * CREATE FORM FIELD
   * create field as form group
   * @param {array} param
   */

	}, {
		key: '_createFormField',
		value: function _createFormField(param) {
			var O = this;
			_log('PANEL SIM / _createFormField');
			_log(param);

			if (param) {

				// formgroup
				var _$formGroup2 = $('<div class="form-group row"></div>');
				// .appendTo( O._$simForm );

				// label
				var _$label2 = $('<label class="col-form-label col-form-label-sm col-9 col-xs-3 order-1 sim-param-label"></label>').attr('for', 'paramInput_' + param.id).appendTo(_$formGroup2);
				// set custom label or id
				param._label ? _$label2.text(param._label) : _$label2.text(param.id);

				// field
				var $field = $('<div class="col-12 col-xs-7 col-md-6 order-3 order-xs-2 sim-param-field"></div>').appendTo(_$formGroup2);

				// actions
				var $actions = $('<div class="col-3 col-xs-auto order-2 order-xs-3 sim-param-actions"></div>').appendTo(_$formGroup2);

				// input item
				var $input = null;

				// set input type
				var inputType = null;
				if (param.dataType.toLowerCase() === 'integer' || param.dataType.toLowerCase() === 'double' || param.dataType.toLowerCase() === 'number') {
					inputType = 'number';
				} else if (param.dataType.toLowerCase() === 'simname') {
					inputType = 'simName'; // custom type for name and description
				} else if (param.dataType.toLowerCase() === 'simdescription') {
					inputType = 'simDescription'; // custom type for name and description
				} else if (param.dataType.toLowerCase() === 'vectorofnumbers') {
					inputType = 'vectorofnumbers';
				} else if (param.dataType.toLowerCase() === 'matrixofnumbers') {
					inputType = 'matrixofnumbers';
				} else {
					inputType = 'text';
				}

				// create param metadata action
				if (_isNull(param._showMetadata) || param._showMetadata === true) {
					// action metadata list
					var $actionMetadata = $('<button class="action action-pure" type="button"><i class="feather icon-info"></i></button>').attr('data-toggle', 'collapse').attr('data-target', '#paramMetadata_' + param.id).attr('aria-expanded', false).attr('aria-controls', 'paramMetadata_' + param.id).attr('title', 'Show Metadata').appendTo($actions);
				}

				// add special action for vector or matrix of numbers
				if (inputType == 'vectorofnumbers') {

					var $actionVectoEditor = $('<button class="action action-pure" type="button"><i class="feather icon-edit"></i></button>').attr('title', 'Edit Vector').appendTo($actions);

					// TO DO
					// action what to do on click
				} else if (inputType == 'matrixofnumbers') {

					var _$actionVectoEditor = $('<button class="action action-pure" type="button"><i class="feather icon-edit"></i></button>').attr('title', 'Edit Matrix').appendTo($actions);

					// TO DO
					// action what to do on click
				}

				if (inputType) {

					// numeric
					if (inputType == 'number') {

						var $inputGroup = $('<div class="input-group input-group-sm"></div>').appendTo($field);

						$input = $('<input type="text" />').attr('id', 'paramInput_' + param.id).data('param-input', param).attr('aria-invalid', false).appendTo($inputGroup);

						// rangeslider single, if min/max
						if (param.minValue && param.maxValue) {

							var step = 1;
							// calc decimals for slider steps depending on min-max values
							if (param.dataType.toLowerCase() === 'double') {
								var decimals = Math.max(param.minValue.substring(param.minValue.indexOf('.') + 1).length, param.maxValue.substring(param.maxValue.indexOf('.') + 1).length);
								for (var j = 0; j < decimals; j++) {
									step = step / 10;
								}
							}

							// add rangeslider attributes
							$input.addClass('custom-range').attr('data-rangeslider', '').attr('data-step', step).attr('data-min', parseFloat(param.minValue)) // min value
							.attr('data-max', parseFloat(param.maxValue)) // max value
							.attr('data-control-single', '#paramControlSingle_' + param.id);

							// control input field for range value
							var $inputControl = $('<input type="text" class="form-control" />').attr('id', 'paramControlSingle_' + param.id).data('param-input', param).appendTo($field).wrap('<div class="input-range-controls"></div>').wrap('<div class="input-group input-group-sm input-range-control-single"></div>');

							// O._$simInputs.push( $inputControl );

							// add unit postfix to touchspin
							if (param.unit && param.unit != '[]' && param.unit != 'Others') {
								var $append = $('<div class="input-group-text"></div>').text(param.unit).insertAfter($inputControl).wrap('<div class="input-group-append"></div>');
							}
						}
						// touchspin
						else {
								$input.addClass('form-control form-control-sm').attr('data-touchspin', '');
								// add unit postfix to touchspin
								if (param.unit && param.unit != '[]' && param.unit != 'Others') {
									$input.attr('data-touchspin-postfix', param.unit);
								}
							}
					}
					// sim name
					else if (inputType == 'simName') {
							$input = $('<input type="text" class="form-control form-control-sm" />').attr('id', 'customInput_' + param.id).data('custom-input', param).appendTo($field);

							O._$simNameInput = $input;
						}
						// sim description
						else if (inputType == 'simDescription') {
								$input = $('<textarea type="text" class="form-control form-control-sm" rows="6" /></textarea>').attr('id', 'customInput_' + param.id).data('custom-input', param).appendTo($field);

								O._$simDescInput = $input;
							}
							// string or others
							else {
									$input = $('<input type="text" class="form-control form-control-sm" />').attr('id', 'paramInput_' + param.id).data('param-input', param).appendTo($field);
								}

					// readonly attribute
					param.classification === "CONSTANT" ? $input.attr('readonly', '') : null;

					// add $input to global variable
					O._$simInputs.push($input);
					O._simFields[param.id] = {
						input: $input,
						param: param
					};
				}

				// create validation container
				$input.$validationContainer = $('<div class="validation-message mt-1"></div>').appendTo($field);

				// create param metadata list
				if (_isNull(param._showMetadata) || param._showMetadata === true) {
					// metadata table
					var $metadataContainer = $('<div class="collapse param-metadata"></div>').attr('id', 'paramMetadata_' + param.id).attr('aria-expanded', false).appendTo($field);

					$metadataContainer.append(O._createParamMetadataList(param));
				}

				return _$formGroup2;
			}

			return null;
		}

		/**
   * POPULATE SIM SELECT
   * create select options 
   */

	}, {
		key: '_populateSimSelect',
		value: function _populateSimSelect() {
			var O = this;
			_log('PANEL SIM / _populateSimSelect');

			if (O._simulations && O._simulations.length > 0 && O._$simSelect) {
				// clear sim select
				O._$simSelect.empty();
				// options
				$.each(O._simulations, function (i, sim) {
					if (sim.name) {
						var $option = $('<option>' + sim.name + '</option>').appendTo(O._$simSelect);
					}
				});
				// update badge counter
				O._$simSelectCounter.text('x' + O._simulations.length);
			}
		}

		/**
   * POPULATE SIMULATION FORM
   * creates all input fields
   */

	}, {
		key: '_populateSimForm',
		value: function _populateSimForm() {
			var O = this;
			_log('PANEL SIM / _populateSimForm');

			// clear
			O._state == 'form';
			O._$simInputs = []; // stores all inputs in global var to provide access on params an custom fields like name and description
			O._simFields = {};
			O._selectedSimIndex = 0;
			O._$simForm ? O._clear(O._$simForm) : null; // clear form


			// create form
			O._$simForm = $('<form class="form-striped"></form>').attr('id', 'simParamsForm').appendTo(O._$modalParams._$content);

			// create mandatory custom form group sim name 
			var simNameParam = {
				id: 'simName',
				dataType: 'SIMNAME',
				_showMetadata: false,
				_label: 'Simulation Name',
				_isCustom: true,
				_on: {
					update: function update(O, $input) {
						O._updateSimName();
					}
				}
			};
			var $simNameFormGroup = O._createFormField(simNameParam);
			$simNameFormGroup ? $simNameFormGroup.appendTo(O._$simForm) : null;

			// create optional custom form group sim description 
			var simDescParam = {
				id: 'simDescription',
				dataType: 'SIMDESCRIPTION',
				_showMetadata: false,
				_label: 'Description (optional)',
				_isCustom: true,
				_on: {
					update: function update(O, $input) {
						O._updateSimDescription();
					}
				}
			};
			var $simDescFormGroup = O._createFormField(simDescParam);
			$simDescFormGroup ? $simDescFormGroup.appendTo(O._$simForm) : null;

			// model metadata params
			var params = O._modelMetadata['modelMath']['parameter'];
			if (params.length > 0) {
				// create form group for each param
				$.each(params, function (i, param) {

					if (param.classification != 'OUTPUT') {

						var _$formGroup3 = O._createFormField(param);
						_$formGroup3 ? _$formGroup3.appendTo(O._$simForm) : null;
					}
				});

				// init form items' functions: touchspin, range, select2 ...
				_appUI._initFormItems(O._$simForm);
			}
			_log(O._simFields);
		}

		/**
   * UPDATE SIMULATION INDEX
   * set selected simulation index and trigger update
   */

	}, {
		key: '_updateSimIndex',
		value: function _updateSimIndex(simIndex) {
			var O = this;
			_log('PANEL SIM / _updateSimIndex: ' + simIndex);

			simIndex = !_isNull(simIndex) ? simIndex : O._simSelectedIndex;

			if (simIndex >= -1 && simIndex != O._simSelectedIndex && simIndex <= O._simulations.length - 1) {
				// update sim index
				O._simSelectedIndex = simIndex;

				O._setState('params');

				// update inputs
				O._updateSimForm(simIndex);
				O._updateSimActions(simIndex);
			}

			// update badge counter
			O._$simSelectCounter.text('x' + O._simulations.length);
		}

		/**
   * UPDATE SIMULATION INPUTS
   * changes the selected simulation's inputs
   * @param {integer} simIndex of selected simulation: -1 ich add action, 0 = default
   */

	}, {
		key: '_updateSimForm',
		value: function _updateSimForm(simIndex) {
			var _this27 = this;

			var O = this;
			_log('PANEL SIM / _updateSimForm: ' + simIndex);

			simIndex = !_isNull(simIndex) ? simIndex : O._simSelectedIndex;

			// set value if param or custom
			var valIndex = simIndex;

			if (simIndex < 0) {
				valIndex = 0;
			}

			var simulation = O._simulations[valIndex];

			// disable parameter inputs for the default simulation.
			O._simDisabled = simIndex == 0;

			// remove error classes
			$('.has-error').removeClass('has-error');
			$('.is-invalid').removeClass('is-invalid');

			// update param values
			// $.each( O._$simInputs, ( i, $input ) => {

			$.each(O._simFields, function (id, field) {

				if (field.input && field.param) {

					// disable or enable 
					field.input.prop('disabled', O._simDisabled);

					// disable rangeslider 
					if (field.input.data('rangeslider')) {
						field.input.data('rangeslider').update({
							disable: O._simDisabled
						});
					}
					// disable touchspin
					else if (!_isUndefined(field.input.attr('data-touchspin'))) {
							// disable/enable buttons oof touchspin group
							field.input.parent().find('button').prop('disabled', O._simDisabled);
						}
					// param fields
					if (!field.param._isCustom) {
						// set value of current selected sim index
						_log(field.param.id + ' : ' + simulation.parameters[field.param.id], 'level1');
						var paramValue = simulation.parameters[field.param.id];

						!_isNull(paramValue) ? field.input.val(paramValue) : null;
					}
					// custom fields like name and desc
					else if (field.param._isCustom && field.param._isCustom == true) {

							_log(field.param.id + ' : ', 'level1');

							// check opt for custom update function 
							if (field.param._on && field.param._on.update && $.isFunction(field.param._on.update)) {
								field.param._on.update.call(_this27, O);
							}
						}

					// trigger change
					field.input.trigger('change');
				}
			});
		}

		/**
   * UPDATE SIMULATION ACTIONS
   * changes the selected simulation's inputs
   * @param {integer} simIndex of selected simulation
   */

	}, {
		key: '_updateSimActions',
		value: function _updateSimActions(simIndex) {
			var O = this;
			_log('PANEL SIM / _updateSimActions');

			simIndex = !_isNull(simIndex) ? simIndex : O._simSelectedIndex;

			if (simIndex == -1) {
				// add simulation
				O._$simActionRemove.prop('disabled', false);
				O._$simActionAdd.prop('disabled', true);
				O._$simActionSave.prop('disabled', false);
			} else if (simIndex == 0) {
				// default sim
				O._$simActionRemove.prop('disabled', true);
				O._$simActionAdd.prop('disabled', false);
				O._$simActionSave.prop('disabled', true);
			} else {
				// selected sim > 0
				O._$simActionRemove.prop('disabled', false);
				O._$simActionAdd.prop('disabled', false);
				O._$simActionSave.prop('disabled', false);
			}
		}

		/**
   * UPDATE SIMULATION CUTOM NAME
   * changes the custom input name to selected simulation's name
   */

	}, {
		key: '_updateSimName',
		value: function _updateSimName() {
			var O = this;
			_log('PANEL SIM / _updateSimName');

			if (O._$simNameInput) {
				var simName = '';
				if (!_isNull(O._simulations) && !_isNull(O._simSelectedIndex) && O._simulations[O._simSelectedIndex]) {
					// get simulation name
					simName = O._simulations[O._simSelectedIndex].name;
				}
				// set name
				O._$simNameInput.val(simName);
			}
		}

		/**
   * UPDATE SIMULATION CUSTOM DESCRIPTUION
   * changes the custom input description to selected simulation's name
   * @param {jquery elem} $input
   */

	}, {
		key: '_updateSimDescription',
		value: function _updateSimDescription($input) {
			var O = this;
			_log('PANEL SIM / _updateSimDescription');

			if (O._$simDescInput) {
				var simDesc = '';
				if (!_isNull(O._simulations) && !_isNull(O._simSelectedIndex) && O._simulations[O._simSelectedIndex]) {
					// get simulation name
					simDesc = O._simulations[O._simSelectedIndex].desc;
				}
				// set name
				O._$simDescInput.val(simDesc);
			}
		}

		/**
   * ADD SIMULATION
   * add the selected simulation
   */

	}, {
		key: '_addSimulation',
		value: function _addSimulation() {
			var O = this;
			_log('PANEL SIM / _addSimulation', 'secondary');

			// clear sim select
			O._$simSelect.val('');

			// update sim select to index for add-action : -1
			O._updateSimIndex(-1);
		}

		/**
   * REMOVE SIMULATION
   * remove the selected simulation
   */

	}, {
		key: '_removeSimulation',
		value: function _removeSimulation() {
			var O = this;
			_log('PANEL SIM / _removeSimulation', 'secondary');

			if (O._simSelectedIndex != 0) {
				// remove from sim select
				O._$simSelect.find('option').eq(O._simSelectedIndex).remove();

				// remove from simulations var
				O._simulations.splice(O._simSelectedIndex, 1);

				// reset to default
				O._$simSelect.find('option').eq(0).prop('selected', true);
				O._updateSimIndex(0);
			}
		}

		/**
   * SAVE SIMULATION
   * save the selected simulation
   */

	}, {
		key: '_saveSimulation',
		value: async function _saveSimulation() {
			var O = this;
			_log('PANEL SIM / _saveSimulation', 'secondary');
			// run validation
			var validation = await O._validateSimForm();

			// no errors ?
			if (validation.length == 0) {

				var newSimulation = JSON.parse(JSON.stringify(O._simulations[0]));
				// set new simulation's name
				newSimulation.name = O._$simNameInput.val();
				// set sim desc
				newSimulation.desc = O._$simDescInput ? O._$simDescInput.val() : '';

				// set new simulation's parameters
				if (O._simFields) {
					// for each input get value and et param value
					$.each(O._simFields, function (id, field) {

						if (field.input && !_isNull(field.param)) {
							// create simulation param
							newSimulation.parameters[field.param.id] = field.input.val();
						}
					});

					O._simulations.push(newSimulation);
					// re-populate sim select
					O._populateSimSelect();
					// update sim index
					O._$simSelect.find('option').last().prop('selected', true);
					O._updateSimIndex(O._simulations.length - 1);
				}
			}

			// TO DO 
			// save to endpoint ?
		}

		/**
   * VALIDATE SIMULATION FORM
   * run validation on sim form inputs
   */

	}, {
		key: '_validateSimForm',
		value: async function _validateSimForm() {
			var O = this;
			_log('PANEL SIM / _validateSimForm');

			var validationErrors = [];
			// remove error classes
			$('.has-error').removeClass('has-error');
			$('.is-invalid').removeClass('is-invalid');
			$('.validation-message').empty();

			// validate sim inputs values
			if (O._simFields) {

				$.each(O._simFields, function (id, field) {
					// let param = $input.data( 'param-input' );

					if (!_isNull(field.param)) {
						var validationParam = O._validateSimField(field);
						validationParam ? validationErrors.push(validationParam) : null;
					}
				});
			}

			// proceed error visualization
			$.each(validationErrors, function (i, error) {

				error.input.parents('.form-group').addClass('has-error');
				error.input.addClass('is-invalid');

				error.input.$validationContainer.text(error.msg);
				// let $errorMsg = $( '<div class="alert alert-danger alert-xs"></div>' )
				// 	.appendTo( error.input.$validationContainer )
				// 	.text( error.msg );
			});

			_log(validationErrors);

			return validationErrors;
		}

		/**
   * VALIDATE SIMULATION PARAM
   * run validation on param field
   */

	}, {
		key: '_validateSimField',
		value: function _validateSimField(field) {
			var O = this;
			_log('PANEL SIM / _validateSimField');
			_log(field);

			if (field && field.input) {

				var fieldValue = field.input.val();

				// check simulation name
				if (field.param.dataType.toLowerCase() === 'simname') {

					var idRegexp = /^[A-Za-z_^s]\w*$/;

					// name length
					if (fieldValue.length == 0) {
						return {
							input: field.input,
							msg: 'Simulation name is required'
						};
					}
					// name already exists?
					for (var i = 0; i < O._simulations.length; ++i) {
						if (fieldValue === O._simulations[i].name) {
							return {
								input: field.input,
								msg: 'Simulation name already exists'
							};
						}
					}
					// name fits regexp
					if (!idRegexp.test(fieldValue)) {
						return {
							input: field.input,
							msg: 'Not a valid simulation name (SId)'
						};
					}
				} else if (field.param.dataType.toLowerCase() === 'simdescription') {} else {

					// no value
					if (!fieldValue) {
						return {
							input: field.input,
							msg: 'Please provide a value for ' + field.param.id
						};
					}

					// check range for integers and doubles
					if (field.param.dataType.toLowerCase() === 'integer' || field.param.dataType.toLowerCase() === 'double') {

						if (field.param.minValue && parseFloat(field.param.minValue) > fieldValue) {
							return {
								input: field.input,
								msg: 'Invalid value. Value is lower than minimal value: ' + field.param.minValue
							};
						}
						if (field.param.maxValue && parseFloat(field.param.maxValue) < fieldValue) {
							return {
								input: field.input,
								msg: 'Invalid value. Value is higher than maximum value: ' + field.param.maxValue
							};
						}
					} else if (field.param.dataType.toLowerCase() === 'file') {

						// TBD

					} else if (field.param.dataType.toLowerCase() === 'vectorofnumbers') {

						// TBD

					} else if (field.param.dataType.toLowerCase() === 'matrixofnumbers') {

						// TBD

					}
				}
			}

			return false;
		}
		/**
  * BUILD PANEL
  * build PANEL content
  * @param {event} event 
  */

	}, {
		key: '_updateContent',
		value: async function _updateContent(_modelMetadata, _modelId, _simulations) {
			var O = this;
			_log('PANEL SIM / _updateContent');
			O._setState('params'); // reset state to form params when opening PANEL

			O._modelMetadata = _modelMetadata;
			O._modelId = _modelId;
			// clear tab-panes
			O._clear(O._$modalParams._$content);

			O._simSelectedIndex = 0; // reset simulation
			// get simulations
			O._simulations = _simulations;

			// create params		
			if (O._simulations && O._simulations.length > 0) {

				// populate sim select
				O._populateSimSelect();
				// populate sim form
				O._populateSimForm();
				// update form groups' value
				O._updateSimForm(0);
				O._updateSimActions(0);
			}
		}

		/**
   * RUN MODEL VIEW
   * run modelview
   */

	}, {
		key: '_runModelView',
		value: async function _runModelView(modelId) {
			var O = this;
			_log('PANEL SIM / _runModelView : ' + O._modelId, 'secondary');

			modelId = modelId || O._modelId;

			if (modelId >= 0) {

				// clear tab content
				O._clear(O._$modalExecution._$content);
				O._clear(O._$modalExecution._$title);
				O._setState('execution');

				// activate loader
				O._opts._loader._setState(true);
				// create loading alert
				var $alert = _appUI._createAlert('executing model... please wait', {
					type: 'primary',
					state: 'show',
					classes: 'm-1'
				}, O._$modalExecution._$content);

				// TO DO 
				// run simulation by selected index

				// execute result
				var result = await _fetchData._content(window._endpoints.execution, modelId); //O._app._getExecutionResult( modelId ) ;

				$alert.remove();

				// add executet simulation name as panel-title
				var $title = $('<div class="panel-heading"></div>').text(O._simulations[O._selectedSimIndex].name).appendTo(O._$modalExecution._$content);

				// add result as plot
				var $plot = $(result).appendTo(O._$modalExecution._$content).wrapAll('<div class="panel-plot"></div>');

				// deactivate loader
				O._opts._loader._setState(false);

				// callback
				if ($.isFunction(O.opts.on.simRunModelView)) {
					O.opts.on.simRunModelView.call(O, O, modelId, O._simulations[O._selectedSimIndex]);
				}
			}
		}

		/**
   * CLEAR
   * removes content from element
   * @param {jquery selector} $elem
   */

	}, {
		key: '_clear',
		value: function _clear($elem) {
			var O = this;
			_log('PANEL SIM / _clear');
			_log($elem);

			if ($elem) {
				$elem.empty();
			}
		}

		/**
  * CLEAR
  * removes content from element
  * @param {string} state: execute or params
  */

	}, {
		key: '_setState',
		value: function _setState(state) {
			var O = this;
			_log('PANEL SIM / _setState: ' + state);
			if (state && O._state != state) {

				if (state == 'execution') {
					O._$modalExecution.show();
					O._$modalParams.hide();
					O._$modalNav.hide();
				} else {
					O._$modalExecution.hide();
					O._$modalParams.show();
					O._$modalNav.show();
				}
				// set sim modal state
				O._state = state;
			}
		}
	}, {
		key: 'opts',
		get: function get() {
			return this._opts;
		},
		set: function set(settings) {
			this._opts = $.extend(true, {}, this.opts, settings);
		}
	}]);

	return APPSimulation;
}();
/*

version: 1.0.0
author: Ahmad Swaid
date: 17.12.2020

*/

var APPMTDetails = function () {
	function APPMTDetails(settings, $container) {
		_classCallCheck(this, APPMTDetails);

		var O = this;
		// defaults maintable simulations modal
		O._$modalContent = $container;
		O._opts = $.extend(true, {}, {
			classes: '',
			data: null,
			on: {
				afterInit: null, // function
				show: function show(O, event) {
					O._updateModal(event);
				}, // function
				hide: null // function
			}
		}, settings);
		O._create();
	}

	_createClass(APPMTDetails, [{
		key: '_create',

		/**
   * CREATE
   * calls super class and sets _metadata
   */

		value: function _create() {
			var O = this;
			_log('MODAL DETAILS / _create', 'primary');

			O._metadata = O.opts.data;
		}
		/**
   * CREATE MODAL
   * creates basic modal components: header and blank body
   */

	}, {
		key: '_createModelMetadataContent',
		value: function _createModelMetadataContent() {
			var O = this;
			_log('MODAL DETAILS / _createModelMetadataContent');
			// modal nav with tabs & search
			O._$modalNav = $('<div class="modal-body modal-nav"></div>').appendTo(O._$modalContent);

			O._navId = O._id + 'Nav';
			if (!O._$navBar) {
				O._$navBar = $('<nav class="navbar navbar-expand-sm row justify-content-start justify-content-md-between"></nav>').appendTo(O._$modalNav);

				// nav toggle
				var $navToggle = $('<button class="action action-pure mt-1 mb-1" type="button" data-toggle="collapse" aria-expanded="false" aria-label="Toggle navigation"><i class="feather icon-list"></i></button>').appendTo(O._$navBar).attr('data-target', '#' + O._navId).attr('aria-controls', O._navId).wrap('<div class="col-auto navbar-toggler order-1 modal-nav-toggler"></div>');

				// divider
				O._$navBar.append('<div class="col-divider order-2 d-block d-sm-none d-md-block"></div>');

				// nav search
				O._$navBar._$search = $('<input class="form-control form-control-plaintext search-input" type="search" placeholder="Search Details" aria-label="Search Details" />').appendTo(O._$navBar).attr('id', O._id + 'NavSearch').wrap('<div class="col col-xxs-auto order-2 modal-nav-search"></div>').wrap('<div class="search"></div>');

				// TO DO
				// search functionality


				// nav tabs
				O._$navBar._$nav = $('<ul class="nav nav-pointer pt-1 pt-md-0"></ul>').appendTo(O._$navBar).wrap('<div class="col-12 col-md-auto order-3 order-md-1 modal-nav-menu order-4"></div>').wrap('<div class="collapse navbar-collapse" id="' + O._navId + '"></div>');
			}

			// modal body
			O._createModalBody();
			O._$modalBody.addClass('p-0 modal-table');

			// content container
			O._$modalTabContent = $('<div class="tab-content h-100"></div>').appendTo(O._$modalBody);
		}

		/**
  * CREATE MODAL
  * creates basic modal components: header and blank body
  */

	}, {
		key: '_createModalBody',
		value: function _createModalBody() {
			var O = this;
			_log('MODAL / _createBody');

			O._$modalBody = $('<div class="modal-body"></div>').appendTo(O._$modalContent);
		}

		/**
  * BUILD PANEL
  * build PANEL content
  * @param {event} event 
  */

	}, {
		key: '_updateContent',
		value: async function _updateContent(_modelMetadata, _modelId) {
			var O = this;
			_log('PANEL MetaData / _updateContent');

			// clear tab-panes
			O._$modalTabContent.html('');

			// get appropiate modelMetadata modelHandler for the model type.
			O._modelHandler = await O._getModelHandler(_modelMetadata);
			// populate nav
			O._populateModalNav(O._modelHandler, O._$navBar._$nav);

			// populate panel
			O._populateModalPanel(O._modelHandler);

			// activate first pane
			O._$navBar._$nav.find('.nav-link').first().addClass('active');
			O._$modalTabContent.find('.tab-pane').first().addClass('active');
		}

		/**
   * POPULATE MODAL MENU
   * @param {object} Model
   */

	}, {
		key: '_populateModalNav',
		value: function _populateModalNav(modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _populateModalNav');
			_log(modelHandler);

			// clear nav
			O._$navBar._$nav.html('');

			// create nav items
			if (modelHandler && modelHandler._menu) {

				$.each(modelHandler._menu, function (i, menuMeta) {

					var $navItem = null;

					if (menuMeta.submenus && menuMeta.submenus.length > 0) {
						$navItem = O._createNavItemDropdown(menuMeta).appendTo(O._$navBar._$nav);
					} else {
						var _$navItem2 = O._createNavItem(menuMeta).appendTo(O._$navBar._$nav);
					}
				});
			}
		}

		/**
   * POPULATE MODAL PANEL
   * @param {object} Model
   */

	}, {
		key: '_populateModalPanel',
		value: function _populateModalPanel(modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _populateModalPanel');
			_log(modelHandler);

			// create panels
			if (modelHandler && modelHandler._menu && modelHandler._panels) {
				// get each menus id
				$.each(modelHandler._menu, function (i, menuMeta) {
					// dropdown nav item 
					if (menuMeta.submenus && menuMeta.submenus.length > 0) {
						// iterate over submenus
						$.each(menuMeta.submenus, function (j, submenuMeta) {
							// panel meta data exists in handler
							if (submenuMeta.id in modelHandler._panels) {
								O._createPanel(submenuMeta, modelHandler).appendTo(O._$modalTabContent);
							}
						});
					}
					// single nav item ? create panel
					else {
							if (menuMeta.id) {
								if (menuMeta.id in modelHandler._panels) {
									O._createPanel(menuMeta, modelHandler).appendTo(O._$modalTabContent);
								}
							}
						}
				});
			}
		}

		/**
   * CREATE NAV ITEM DROPDOWN
   * @param {array} menuMeta: array of dropdown-items width 'id' and 'label'
   */

	}, {
		key: '_createNavItemDropdown',
		value: function _createNavItemDropdown(menuMeta) {
			var O = this;
			_log('MODAL DETAILS / _createTabNavItemDropdown: ' + menuMeta.label);

			var $navItem = $('<li class="nav-item dropdown"></li>');

			var $navLink = $('<a class="nav-link dropdown-toggle" role="button">' + menuMeta.label + '</a>').attr('href', '#').attr('aria-haspopup', true).attr('aria-expanded', false).attr('data-toggle', 'dropdown').appendTo($navItem);
			var $dropdown = $('<div class="dropdown-menu"></div>').appendTo($navItem);

			$.each(menuMeta.submenus, function (i, submenuMeta) {

				var $dropdownItem = $('<a class="dropdown-item" role="button">' + submenuMeta.label + '</a>').attr('href', '#' + submenuMeta.id).attr('aria-controls', '#' + submenuMeta.id).attr('data-toggle', 'tab').appendTo($dropdown);
			});

			return $navItem;
		}

		/**
   * CREATE NAV ITEM
   * @param {array} menuMeta
   */

	}, {
		key: '_createNavItem',
		value: function _createNavItem(menuMeta) {
			var O = this;
			_log('MODAL DETAILS / _createNavItem: ' + menuMeta.label);

			var $navItem = $('<li class="nav-item"></li>');
			var $navLink = $('<a class="nav-link" role="button">' + menuMeta.label + '</a>').attr('href', '#' + menuMeta.id).attr('aria-controls', '#' + menuMeta.id).attr('data-toggle', 'tab').appendTo($navItem);

			return $navItem;
		}

		/**
   * CREATE PANEL
   * create tab-pane for specific menu by selecting type and calling specific creation (simple, complex, plot)
   * @param {array} menu
   * @param {object} modelHandler: object of type Model
   */

	}, {
		key: '_createPanel',
		value: function _createPanel(menu, modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _createPanel: ' + menu.id);

			var $panel = null;
			if (modelHandler && menu.id) {

				var panelMeta = modelHandler._panels[menu.id];
				// panel type
				if (panelMeta.type) {
					// complex
					if (panelMeta.type == 'complex') {
						$panel = O._createComplexPanel(menu, modelHandler);
					}
					// simple
					else if (panelMeta.type == 'simple') {
							$panel = O._createSimplePanel(menu, modelHandler);
						}
						// plot
						else if (panelMeta.type == 'plot') {
								$panel = O._createPlotPanel(menu, modelHandler);
							}
				}
			}

			return $panel;
		}

		/**
   * CREATE SIMPLE PANEL
   * create simple tab-pane for specific menu
   * table has property, value cols
   * @param {array} menu
   * @param {object} modelHandler: object of type Model
   */

	}, {
		key: '_createSimplePanel',
		value: function _createSimplePanel(menu, modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _createSimplePanel: ' + menu.id);

			// tab-pane
			var $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>').attr('id', menu.id);

			if (modelHandler && menu.id) {
				// get panel meta
				var panelMeta = modelHandler._panels[menu.id];

				// title
				$panel.append('<div class="panel-heading">' + menu.label + '</div>');

				// table settings
				var tableSettings = {
					cols: [{
						label: 'Property',
						field: 'property',
						classes: {
							th: null,
							td: 'td-label min-200'
						},
						sortable: true,
						switchable: false
					}, {
						label: 'Value',
						field: 'value',
						sortable: false,
						switchable: false
					}],
					tableData: [],
					responsive: true,
					showToggle: true
				};

				// set table row data
				if (panelMeta.metadata && panelMeta.schema) {
					$.each(panelMeta.schema, function (j, prop) {
						var rowData = {
							cells: []
						};
						// cell 1 label
						rowData.cells.push(prop.label);
						// cell 2 val
						var data = panelMeta.metadata[prop.id];
						data = _checkUndefinedContent(data);
						rowData.cells.push(data);

						tableSettings.tableData.push(rowData);
					});
				}

				// create table
				var panelTable = new APPTable(tableSettings, $panel);
				$panel.data('table', panelTable);
			};

			return $panel;
		}

		/**
   * CREATE COMPLEX PANEL
   * create complex tab-pane for specific menu
   * table has in metadata and schema defined cols
   * @param {array} menu
   * @param {object} modelHandler: object of class Model
   */

	}, {
		key: '_createComplexPanel',
		value: function _createComplexPanel(menu, modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _createComplexPanel: ' + menu.id);

			// tab-pane
			var $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>').attr('id', menu.id);

			if (modelHandler && menu.id) {
				// get panel meta
				var panelMeta = modelHandler._panels[menu.id];

				// title
				$panel.append('<div class="panel-heading">' + menu.label + '</div>');

				// table settings
				var tableSettings = {
					cols: [],
					tableData: [],
					responsive: true,
					showToggle: true
				};

				// set table cols
				$.each(panelMeta.schema, function (i, prop) {
					tableSettings.cols.push({
						label: prop.label,
						field: prop.id,
						sortable: true,
						switchable: true
					});
				});

				// set table row data
				if (panelMeta.metadata && panelMeta.schema) {
					$.each(panelMeta.metadata, function (i, item) {
						// row each item
						var rowData = {
							cells: []
						};
						// cells
						$.each(panelMeta.schema, function (j, prop) {
							var data = item[prop.id];
							data = _checkUndefinedContent(data);
							// cell each prop
							rowData.cells.push(data);
						});

						tableSettings.tableData.push(rowData);
					});
				}

				// create table
				var panelTable = new APPTable(tableSettings, $panel);
				$panel.data('table', panelTable);
			};
			return $panel;
		}

		/**
   * CREATE PLOT PANEL
   * create plot tab-pane for specific menu
   * @param {array} menu
   * @param {object} modelHandler: object of class Model
   */

	}, {
		key: '_createPlotPanel',
		value: function _createPlotPanel(menu, modelHandler) {
			var O = this;
			_log('MODAL DETAILS / _createPlotPanel');

			// tab-pane
			var $panel = $('<div class="tab-pane h-100" role="tabpanel"></div>').attr('id', menu.id);

			if (modelHandler && menu.id && modelHandler._img) {
				// get panel meta
				var panelMeta = modelHandler._panels[menu.id];

				// title
				$panel.append('<div class="panel-heading">' + menu.label + '</div>');

				var $plot = $('<figure class="figure"><img src="' + modelHandler._img + '" /></figure>').appendTo($panel).wrap('<div class="panel-plot"></div>');
			}

			return $panel;
		}

		/**
   * GET MODEL HANDLER
   * returns model handler of class Model
   * @param {array} modelMetadata: metadata for specific id
   */

	}, {
		key: '_getModelHandler',
		value: async function _getModelHandler(modelMetadata) {
			var O = this;
			_log('MODAL DETAILS / _getModelHandler');
			console.log(modelMetadata);

			var modelHandler = null;

			if (modelMetadata) {

				// get plot image
				var imgUrl = void 0;
				// get appropiate modelMetadata modelHandler for the model type.

				if (modelMetadata.modelType === 'genericModel') {
					modelHandler = new GenericModel(modelMetadata, imgUrl, false);
				} else if (modelMetadata.modelType === 'dataModel') {
					modelHandler = new DataModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'predictiveModel') {
					modelHandler = new PredictiveModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'otherModel') {
					modelHandler = new OtherModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'toxicologicalModel') {
					modelHandler = new ToxicologicalModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'doseResponseModel') {
					modelHandler = new DoseResponseModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'exposureModel') {
					modelHandler = new ExposureModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'processModel') {
					modelHandler = new ProcessModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'consumptionModel') {
					modelHandler = new ConsumptionModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'healthModel') {
					modelHandler = new HealthModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'riskModel') {
					modelHandler = new RiskModel(modelMetadata, imgUrl);
				} else if (modelMetadata.modelType === 'qraModel') {
					modelHandler = new QraModel(modelMetadata, imgUrl);
				} else {
					modelHandler = new GenericModel(modelMetadata, imgUrl, false);
				}
			}

			return modelHandler;
		}
	}, {
		key: 'opts',
		get: function get() {
			return this._opts;
		},
		set: function set(settings) {
			this._opts = $.extend(true, {}, this.opts, settings);
		}
	}]);

	return APPMTDetails;
}();
/*

version: 1.0.0
author: sascha obermüller
date: 07.12.2020

*/

var APPTable = function () {
	function APPTable(settings, $container) {
		_classCallCheck(this, APPTable);

		var O = this;
		O._$container = $container;
		O._$wrapper = null;
		O.totalRows = 0;
		// defaults
		O._opts = $.extend(true, {}, {
			attributes: {}, // attribute : value pairs for <table>
			classes: '', // string of classes for <table>
			cols: [], // cols definition as array
			tableData: null, // table data
			ids: { // ids for specific table elements
				table: 'tGrid',
				thead: 'tHead',
				tbody: 'tRows'
			},
			responsive: true, // wrap table with .table-responsive
			rowActions: [],
			rowSelectable: false, // 'single', // 'multiple', //
			showToggle: true, // show card view toggle
			wrapper: false, // 'card'
			on: { // hooks/callbacks on specific events
				afterInit: null,
				afterPopulate: null,
				selectRow: null,
				deselectRow: null
			}
		}, settings);

		// basic init actions
		O._create();

		// callback
		if ($.isFunction(O.opts.on.afterInit)) {
			O.opts.on.afterInit.call(O, O);
		}
	}

	_createClass(APPTable, [{
		key: 'addRow',
		value: function addRow(rowIndex, rowData, tableData) {
			var O = this;
			tableData = O._tableData;
			// row
			var $tr = $('<tr data-row-id="' + rowIndex + '"></tr>').appendTo(O._$tbody);

			// rows selectable
			if (O.opts.rowSelectable) {

				// add selectable attribrutes
				$tr.data('selectable', '');
				$tr.data('selected', false);

				// add row click actions
				$tr.on('click', function (event) {
					// activate row not on click on buttons, actions or links in the table
					if (!$(event.target).is('a, button, .action') && !$(event.target).parent().is('a, button, .action')) {
						// select row
						O._handleRowSelect($tr);
					}
				});
			}

			// complete table data by adding row element to certain row data
			tableData[rowIndex].el = $tr;

			// create cols
			$.each(O.opts.cols, function (j, col) {
				var data = void 0;
				if (rowData.cells) data = rowData.cells[j];else data = rowData[col.field];
				var $td = $('<td></td>').appendTo($tr);
				if (data.length > 60) {
					col.collapsable = "true";
				}
				col.classes && col.classes.td ? $td.addClass(col.classes.td) : null; // classes
				col.collapsable ? $td.attr('data-td-collapse', col.collapsable) : null; // data collapsable
				col.label ? $td.attr('data-label', col.label) : null; // add data-label for toggle view cards
				col.id ? $td.attr('data-id', col.id) : null;

				// td attributes
				if (col.attributes && col.attributes.td) {
					$.each(col.attributes.td, function (attr, val) {
						attr && val ? $td.attr(attr, val) : '';
					});
				}

				// check for function that format the data
				if (col.formatter) {
					if ($.isFunction(col.formatter)) {
						data = col.formatter.call(O, data);
					} else if (_formatter && _formatter.hasOwnProperty(col.formatter)) {
						data = _formatter[col.formatter].call(O, data);
					}
				}
				// fill td with data
				$td.html(data);
			});

			// create row actions 
			if (O.opts.rowActions && O.opts.rowActions.length > 0) {

				// create action col
				var $tdActions = $('<td class="td-actions"></td>').appendTo($tr);

				// create row actions 
				$.each(O.opts.rowActions, function (j, action) {

					// create action element
					var $action = $('<button class="action action-outline-secondary"></button>').attr('id', action.idPrefix + rowIndex);
					// .appendTo( $tdActions );

					// create action icon
					$action.$icon = $('<i class="feather"></i>').appendTo($action);
					// set icon by class
					action.icon ? $action.$icon.addClass(action.icon) : null;

					// set tooltip and title
					if (action.title) {
						$action.attr('data-tooltip', '').attr('aria-label', action.title).attr('title', action.title);
					}

					// action on click
					if (action.on) {
						if (action.on.click && $.isFunction(action.on.click)) {
							// bind click action on action
							$action.on('click', function (event) {
								console.log('moooooooo', event);
								actionIndex = $tr.attr('data-row-id');
								action.on.click.call(O, O, $action, actionIndex, rowData);
							});
						}
					}

					// add action type specific attributes
					if (action.type) {
						// create modal action
						if (action.type == 'modal') {
							$action.attr('data-toggle', 'modal').attr('data-target', action.target).attr('data-modal-id', rowIndex);
						}
					}
					// append to td
					$action.appendTo($tdActions);
				});

				// wrap actions with inner container of td
				$tdActions.wrapInner('<div class="td-actions-container"></div>');
			}
			O.totalRows = rowIndex;
		}
		/**
   * CREATE TABLE HEAD
   * @param
   */

	}, {
		key: '_create',
		value: async function _create() {
			var O = this;
			_log('TABLE / _create', 'primary');

			// current state of view
			O._view = 'default'; // card; //

			// table rows
			O._tableData = O.opts.tableData;
			_log(O._tableData);

			// callback on create
			if ($.isFunction(O.opts.on.create)) {
				O.opts.on.create.call(O, O);
			}

			if (O._tableData) {

				// create table element
				O._$table = $('<table class="table table-striped table-hover"></table>').attr('id', O.opts.ids.table).attr('data-table', '').addClass(O.opts.classes).appendTo(O._$container);

				// wrapper
				if (O.opts.wrapper) {
					// card
					if (O.opts.wrapper == 'card') {
						O._$wrapper = $('<div class="card card-table-main overflow-hidden"></div>');
						O._$table.wrap(O._$wrapper);
						O._$table.wrap('<div class="card-body p-0"></div>');
					}
				}

				// responsive table wrapper
				if (O.opts.responsive) {
					O._$table.wrap('<div class="table-responsive"></div>');
				}

				// custom classes
				O.opts.classes ? O._$table.addClass(O.opts.classes) : null;

				// card view toggle
				O.opts.showToggle ? O._$table.attr('data-show-toggle', O.opts.showToggle) : null; // bs table / data-show-toggle for view: table/list

				// custom table attributes
				$.each(O.opts.attributes, function (attr, val) {
					O._$table.attr(attr, val);
				});

				// toolbar
				O._createTableToolbar();

				// create table head
				O._$thead = O._createTableHead(O.opts.cols).appendTo(O._$table);

				// create table head
				O._$tbody = O._createTableBody().appendTo(O._$table);

				// populate table
				O._populateTable(O._tableData);
			}

			// tooltips
			_appUI._initTooltips(O._$container);
		}

		/**
   * CREATE TABLE HEAD
   * @param {array} cols
   */

	}, {
		key: '_createTableHead',
		value: function _createTableHead(cols) {
			var O = this;
			_log('TABLE / _createTableHead');

			// thead
			var $thead = $('<thead></thead>');

			// create th cols
			if (cols) {
				$.each(cols, function (i, col) {
					var $th = $('<th></th>').appendTo($thead);

					// th attributes
					col.label ? $th.html('<span>' + col.label + '</span>') : null;
					col.id ? $th.attr('id', col.id) : null; // id
					col.classes && col.classes.th ? $th.addClass(col.classes.th) : null; // classes
					col.field ? $th.attr('data-field', col.field) : null; // bs table / data-field identifier

					// add sort functionality
					if (col.sortable) {
						$th.attr('data-sortable', col.sortable);
						$th.on('click', function (event) {
							O._updateOrder($th);
						});
						col.sorter ? $th.attr('data-sorter', col.sorter) : null; // bs table / data-sorter function
					}

					// th custom attributes
					if (col.attribute && col.attributes.th) {
						$.each(col.attributes.th, function (attr, val) {
							$th.attr(attr, val);
						});
					}
				});

				// create row actions col
				if (O.opts.rowActions && O.opts.rowActions.length > 0) {
					var $th = $('<th></th>').attr('id', 'colActions').attr('data-field', 'actionsTable').appendTo($thead);
				}

				$thead.wrapInner('<tr></tr>');
			}

			return $thead;
		}

		/**
   * CREATE TABLE TOOLBAR
   * creates button bar above the table for responsive toggle
   */

	}, {
		key: '_createTableToolbar',
		value: function _createTableToolbar() {
			var O = this;
			_log('TABLE / _createTableToolbar');

			if (O.opts.showToggle || O.opts.showColumns) {
				O._$toolbar = $('<div class="table-toolbar"></div>');

				if (O.opts.responsive) {
					O._$toolbar.insertBefore(O._$table.parent());
				} else {
					O._$toolbar.insertBefore(O._$table);
				}
				// place button group
				O._$toolbar._$btnGroup = $('<div class="col-auto btn-group"></div>').appendTo(O._$toolbar).wrap('<div class="ml-auto row justify-content-end"></div>');

				// create toolbar buttons 
				// toggle view
				if (O.opts.showToggle) {
					O._$toolbar._$btnToggleView = $('<button class="btn btn-outline-secondary btn-sm btn-icon toggle-card" type="button"><i class="feather icon-pause"></i></button>').attr('aria-label', 'Toggle view').attr('title', 'Toggle view').attr('data-tooltip', '').attr('data-toggle-table-view', '').appendTo(O._$toolbar._$btnGroup);

					O._$toolbar._$btnToggleView.on('click', function (event) {
						O._toggleTableView();
					});
				}
				if (O._opts.editableToolbarbuttons) {
					$.each(O._opts.editableToolbarbuttons, function (index, element) {
						element.appendTo(O._$toolbar._$btnGroup);
					});
				}
			}
		}

		/**
   * CREATE TABLE BODY
   * @param {object} tableData: metadata object
   */

	}, {
		key: '_createTableBody',
		value: function _createTableBody() {
			var O = this;
			_log('TABLE / _createTableBody');

			// table body
			var $tbody = $('<tbody></tbody>');

			return $tbody;
		}

		/**
   * POPULATE TABLE
   * @param {object} tableData: data object
   */

	}, {
		key: '_populateTable',
		value: function _populateTable(tableData) {
			var _this28 = this;

			var O = this;
			_log('TABLE / _populateTable');
			O._clear();

			tableData = tableData || O._tableData;

			// create rows
			$.each(tableData, function (rowIndex, rowData) {
				_this28.addRow(rowIndex, rowData, tableData);
			});

			// callback
			if ($.isFunction(O.opts.on.afterPopulate)) {
				O.opts.on.afterPopulate.call(O, O, O._tableData);
			}
		}

		/**
   * UPDATE ORDER
   * returns col index by field identifier
   * @param {string} field name of the column
   */

	}, {
		key: '_updateOrder',
		value: function _updateOrder($th) {
			var O = this;
			_log('TABLE / _updateOrder');

			var field = $th.data('field');
			var $rows = O._$tbody.find('tr').toArray();
			var col = O._getColIndexByField(field);

			// if selected item has alreay asc or desc class, just reverse contents
			if ($th.is('.asc') || $th.is('.desc')) {

				// toggle to other class
				$th.toggleClass('asc desc');

				// reverse the array
				O._$tbody.append($rows.reverse());
			}
			// otherwise perform a sort 
			else {
					// add class to header                            
					$th.addClass('asc');

					// remove asc or desc from all other headers
					$th.siblings().removeClass('asc desc');

					var fieldSorter = $th.data('sorter') || '_name';
					_log('sort by : ' + field + ' with ' + fieldSorter, 'level1');

					if (fieldSorter) {
						// fieldsorter is a function
						if ($.isFunction(fieldSorter)) {
							$rows.sort(function (a, b) {

								aa = $(a).find('td').eq(col).text(); // get text of column in row a
								bb = $(b).find('td').eq(col).text(); // get text of column in row b
								log(aa + ' / ' + bb);

								fieldSorter.call(aa, bb);
							});
						}
						// or fieldsorter is a sub routine of _sorter
						else if (_sorter && _sorter.hasOwnProperty(fieldSorter)) {
								// call sort() on rows array
								$rows.sort(function (a, b) {

									aa = $(a).find('td').eq(col).text(); // get text of column in row a
									bb = $(b).find('td').eq(col).text(); // get text of column in row b

									// call compare method
									return _sorter[fieldSorter](aa, bb);
								});
							}

						O._$tbody.append($rows);
					}
				}
		}

		/**
   * GET COL INDEX BY FIELD
   * returns col index by field identifier
   * @param {string} field name of the column
   */

	}, {
		key: '_getColIndexByField',
		value: function _getColIndexByField(field) {
			var O = this;

			var colIndex = -1;
			$.each(O.opts.cols, function (i, col) {
				if (field == col.field) {
					colIndex = i;
				}
			});

			return colIndex;
		}

		/**
   * CLEAR TABLE
   * removes table body rows
   */

	}, {
		key: '_clear',
		value: function _clear() {
			var O = this;
			_log('TABLE / _clear');

			if (O._$tbody) {
				O._$tbody.empty();
			}
		}

		/**
   * TOGGLE VIEW
   * change view of table <> card
   */

	}, {
		key: '_toggleTableView',
		value: function _toggleTableView(event) {
			var O = this;
			_log('TABLE / _toggleTableView');

			_log(O._view);
			// current state = default => set card view class
			if (O._view == 'default') {
				O._view = 'card';
				O._$table.addClass('table-view-card');
				// update toggles class
				O._$toolbar._$btnToggleView.removeClass('toggle-card').addClass('toggle-default');
			}
			// current state != default => set remove view class
			else {
					O._view = 'default';
					O._$table.removeClass('table-view-card');
					// update toggles class
					O._$toolbar._$btnToggleView.removeClass('toggle-default').addClass('toggle-card');
				}
		}

		/**
   * SELECT ROW
   * select or deselect a certain row
   * @param {dom object} $tr: jquery element 
   */

	}, {
		key: '_handleRowSelect',
		value: function _handleRowSelect($tr) {
			var O = this;
			// rows selectable and tr exists
			if (O.opts.rowSelectable && $tr) {
				// get current state
				var isSelected = $tr.data('selected');

				// already selected
				if (isSelected) {
					O._deselectRow($tr);
				} else {
					// deselect all
					if (O.opts.rowSelectable == 'single') {
						O._deselectAllRows();
						O._selectRow($tr);
					} else {
						O._selectRow($tr);
					}
				}
			}
		}

		/**
   * SELECT ROW
   * select a certain row
   * @param {dom object} $tr: jquery element 
   */

	}, {
		key: '_selectRow',
		value: function _selectRow($tr) {
			var O = this;
			_log('TABLE / _selectRow : ' + $tr.data('row-id'));

			if ($tr) {

				var rowIndex = $tr.data('row-id');
				var rowData = O._tableData[rowIndex];
				// add class marker
				$tr.addClass('tr-selected');
				$tr.data('selected', true);

				// callback on select row
				if ($.isFunction(O.opts.on.selectRow)) {
					O.opts.on.selectRow.call(O, O, rowIndex, rowData);
				}
			}
		}

		/**
   * DESELECT ROW
   * deselect a certain row
   * @param {dom object} $tr: jquery element 
   */

	}, {
		key: '_deselectRow',
		value: function _deselectRow($tr) {
			var O = this;
			_log('TABLE / _deselectRow : ' + $tr.data('row-id'));

			if ($tr) {

				var rowIndex = $tr.data('row-id');
				var rowData = O._tableData[rowIndex];

				// remove class marker
				$tr.removeClass('tr-selected');
				$tr.data('selected', false);

				// callback on de-select row
				if ($.isFunction(O.opts.on.deselectRow)) {
					O.opts.on.deselectRow.call(O, O, rowIndex, rowData);
				}
			}
		}

		/**
   * DESELECT ALL ROWS
   * deselect all rows
   */

	}, {
		key: '_deselectAllRows',
		value: function _deselectAllRows() {
			var O = this;
			_log('TABLE / _deselectAllRows');

			O._$table.find('.tr-selected').each(function (i, tr) {
				var $tr = $(tr);
				// remove class marker
				$tr.removeClass('tr-selected');
				$tr.data('selected', false);
			});
		}

		/**
   * GET SELECTD ROWS
   * deselect all rows
   * @return {object} tabledData filtered by rows selected
   */

	}, {
		key: '_getSelectedRows',
		value: function _getSelectedRows() {
			var O = this;
			_log('TABLE / _getSelectedRows');

			var selectedRows = [];

			// check all table rows
			$.each(O._tableData, function (i, row) {
				// row is currently selected and not hidden
				if (row.el.is('.tr-selected') && !row.el.is('.tr-hidden')) {
					selectedRows.push(row);
				}
			});

			return selectedRows;
		}

		/**
   * UPDATE STRIPES
   * updates stripe classes for each row
   */

	}, {
		key: '_updateStripes',
		value: function _updateStripes() {
			var O = this;
			_log('TABLE / _updateStripes');

			if (O._$table.is('.table-striped')) {

				// update stripes
				O._$tbody.find('tr:not(.tr-hidden)').each(function (i, tr) {

					var $tr = $(tr);
					$tr.removeClass('tr-odd tr-even');

					if ((i + 1) % 2 == 0) {
						$tr.addClass('tr-even');
					} else {
						$tr.addClass('tr-odd');
					}
				});
			}
		}
	}, {
		key: 'opts',
		get: function get() {
			return this._opts;
		},
		set: function set(settings) {
			this._opts = $.extend(true, {}, this.opts, settings);
		}
	}]);

	return APPTable;
}();
/*

version: 1.0.0
author: sascha obermüller
date: 06.12.2020

*/

var APPTableMT = function (_APPTable) {
	_inherits(APPTableMT, _APPTable);

	function APPTableMT(settings, $container) {
		_classCallCheck(this, APPTableMT);

		// defaults maintable
		var tableSettings = $.extend(true, {}, {
			classes: 'table-main',
			ids: {
				table: 'mtGrid',
				thead: 'mtHead',
				tbody: 'mtRows',
				filter: 'mtFilter'
			},
			rowActions: [],
			rowSelectable: false, //'multiple', // 'single', // 
			wrapper: 'card',
			on: {
				afterInit: null,
				selectRow: null,
				deselectRow: null,
				updateFilter: null
			}
		}, settings);

		return _possibleConstructorReturn(this, (APPTableMT.__proto__ || Object.getPrototypeOf(APPTableMT)).call(this, tableSettings, $container));
	}

	_createClass(APPTableMT, [{
		key: '_create',
		value: async function _create() {
			var O = this;
			_log('TABLE MAIN / _create', 'primary');

			O._facets = {}; // column facets
			O._filtered = []; // all hidden/filtered rows

			// current state of view
			O._view = 'default'; // card; //

			// loader
			O._loader = _appUI._createLoader({ classes: 'loader-page' }, O._$container);
			O._loader._setState(true);

			// get full metadata and create tabledata
			await O._createData();

			// set loader
			O._loader._setState(false);

			// callback on create
			if ($.isFunction(O.opts.on.create)) {
				O.opts.on.create.call(O, O);
			}

			if (O._tableData) {

				// create filter
				O._createFilter();

				// create table element
				O._$table = $('<table class="table table-striped table-hover table-main"></table>').appendTo(O._$container).attr('id', O.opts.ids.table);

				// wrapper
				if (O.opts.wrapper) {
					// card
					if (O.opts.wrapper == 'card') {
						O._$table.add(O._$filter).wrapAll($('<div class="card card-table-main overflow-hidden"></div>'));
						O._$table.wrap('<div class="card-body p-0"></div>');
						O._$filter.wrap('<div class="card-header p-0"></div>');
					}
				}

				// responsive table wrapper
				if (O.opts.responsive) {
					O._$table.wrap('<div class="table-responsive"></div>');
				}

				// card view toggle
				O.opts.showToggle ? O._$table.attr('data-show-toggle', O.opts.showToggle) : null; // bs table / data-show-toggle for view: table/list

				// custom table attributes
				$.each(O.opts.attributes, function (attr, val) {
					O._$table.attr(attr, val);
				});

				// toolbar
				O._createTableToolbar();

				// create table head
				O._$thead = O._createTableHead(O.opts.cols).appendTo(O._$table);

				// create table head
				O._$tbody = O._createTableBody(O._tableData).appendTo(O._$table);

				// populate table
				O._populateTable(O._tableData);

				_appUI._initTdCollapse(O._$table);

				// set counter
				O._updateFilter();
			}

			// tooltips
			_appUI._initTooltips(O._$container);
		}

		/**
   * CREATE DATA
   * create tabledata
   */

	}, {
		key: '_createData',
		value: async function _createData() {
			var O = this;
			_log('TABLE MAIN / _createData');

			O._metadata = await _fetchData._json(window._endpoints.metadata); //O._app._getMetadata();
			O._uploadDates = await _fetchData._array(window._endpoints.uploadDate, O._metadata.length); //O._app._getUploadDates( window._endpoints.uploadDate, O._metadata.length );
			O._executionTimes = await _fetchData._array(window._endpoints.executionTime, O._metadata.length); //O._app._getExecutionTimes( window._endpoints.executionTime, O._metadata.length );

			// prepare table data
			O._tableData = [];

			var _loop2 = function _loop2(i) {

				var modelMetadata = O._metadata[i]; // full metadata of model
				var rowData = {
					modelMetadata: modelMetadata, // storess full model metadata for callbacks/hooks
					cells: [], // will contain raw cell value
					el: null // will be added later in _populateTable
				}; // model data container for table output

				// create table data of models for output
				$.each(O.opts.cols, function (j, col) {

					var data = null;

					if (col.field == 'modelName') {
						data = O._getData(modelMetadata, 'generalInformation', 'name');
					} else if (col.field == 'software') {
						data = O._getData(modelMetadata, 'generalInformation', 'software');
					} else if (col.field == 'hazard') {
						data = O._getScopeData(modelMetadata, 'scope', 'hazard', 'hazardName');
						// if formatter is not list join array
						if (col.formatter != '_list') {
							data = Array.from(data).join(' ');
						}
						// let joiner = col.formatter == 'list' ? '||' ' ';
						// data = Array.from( data ).join( '' );
					} else if (col.field == 'environment') {
						data = O._getScopeData(modelMetadata, 'scope', 'product', 'productName');
						// if formatter is not list join array
						if (col.formatter != '_list') {
							data = Array.from(data).join(' ');
						}
					} else if (col.field == 'modelType') {
						data = modelMetadata['modelType'];
					} else if (col.field == 'executionTime') {
						data = O._executionTimes[i];
					} else if (col.field == 'uploadDate') {
						data = O._uploadDates[i];
					}

					rowData.cells.push(data);
				});

				O._tableData.push(rowData);
			};

			for (var i = 0; i < O._metadata.length; i++) {
				_loop2(i);
			}

			_log(O._tableData);
		}

		/**
   * CREATE FILTER
   * @param
   */

	}, {
		key: '_createFilter',
		value: function _createFilter() {
			var O = this;
			_log('TABLE MAIN / _createFilter');

			// prepare sets for filter
			O._sets = O._sets || {};
			O._sets.software = new Set();
			O._sets.environment = new Set();
			O._sets.hazard = new Set();
			O._sets.modelType = new Set();

			for (var i = 0; i < O._metadata.length; i++) {

				var _modelMetadata2 = O._metadata[i];
				var software = O._getData(_modelMetadata2, 'generalInformation', 'software');
				var environment = O._getScopeData(_modelMetadata2, 'scope', 'product', 'productName');
				var hazard = O._getScopeData(_modelMetadata2, 'scope', 'hazard', 'hazardName');
				var modelType = _modelMetadata2['modelType'];

				// update sets
				if (software) O._updateSet('software', software);
				if (environment) {
					environment.forEach(function (x) {
						O._updateSet('environment', x);
					});
				}
				if (hazard) {
					hazard.forEach(function (x) {
						O._updateSet('hazard', x);
					});
				}
				if (modelType) O._updateSet('modelType', modelType);
			}

			// create table element
			O._$filter = $('<div class="filter">').attr('id', O.opts.ids.filter).appendTo(O._$container);

			// navbar
			var $navbar = $('<nav class="navbar navbar-expand-lg row justify-content-between"></nav>').appendTo(O._$filter);

			// navbar toggle
			var $navbarToggle = $('<button class="action action-pure collapsed" type="button" data-tooltip data-toggle="collapse" data-target="#mainTableFilterFacets" aria-controls="mainTableFilter" aria-expanded="false" title="Toggle Filter" aria-label="Toggle Filter"><span class="feather icon-sliders"></span></button>');
			$navbarToggle.appendTo($navbar).wrap('<div class="navbar-toggler col-auto order-1 filter-toggler"></div>');

			// divider 1
			$navbar.append('<div class="col-divider order-2 d-block d-lg-none"></div>');

			// filter search
			O._$search = $('<input id="mainTableFilterSearch" class="form-control form-control-plaintext search-input" type="search" placeholder="Search Models" aria-label="Search Models" />').appendTo($navbar).wrap('<div class="col col-xxs-auto order-3 filter-search">').wrap('<div class="search w-100"></div>');

			// search clear button
			O._$searchClear = $('<button class="search-clear" data-clear="#mainTableFilterSearch"><i class="feather icon-x"></i></button>').insertAfter(O._$search);

			// custom search
			O._$search.on('keyup, change', function (event) {
				// get the query by updating filter
				O._updateFilter();
			});

			// divider 2
			$navbar.append('<div class="col-divider order-4 d-none d-xxs-block"></div>');

			// facets
			O._$facets = $('<div id="mainTableFilterFacets" class="collapse navbar-collapse row mt-1 mt-lg-0 facets"></div>');
			O._$facets.appendTo($navbar).wrap('<div class="col-12 col-lg-8 order-5 order-xss-6 order-lg-5 filter-facets">');
			// create all facets of cols
			$.each(O.opts.cols, function (i, col) {
				if (col.field && col.facet) {
					var $facet = $('<select class="form-control form-control-sm" style="" multiple="multiple"></select>');
					$facet.appendTo(O._$facets);

					// crate facet wrapper
					var $facetWrapper = $('<div class="col-12 col-xs-6 col-md-3 facet"></div>');
					col.facet.tooltip && col.facet.placeholder ? $facetWrapper.attr('data-tooltip', col.facet.tooltip) : null;
					col.facet.tooltip && col.facet.placeholder ? $facetWrapper.attr('title', 'Filter by ' + col.facet.placeholder) : null;

					$facet.wrap($facetWrapper);

					// set unique id
					if (col.id) {
						$facet.id = col.id + 'Facet';
					} else {
						$facet.id = 'facet-' + $.now();
					}

					// facet attributes
					$facet.attr('id', $facet.id);
					col.facet.select2 ? $facet.attr('data-sel2', col.facet.select2) : null;
					col.facet.select2SingleRow ? $facet.attr('data-sel2-choice-single-row', col.facet.select2SingleRow) : null;
					col.facet.placeholder ? $facet.attr('data-placeholder', col.facet.placeholder) : null;
					col.facet.maxSelectable ? $facet.attr('data-maximum-selection-length', col.facet.maxSelectable) : null;

					// create action on filter facet
					$facet.on('change', function (event) {
						O._updateFilter();
					});

					O._facets[col.field] = {
						el: $facet
					};

					// populate facet select
					if (col.id && O._sets[col.field]) {
						_appUI._populateSelect($facet, O._sets[col.field]);
					}
				}
			});

			// facets clear
			var $facetsClear = $('<button class="action action-pure" data-tooltip title="Clear Filter"><i class="feather icon-x"></i></button>').appendTo(O._$facets).wrap('<div class="facets-clear"></div>');

			// set clear options of facets clear
			var facetsIds = [];
			$.each(O._facets, function (i, item) {
				facetsIds.push('#' + item.el.id);
			});
			$facetsClear.attr('data-clear', facetsIds.join(','));

			// init facets functions
			_appUI._initClear(O._$navbar);
			_appUI._initSelect2(O._$facets);

			// result counter
			O._$counter = $('<small id="mainTableCounter"></small>').appendTo($navbar).wrap('<div class="col-auto align-items-center order-6 order-xxs-4 order-lg-6 mt-2 mt-xxs-0 filter-counter">');
		}

		/**
   * SET COUNTER
   * set main table result counter
   * @param {boolean} state: false=hide, true=show
   */

	}, {
		key: '_updateCounter',
		value: function _updateCounter() {
			var O = this;

			var counterText = '';

			// filtered items?
			if (O._tableData.length - O._filtered.length > 0) {
				counterText = O._filtered.length + '/' + O._tableData.length;
			} else {
				counterText = O._filtered.length;
			}
			// append default label text
			counterText += ' models';

			// set counter text to elem
			counterText && O._$counter ? O._$counter.html(counterText) : null;
		}

		/**
   * SEARCH
   * search on enpoint for query
   * @param {string} query
   */

	}, {
		key: '_search',
		value: async function _search(query) {
			var O = this;
			_log('TABLE MAIN / _search : ' + query, 'secondary');

			O._loader._setState(true);

			// fetch result from endpoint
			var result = await _fetchData._json(window._endpoints.search, query);

			O._highlight(query);

			O._loader._setState(false);

			return result;
		}

		/**
   * HIGHLIGHT
   * highlight text matching query
   * @param {string} query
   */

	}, {
		key: '_highlight',
		value: function _highlight(query) {
			var O = this;

			if (query && query.length > 0 && query != '%20') {

				$.each(O._tableData, function (rowIndex, rowData) {
					rowData.el.find('td').each(function (j, td) {
						var value = $(td).html();
						value = _formatter._searchHighlight(value, query);

						$(td).html(value);
					});
				});
			}
		}

		/**
   * UPDATE FILTER
   * updates all filter and counter
   */

	}, {
		key: '_updateFilter',
		value: async function _updateFilter() {
			var _this30 = this;

			var O = this;
			_log('TABLE MAIN / _updateFilter');

			O._filtered = []; // stores all hidden/filtered rows

			if (O._$filter) {
				// clear on update
				// remove all highlighting
				O._$table.find('mark').contents().unwrap();

				// search 
				var searchQuery = O._$search.val() == undefined || O._$search.val() == '' ? '%20' : O._$search.val().trim().toLowerCase();
				var searchResult = [];

				if (searchQuery) {
					searchResult = await O._search(searchQuery);
				}

				// check each row for matchings
				$.each(O._tableData, function (rowIndex, rowData) {

					// initale state for each row 
					// match = true : will be shown
					var rowMatchesFilter = true;

					// filter by search
					if (searchResult.length > 0 && !searchResult.includes(rowIndex)) {
						rowMatchesFilter = false;
					}

					// filter by cols
					if (rowMatchesFilter) {
						$.each(O._facets, function (field, facet) {

							var facetValue = facet.el.val();
							if ($.isArray(facetValue) && facetValue.length > 0) {

								// get according col index
								var colIndex = _get(APPTableMT.prototype.__proto__ || Object.getPrototypeOf(APPTableMT.prototype), '_getColIndexByField', _this30).call(_this30, field);
								var cellData = rowData.cells[colIndex];

								if (cellData instanceof Set) {

									var cellMatches = false;

									$.each(facetValue, function (i, val) {
										// check set for matching one of the facet values
										cellData.has(val) ? cellMatches = true : null;
									});

									if (!cellMatches) {
										rowMatchesFilter = false;
									}
								} else {
									if (!facetValue.includes(cellData)) {
										// row data does not match
										rowMatchesFilter = false;
									}
								}
							}
						});
					}

					// _log( 'row '+ rowIndex + ' > '+ rowMatchesFilter );
					if (rowMatchesFilter && !O._filtered.includes(rowIndex)) {
						O._filtered.push(rowIndex); // row matches all filter facets citeria
					}

					// apply filter class on each row
					if (rowMatchesFilter) {
						rowData.el.removeClass('tr-hidden');
					} else {
						rowData.el.addClass('tr-hidden');
					}
				});

				O._updateStripes();

				// TO DO 
				// re-populate facets with visible sets only ?

				_log(O._filtered);

				// callback
				if ($.isFunction(O.opts.on.updateFilter)) {
					O.opts.on.updateFilter.call(O, O, O._filtered);
				}

				O._updateCounter();
			}
		}

		/**
   * GET SET
   * @param {string} name: name of the set 
   */

	}, {
		key: '_getSet',
		value: function _getSet(name) {
			var O = this;
			// _log( 'TABLE MT / _getSet' );

			if (name in O._sets) {
				return O._sets.name;
			} else {
				return O._sets;
			}
		}

		/**
   * UPDATE SET
   * @param {string} name: name of the set 
   * @param {string} data: new data 
   */

	}, {
		key: '_updateSet',
		value: function _updateSet(name, data) {
			var O = this;
			// _log( 'TABLE MT / _updateSet' );

			if (name) {
				O._sets[name].add(data);
			}
		}

		/**
   * GET DATA
   * get a metadata property or return empty string if missing.
   * @param {object} modelMetadata: whole metadata of a model
   * @param {string} toplevel: name of the metadata component. it can be
   *  *generalInformation*, *scope*, *dataBackground* or *modelMath*.
   * @param {string} name: name of the metadata property 
   */

	}, {
		key: '_getData',
		value: function _getData(modelMetadata, toplevel, name) {
			var O = this;
			// _log( 'TABLE MT / _getData' );

			try {
				return modelMetadata[toplevel][name];
			} catch (err) {
				return 'no information for ' + name;
			}
		}

		/**
   * GET SCOPEDATA
   * get metadata property or return empty string if missing.
   * @param {object} modelMetadata: whole metadata of a model
   * @param {string} toplevel: name of the metadata component. It can be
   *  *generalInformation*, *scope*, *dataBackground* or *modelMath*.
   * @param {string} sublevel: Name of metadata comonent like *product*, *hazard*
   * @param {string} name: name of the metadata property 
   */

	}, {
		key: '_getScopeData',
		value: function _getScopeData(modelMetadata, toplevel, sublevel, name) {
			var O = this;
			// _log( 'TABLE MT / _getScopeData' );

			try {
				var subs = modelMetadata[toplevel][sublevel];
				names = new Set();
				subs.forEach(function (it) {
					var element = it[name];
					if (!element) element = it['name'];
					names.add(element);
				});
				return names;
			} catch (err) {
				return new Set().add('no information');
			}
		}
	}]);

	return APPTableMT;
}(APPTable);
/*

version: 1.0.0
author: sascha obermüller
date: 04.12.2020

*/

var APPUI = function () {
	function APPUI(settings) {
		_classCallCheck(this, APPUI);

		var O = this;
		O._debug = true;
		// defaults
		O._opts = $.extend(true, {}, {
			on: {
				afterInit: null
			}
		}, settings);
	}

	_createClass(APPUI, [{
		key: '_createTableHead',


		/**
   * CREATE TABLE HEAD
   * @param {array} settings
   */

		value: function _createTableHead(settings) {

			// thead
			var $thead = $('<thead></thead>');

			if (settings) {
				// thead id
				settings.id ? $thead.attr('id', settings.id) : null;

				// create th cols
				if (settings.cols) {
					$.each(settings.cols, function (i, col) {
						var $th = $('<th></th>').appendTo($thead);
						// th attributes
						col.label ? $th.html('<span>' + col.label + '</span>') : null;
						col.id ? $th.attr('id', col.id) : null; // id
						col.classes && col.classes.th ? $th.addClass(col.classes.th) : null; // classes
						col.field ? $th.attr('data-field', col.field) : null; // bs table / data-field identifier
						col.sortable ? $th.attr('data-sortable', col.sortable) : null; // bs table / data-sortable
						col.switchable ? $th.attr('data-switchable', col.switchable) : null; // bs table / data-switchable
						col.sorter ? $th.attr('data-sorter', col.sorter) : null; // bs table / data-sorter function

						// th custom attributes
						if (col.attribute && col.attributes.th) {
							$.each(col.attributes.th, function (attr, val) {
								$th.attr(attr, val);
							});
						}
					});
					$thead.wrapInner('<tr></tr>');
				}
			}
			return $thead;
		}

		/**
   * CREATE MODAL
   * @param {array} settings
   * @param {jquery selector/object} $container: append to this
   */

	}, {
		key: '_createModal',
		value: function _createModal(settings, $container) {
			var O = this;
			_log('UI / _createModal');

			return new APPModal(settings, $container);
		}

		/**
   * CREATE LOADER
   * create page loader
   * @param {array} settings
   * @param {jquery selector/object} $container: append to this
   */

	}, {
		key: '_createLoader',
		value: function _createLoader(settings, $container) {
			var O = this;
			_log('UI / _createLoader');

			var loader = {};
			loader._$el = $('<div class="loader loading"></div>').appendTo($container);
			// optional classes
			settings.classes ? loader._$el.addClass(settings.classes) : null;

			loader._setState = function (state) {
				_log('UI / loader._setState : ' + state);
				state ? loader._$el.addClass('loading') : loader._$el.removeClass('loading');
			};

			return loader;
		}

		/**
   * CREATE ALERT
   * create alert and place it into $container
   * @param {array} settings
   * @param {jquery selector/object} $container: append to this
   */

	}, {
		key: '_createAlert',
		value: function _createAlert(msg, settings, $container) {
			var O = this;
			_log('UI / _createAlert');

			var $alert = $('<div class="alert fade">' + msg + '</div>').appendTo($container);

			// alert type
			settings.type ? $alert.addClass('alert-' + settings.type) : $alert.addClass('alert-info'); // bs type: primary, secondary, success, danger, warning, info
			settings.state ? $alert.addClass(settings.state) : $alert.addClass('show'); // hide or show
			settings.classes ? $alert.addClass(settings.classes) : null;

			// dismissable
			if (settings.dismissable) {
				$alert.addClass('alert-dismissable');
				// close button
				$alert.append('<button type="button" class="close close-sm" data-dismiss="alert" aria-label="Close"><i class="feather icon-x" aria-hidden="true"></i></button>');
			}

			return $alert;
		}

		/**
   * POPULATE SELECT
   *
   * @param {element} select: dom element
   * @param {array} options: array of possible values
   */

	}, {
		key: '_populateSelect',
		value: function _populateSelect($select, options) {
			var O = this;
			if ($select) {
				options.forEach(function (entry) {
					$select.append('<option value="' + entry + '">' + entry + '</option>');
				});
			}
		}

		/**
   * POPULATE SELECT BY ID
   *
   * @param {string} selectID: element id
   * @param {array} options: array of possible values
   */

	}, {
		key: '_populateSelectByID',
		value: function _populateSelectByID(selectID, options) {
			var O = this;
			var $select = $(selectID);
			O._populateSelect($select, options);
		}

		/**
   * CREATE TOOLTIPS
   * initialize tooltips on all elements with data-attribute [data-tooltip]
   * @param {string/jquery selector} container: dom-element that contains the elements to init
   */

	}, {
		key: '_initTooltips',
		value: function _initTooltips(container) {
			var O = this;
			_log('UI / _initTooltips');

			var $elems = $(container).length > 0 ? $(container).find('[data-tooltip]') : $('[data-tooltip]');
			$elems.each(function (i, el) {

				var $el = $(el);
				// create tooltips
				$el.tooltip({
					offset: 10
				});
			});
		}

		/**
   * INIT TOGGLE TD
   * adds a collapsable container on element <td data-td-collapse>, when td's content height is higher than defined min-height
   * must be initiated before bs table init
   * @param {string/jquery selector} container: dom-element that contains the elements to init
   */

	}, {
		key: '_initTdCollapse',
		value: function _initTdCollapse($table) {
			var O = this;
			_log('UI / _initTdCollapse');
			console.log("tablooooooo ", $table);

			var minH = 100;

			var $tds = $table.find('td[data-td-collapse="true"]');

			$tds.each(function (i, td) {

				var $td = $(td); // td
				$td.wrapInner('<div></div>');

				// check for content higher than min height
				if ($td.children().outerHeight() > minH) {
					// create unique id
					var collapseId = 'tdCollapse' + jQuery.now();
					// wrap inner with collapse container
					$td.wrapInner('<div id="' + collapseId + '" class="collapse td-collapse"></div>');
					// create toggle
					var $collapseToggle = $('<a href="#" class="td-collapse-toggle collapsed" data-target="#' + collapseId + '" data-toggle="collapse" aria-expanded="false" aria-controls="' + collapseId + '"></a>').appendTo($td);
					// create collapse
					$('#' + collapseId).collapse({
						toggle: false
					});
				}
			});
		}

		/**
   * INIT SELECT2
   * initialize select2 lib on element <select data-sel2 …>
   * @param {string/jquery selector} container: dom-element that contains the elements to init
   */

	}, {
		key: '_initSelect2',
		value: function _initSelect2(container) {
			var O = this;
			_log('UI / _initSelect2');

			var $elems = $(container).length > 0 ? $(container).find('select[data-sel2]') : $('select[data-sel2]');
			$elems.each(function (i, el) {

				var $el = $(el); // select

				var select2Defaults = {
					dropdownParent: $el.parent(),
					dropdownAutoWidth: false,
					// minimumResultsForSearch	: Infinity,
					width: '100%'
				};
				// check for settings by attributes
				// select size
				if (el.hasAttribute('data-sel2-size') && $el.data('sel2-size') == 'sm' || $el.hasClass('form-control-sm') || $el.hasClass('custom-select-sm')) {
					select2Defaults.selectionCssClass = 'select2-selection--sm';
					select2Defaults.dropdownCssClass = 'select2-dropdown--sm';
				}
				// check allow clear attr
				if (el.hasAttribute('data-allow-clear') && $el.data('allow-clear') == true) {
					select2Defaults.selectionCssClass += ' select2-selection--clear';
				}
				// check custom max height attr
				if (el.hasAttribute('data-sel2-max-height')) {
					select2Defaults.selectionCssClass += ' select2-selection--max-height';
				}
				// check custom choice single row
				if (el.hasAttribute('data-sel2-choice-single-row')) {
					select2Defaults.selectionCssClass += ' select2-selection--choice-single-row';
				}
				// check max selection length attr
				if (el.hasAttribute('data-maximum-selection-length') && $el.data('maximum-selection-length') == '1') {
					select2Defaults.selectionCssClass += ' select2-selection--max-sel-1';
				}
				// create select2
				$el.select2(select2Defaults);

				// $( window ).on( 'resize', function() {
				// 	$el.select2( select2Defaults );
				// } );
			});
		}

		/**
   * INIT CLEAR
   * initialize clear function
   * data-clear attribute should contain targets for clear as jquery selector
   * @param {string/jquery selector} container: dom-element that contains the elements to init
   */

	}, {
		key: '_initClear',
		value: function _initClear(container) {
			var O = this;
			_log('UI / _initClear');

			var $elems = $(container).length > 0 ? $(container).find('[data-clear]') : $('[data-clear]');
			$elems.each(function (i, el) {

				var $clear = $(el); // button or a
				// set clear targets
				$clear.targets = $clear.data('clear');

				if ($($clear.targets)) {

					$clear.state = false;

					$($clear.targets).on('change keyup', function (event) {

						$clear.state = false;
						// check all target's state
						$.each($($clear.targets), function (j, target) {
							if ($(target).val().length > 0) {
								$clear.addClass('visible');
								$clear.state = true;
							}
						});

						if ($clear.state) {
							$clear.addClass('visible');
						} else {
							$clear.removeClass('visible');
						}
					});
					// add event to clear
					$clear.on('click', function (event) {
						// iterate all targets and reset
						$.each($($clear.targets), function (j, target) {
							var $target = $(target);
							if ($target.is('select')) {
								$target.val('').trigger('change');
							} else if ($target.is('input')) {
								$target.val('').trigger('change');
							}
						});
						// hide clear
						$clear.state = false;
						$clear.removeClass('visible');
					});
				} else {
					$clear.remove();
				}
			});
		}

		/**
   * INIT TOUCHSSPIN
   * initialize touchspin lib on element <input type="text" data-touchspin …>
   * @param {string/jquery selector} container: dom-element that contains the elements to init
   */

	}, {
		key: '_initTouchspin',
		value: function _initTouchspin(container) {
			var O = this;
			_log('UI / _initTouchspin');

			var $elems = $(container).length > 0 ? $(container).find('input[data-touchspin]') : $('input[data-touchspin]');
			$elems.each(function (i, el) {

				var $el = $(el); // input

				var touchspinDefaults = {
					buttondown_class: "btn btn-outline-secondary",
					buttonup_class: "btn btn-outline-secondary",
					decimals: 0,
					initval: 0,
					mousewheel: true,
					step: 1
				};

				// check for settings by attributes
				// min & max
				if (el.hasAttribute('data-touchspin-min')) {
					touchspinDefaults.min = $el.data('touchspin-min');
				}
				if (el.hasAttribute('data-touchspin-max')) {
					touchspinDefaults.max = $el.data('touchspin-max');
				}
				// step
				if (el.hasAttribute('data-touchspin-step')) {
					touchspinDefaults.step = $el.data('touchspin-step');
				}
				// decimals
				if (el.hasAttribute('data-touchspin-decimals')) {
					touchspinDefaults.decimals = $el.data('touchspin-decimals');
				}
				// initial value
				if (el.hasAttribute('data-touchspin-initval')) {
					touchspinDefaults.initval = $el.data('touchspin-initval');
				}
				// prefix & postfix
				if (el.hasAttribute('data-touchspin-prefix')) {
					touchspinDefaults.prefix = $el.data('touchspin-prefix');
				}
				if (el.hasAttribute('data-touchspin-postfix')) {
					touchspinDefaults.postfix = $el.data('touchspin-postfix');
				}
				// create touchspin
				$el.TouchSpin(touchspinDefaults);
			});
		}

		/**
   * INIT DATEPICKER
   * initialize jquery.datepicker lib on element <input type="text" data-datepicker …>
   * @param {array} settings: setting for datepicker
   * @param {string/jquery selector} container: dom-element that contains the elements to init
   */

	}, {
		key: '_initDatepicker',
		value: function _initDatepicker(container) {
			var O = this;
			_log('UI / _initDatepicker');

			var $elems = $(container).length > 0 ? $(container).find('[data-datepicker]') : $('[data-datepicker]');
			$elems.each(function (i, el) {

				var $el = $(el); // input or input group

				// create datepicker
				$el.datepicker({
					format: {
						toDisplay: function toDisplay(date, format, language) {
							var d = new Date(date);
							var day = d.getDate();
							var month = d.getMonth();
							var year = d.getFullYear();

							return year + '-' + month + '-' + day;
						},
						toValue: function toValue(date, format, language) {
							return date;
						}
					},
					todayHighlight: true
				});
			});
		}

		/**
   * INIT RANGESLIDER
   * initialize ion.rangeslider lib on element <input type="text" data-rangeslider …>
   * @param {string/jquery selector} container: dom-element that contains the elements to init
   */

	}, {
		key: '_initRangeslider',
		value: function _initRangeslider(container) {
			var O = this;
			_log('UI / _initRangeslider');

			var $elems = $(container).length > 0 ? $(container).find('[data-rangeslider]') : $('[data-rangeslider]');
			$elems.each(function (i, el) {

				var $el = $(el); // input

				var rangesliderDefaults = {
					drag_interval: true
				};

				// check if inputs for rangeslider are set and exist as el
				// for double slider
				if (el.hasAttribute('data-type') && $el.data('type') == 'double') {
					// from input
					if (el.hasAttribute('data-control-double-from') && $($el.data('control-double-from')).length > 0) {
						$el.$inputDoubleFrom = $($el.data('control-double-from'));
						$el.$inputDoubleFrom.on('change', function (event) {
							var min = $el.data('rangeslider').result.min;
							var to = $el.data('rangeslider').result.to;
							var val = $el.$inputDoubleFrom.prop('value');
							// validate
							if (val < min) {
								val = min;
							} else if (val > to) {
								val = to;
							}

							$el.data('ionRangeSlider').update({
								from: val
							});

							$el.$inputDoubleFrom.val(val);
						});
					}
					// to input
					if (el.hasAttribute('data-control-double-to') && $($el.data('control-double-to')).length > 0) {
						$el.$inputDoubleTo = $($el.data('control-double-to'));
						$el.$inputDoubleTo.on('change', function (event) {
							var max = $el.data('rangeslider').result.max;
							var from = $el.data('rangeslider').result.from;
							var val = $el.$inputDoubleTo.prop('value');
							// validate
							if (val < from) {
								val = from;
							} else if (val > max) {
								val = max;
							}

							$el.data('rangeslider').update({
								to: val
							});

							$el.$inputDoubleTo.val(val);
						});
					}
					// if inputs for from/to, ad update routines
					if ($el.$inputDoubleFrom || $el.$inputDoubleTo) {
						rangesliderDefaults = $.merge({
							drag_interval: false,
							onStart: function onStart(data) {
								$el.updateInputs(data);
							},
							onChange: function onChange(data) {
								$el.updateInputs(data);
							},
							onFinish: function onFinish(data) {
								$el.updateInputs(data);
							}
						}, rangesliderDefaults);

						$el.updateInputs = function (data) {
							from = data.from;
							to = data.to;

							$el.$inputDoubleFrom.prop('value', from);
							$el.$inputDoubleTo.prop('value', to);
						};
					}
				}
				// for single slider
				else {
						// to do
						// from input
						if (el.hasAttribute('data-control-single') && $($el.data('control-single')).length > 0) {
							$el.$inputSingle = $($el.data('control-single'));
							$el.$inputSingle.on('change', function (event) {
								var min = $el.data('rangeslider').result.min;
								var max = $el.data('rangeslider').result.max;
								var val = $el.$inputSingle.prop('value');
								// validate
								if (val < min) {
									val = min;
								} else if (val > max) {
									val = max;
								}

								$el.data('rangeslider').update({
									from: val
								});

								$el.$inputSingle.val(val);
							});
						}
						// if inputs for from/to, ad update routines
						if ($el.$inputSingle) {
							rangesliderDefaults = $.merge({
								drag_interval: false,
								onStart: function onStart(data) {
									$el.updateInputs(data);
								},
								onChange: function onChange(data) {
									$el.updateInputs(data);
								},
								onFinish: function onFinish(data) {
									$el.updateInputs(data);
								}
							}, rangesliderDefaults);

							$el.updateInputs = function (data) {
								from = data.from;

								$el.$inputSingle.prop('value', from);
							};
						}
					}

				// create datepicker
				$el.ionRangeSlider(rangesliderDefaults);
				$el.data('rangeslider', $el.data('ionRangeSlider'));
			});
		}

		/**
   * INIT FORM VALIDATION
   * initialize validation on forms with data-atribute [data-validate]
   * @param {string/jquery selector} container: dom-element that contains the elements to init
   */

	}, {
		key: '_initFormValidation',
		value: function _initFormValidation(container) {
			var O = this;
			_log('UI / _initFormValidation');

			var $elems = $(container).length > 0 ? $(container).find('[data-validate]') : $('[data-validate]');
			$elems.each(function (i, el) {
				var $el = $(el); // form
				var $validations = $el.find('.validate-me');
				var validation = Array.prototype.filter.call($elems, function (form) {
					form.addEventListener('submit', function (event) {
						if (form.checkValidity() === false) {
							e.preventDefault();
							e.stopPropagation();
						}
						// added validation class to all form-groups in need of validation
						$validations.each(function (j, val) {
							$(val).addClass('was-validated');
						});
					}, false);
				});
			});
		}

		/**
   * INIT FORMS
   * combined initialization external lib items 
   * - touchspin
   * - select2
   * - datepicker
   * - ion rangeslider
   */

	}, {
		key: '_initFormItems',
		value: function _initFormItems(container) {
			var O = this;
			_log('UI / _initFormItems');

			O._initClear(container);
			O._initTouchspin(container);
			O._initSelect2(container);
			O._initDatepicker(container);
			O._initRangeslider(container);
		}
	}, {
		key: '_initAll',


		/**
   * INIT All
   * combined initialization of all external lib items 
   * - touchspin
   * - select2
   * - datepicker
   * - ion rangeslider
   */

		value: function _initAll() {
			var O = this;
			_log('UI / _initAll');

			O._initClear();
			O._initTouchspin();
			O._initSelect2();
			O._initDatepicker();
			O._initRangeslider();
			O._initTable();
			O._initFormValidation();
		}
	}, {
		key: 'opts',
		get: function get() {
			return this._opts;
		},
		set: function set(settings) {
			this._opts = $.extend(true, {}, this.opts, settings);
		}
	}]);

	return APPUI;
}();

var _appUI = _appUI || new APPUI();
/*

version: 1.0.0
author: sascha obermüller
date: 04.12.2020

*/

var APPLandingpage = function () {
	function APPLandingpage(settings, $container) {
		_classCallCheck(this, APPLandingpage);

		var O = this;
		O._$container = $container;
		O._debug = true;
		// defaults
		O._opts = $.extend(true, {}, {
			header: {
				brand: {
					logo: 'assets/img/bfr_logo.gif', // false
					title: 'FSK-Web Landing Page Test' // false or ''
				},
				nav: [{
					title: 'MenuItem',
					href: '#'
				}]
			},
			mainTable: {},
			on: {
				afterInit: null
			}
		}, settings);

		// basic init actions
		O._create();
		// callback
		if ($.isFunction(O.opts.on.afterInit)) {
			O.opts.on.afterInit.call(O);
		}
	}

	_createClass(APPLandingpage, [{
		key: '_create',


		/**
   * CREATE
   * init main app
   */

		value: async function _create() {
			var O = this;
			_log('LANDINGPAGE / _create', 'primary');

			// header
			if (O.opts.header) {
				O._$header = O._createHeader(O.opts.header).appendTo(O._$container);
			}

			// endpoints defined?
			if (_typeof(window._endpoints) != (typeof undefined === 'undefined' ? 'undefined' : _typeof(undefined))) {

				// main table settings: merge with app opts
				var mtSettings = $.extend(true, {}, {
					endpoints: window._endpoints,
					data: {
						metadata: O._metadata,
						uploadDates: O._uploadDates,
						executionTimes: O._executionTimes
					}
				}, O.opts.mainTable);
				O._mainTable = new APPTableMT(mtSettings, O._$container);

				// tooltips
				_appUI._initTooltips();
			} else {
				// error no endpoints
				_appUI._createAlert('Cannot create main table, no endpoints defined', {}, O._$container);
			}
		}

		/**
   * CREATE HEADER
   * @param {array} settingss
   */

	}, {
		key: '_createHeader',
		value: function _createHeader(settings) {
			var O = this;
			_log('LANDINGPAGE / _create header');

			// header
			var $header = $('<header class="page-header"></header>');

			// navbar
			$header.navBar = $('<nav class="navbar navbar-expand">').appendTo($header);

			// header brand
			if (settings.brand) {
				$header.navBrand = $('<div class="navbar-brand"></div>').appendTo($header.navBar);

				// brand logo
				if (settings.brand.logo) {
					// logo
					$('<span class="brand-logo"><img src="' + settings.brand.logo + '" alt="" /></span>').appendTo($header.navBrand);
					// brand logo + title ? add divider 
					if (settings.brand.title) {
						$('<span class="brand-divider"></span>').appendTo($header.navBrand);
					}
				}
				// brand title
				if (settings.brand.title) {
					$('<span class="brand-typo">' + settings.brand.title + '</span>').appendTo($header.navBrand);
				}
			}

			// create header nav
			if (settings.nav && settings.nav.length > 0) {

				// nav
				$header.nav = $('<ul class="navbar-nav mt-2 mt-sm-0 ml-auto"></ul>').appendTo($header.navBar);

				// toggle
				$header.navToggle = $('<button id="menuToggle" class="action action-pure action-lg" data-toggle="dropdown" aria-expanded="false" role="button"><i class="feather icon-menu"></i></button>').appendTo($header.nav).wrap('<li class="nav-item"></li>').wrap('<div class="dropdown"></div>');

				// dropdown menu
				$header.navDropdownMenu = $('<div class="dropdown-menu dropdown-menu-right" aria-labeledby="menuToggle"></div>').insertAfter($header.navToggle);

				// dropdown items by opts
				$.each(settings.nav, function (i, link) {
					// set link target or default value _self?
					link.target = link.target ? link.target : '_self';
					// add links
					var $link = $('<a href="' + link.href + '" target="' + link.target + '" class="dropdown-item">' + link.title + '</a>').appendTo($header.navDropdownMenu);
				});
			}

			return $header;
		}
	}, {
		key: 'opts',
		get: function get() {
			return this._opts;
		},
		set: function set(settings) {
			this._opts = $.extend(true, {}, this.opts, settings);
		}
	}]);

	return APPLandingpage;
}();
