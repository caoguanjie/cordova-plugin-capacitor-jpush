var __extends = (this && this.__extends) || (function() {
    var extendStatics = function(d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] }
                instanceof Array && function(d, b) { d.__proto__ = b; }) ||
            function(d, b) {
                for (var p in b)
                    if (b.hasOwnProperty(p)) d[p] = b[p];
            };
        return extendStatics(d, b);
    };
    return function(d, b) {
        extendStatics(d, b);

        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __decorate = (this && this.__decorate) || function(decorators, target, key, desc) {
    var c = arguments.length,
        r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc,
        d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else
        for (var i = decorators.length - 1; i >= 0; i--)
            if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
/**
 * This is a template for new plugin wrappers
 *
 * TODO:
 * - Add/Change information below
 * - Document usage (importing, executing main functionality)
 * - Remove any imports that you are not using
 * - Remove all the comments included in this template, EXCEPT the @Plugin wrapper docs and any other docs you added
 * - Remove this note
 *
 */
import { Injectable } from '@angular/core';
import { IonicNativePlugin, cordova } from '@ionic-native/core';
var JPush = /** @class */ (function(_super) {
    __extends(JPush, _super);

    function JPush() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    JPush.prototype.functionName = function(arg1, arg2) { return cordova(this, "functionName", {}, arguments); };
    JPush.prototype.init = function() { return cordova(this, "init", { "sync": true, "platforms": ["iOS", "Android"] }, arguments); };
    JPush.prototype.setDebugMode = function(enable) { return cordova(this, "setDebugMode", { "sync": true, "platforms": ["iOS", "Android"] }, arguments); };
    JPush.prototype.getRegistrationID = function() { return cordova(this, "getRegistrationID", {}, arguments); };
    JPush.prototype.stopPush = function() { return cordova(this, "stopPush", {}, arguments); };
    JPush.prototype.resumePush = function() { return cordova(this, "resumePush", {}, arguments); };
    JPush.prototype.isPushStopped = function() { return cordova(this, "isPushStopped", {}, arguments); };
    JPush.prototype.setTags = function(params) { return cordova(this, "setTags", {}, arguments); };
    JPush.prototype.addTags = function(params) { return cordova(this, "addTags", {}, arguments); };
    JPush.prototype.deleteTags = function(params) { return cordova(this, "deleteTags", {}, arguments); };
    JPush.prototype.cleanTags = function(params) { return cordova(this, "cleanTags", {}, arguments); };
    JPush.prototype.getAllTags = function(params) { return cordova(this, "getAllTags", {}, arguments); };
    JPush.prototype.checkTagBindState = function(params) { return cordova(this, "checkTagBindState", {}, arguments); };
    JPush.prototype.setAlias = function(params) { return cordova(this, "setAlias", {}, arguments); };
    JPush.prototype.deleteAlias = function(params) { return cordova(this, "deleteAlias", {}, arguments); };
    JPush.prototype.getAlias = function(params) { return cordova(this, "getAlias", {}, arguments); };
    JPush.prototype.getUserNotificationSettings = function() { return cordova(this, "getUserNotificationSettings", {}, arguments); };
    JPush.prototype.clearLocalNotifications = function() { return cordova(this, "clearLocalNotifications", {}, arguments); };
    JPush.prototype.setBadge = function(badge) { return cordova(this, "setBadge", { "sync": true, "platforms": ["iOS"] }, arguments); };
    JPush.prototype.resetBadge = function() { return cordova(this, "resetBadge", { "sync": true, "platforms": ["iOS"] }, arguments); };
    JPush.prototype.setApplicationIconBadgeNumber = function(badge) { return cordova(this, "setApplicationIconBadgeNumber", { "sync": true, "platforms": ["iOS"] }, arguments); };
    JPush.prototype.getApplicationIconBadgeNumber = function() { return cordova(this, "getApplicationIconBadgeNumber", {}, arguments); };
    JPush.prototype.addLocalNotificationForIOS = function(delayTime, content, badge, identifierKey, extras) { return cordova(this, "addLocalNotificationForIOS", { "sync": true, "platforms": ["iOS"] }, arguments); };
    JPush.prototype.deleteLocalNotificationWithIdentifierKeyInIOS = function(identifierKey) { return cordova(this, "deleteLocalNotificationWithIdentifierKeyInIOS", { "sync": true, "platforms": ["iOS"] }, arguments); };
    JPush.prototype.addDismissActions = function(actions, categoryId) { return cordova(this, "addDismissActions", { "sync": true, "platforms": ["iOS"] }, arguments); };
    JPush.prototype.addNotificationActions = function(actions, categoryId) { return cordova(this, "addNotificationActions", { "sync": true, "platforms": ["iOS"] }, arguments); };
    JPush.prototype.setLocation = function(latitude, longitude) { return cordova(this, "setLocation", { "sync": true, "platforms": ["iOS"] }, arguments); };
    JPush.prototype.startLogPageView = function(pageName) { return cordova(this, "startLogPageView", { "sync": true, "platforms": ["iOS"] }, arguments); };
    JPush.prototype.stopLogPageView = function(pageName) { return cordova(this, "stopLogPageView", { "sync": true, "platforms": ["iOS"] }, arguments); };
    JPush.prototype.beginLogPageView = function(pageName, duration) { return cordova(this, "beginLogPageView", { "sync": true, "platforms": ["iOS"] }, arguments); };
    JPush.prototype.getConnectionState = function() { return cordova(this, "getConnectionState", {}, arguments); };
    JPush.prototype.setBasicPushNotificationBuilder = function() { return cordova(this, "setBasicPushNotificationBuilder", {}, arguments); };
    JPush.prototype.setCustomPushNotificationBuilder = function() { return cordova(this, "setCustomPushNotificationBuilder", {}, arguments); };
    JPush.prototype.clearAllNotification = function() { return cordova(this, "clearAllNotification", {}, arguments); };
    JPush.prototype.clearNotificationById = function(id) { return cordova(this, "clearNotificationById", {}, arguments); };
    JPush.prototype.setLatestNotificationNum = function(num) { return cordova(this, "setLatestNotificationNum", {}, arguments); };
    JPush.prototype.addLocalNotification = function(builderId, content, title, notificationId, broadcastTime, extras) { return cordova(this, "addLocalNotification", {}, arguments); };
    JPush.prototype.removeLocalNotification = function(notificationId) { return cordova(this, "removeLocalNotification", {}, arguments); };
    JPush.prototype.reportNotificationOpened = function(msgId) { return cordova(this, "reportNotificationOpened", {}, arguments); };
    JPush.prototype.requestPermission = function() { return cordova(this, "requestPermission", {}, arguments); };
    JPush.prototype.setSilenceTime = function(startHour, startMinute, endHour, endMinute) { return cordova(this, "setSilenceTime", {}, arguments); };
    JPush.prototype.setPushTime = function(weekdays, startHour, endHour) { return cordova(this, "setPushTime", {}, arguments); };
    JPush.pluginName = "JPush";
    JPush.plugin = "cordova-plugin-capacitor-jpush.git";
    JPush.pluginRef = "plugins.jPushPlugin";
    JPush.repo = "https://github.com/caoguanjie/cordova-plugin-capacitor-jpush.git";
    JPush.install = "ionic cordova plugin add cordova-plugin-capacitor-jpush.git --variable APP_KEY=your_app_key";
    JPush.installVariables = ["APP_KEY"];
    JPush.platforms = ["Android", "iOS"];
    JPush = __decorate([
        Injectable({
            providedIn: 'root'
        })
    ], JPush);
    return JPush;
}(IonicNativePlugin));
export { JPush };
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiaW5kZXguanMiLCJzb3VyY2VSb290IjoiIiwic291cmNlcyI6WyIuLi8uLi8uLi8uLi8uLi9zcmMvQGlvbmljLW5hdGl2ZS9wbHVnaW5zL2pwdXNoL25neC9pbmRleC50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7Ozs7Ozs7O0FBQUE7Ozs7Ozs7Ozs7R0FVRztBQUNILE9BQU8sRUFBRSxVQUFVLEVBQUUsTUFBTSxlQUFlLENBQUM7QUFDM0MsT0FBTyw4QkFBMEYsTUFBTSxvQkFBb0IsQ0FBQzs7SUF1RGpHLHlCQUFpQjs7OztJQVMxQyw0QkFBWSxhQUFDLElBQVksRUFBRSxJQUFZO0lBUXZDLG9CQUFJO0lBTUosNEJBQVksYUFBQyxNQUFlO0lBRzVCLGlDQUFpQjtJQUdqQix3QkFBUTtJQUdSLDBCQUFVO0lBR1YsNkJBQWE7SUFHYix1QkFBTyxhQUFDLE1BQWtCO0lBRzFCLHVCQUFPLGFBQUMsTUFBa0I7SUFHMUIsMEJBQVUsYUFBQyxNQUFrQjtJQUc3Qix5QkFBUyxhQUFDLE1BQWtCO0lBRzVCLDBCQUFVLGFBQUMsTUFBa0I7SUFNN0IsaUNBQWlCLGFBQUMsTUFBYztJQUdoQyx3QkFBUSxhQUFDLE1BQW9CO0lBRzdCLDJCQUFXLGFBQUMsTUFBb0I7SUFHaEMsd0JBQVEsYUFBQyxNQUFvQjtJQWU3QiwyQ0FBMkI7SUFHM0IsdUNBQXVCO0lBUXZCLHdCQUFRLGFBQUMsS0FBYTtJQU10QiwwQkFBVTtJQU1WLDZDQUE2QixhQUFDLEtBQWE7SUFHM0MsNkNBQTZCO0lBTTdCLDBDQUEwQixhQUFDLFNBQWlCLEVBQUUsT0FBZSxFQUFFLEtBQWEsRUFBRSxhQUFxQixFQUFFLE1BQWU7SUFNcEgsNkRBQTZDLGFBQUMsYUFBcUI7SUFNbkUsaUNBQWlCLGFBQUMsT0FBc0IsRUFBRSxVQUFrQjtJQU01RCxzQ0FBc0IsYUFBQyxPQUFzQixFQUFFLFVBQWtCO0lBTWpFLDJCQUFXLGFBQUMsUUFBZ0IsRUFBRSxTQUFpQjtJQU0vQyxnQ0FBZ0IsYUFBQyxRQUFnQjtJQU1qQywrQkFBZSxhQUFDLFFBQWdCO0lBTWhDLGdDQUFnQixhQUFDLFFBQWdCLEVBQUUsUUFBZ0I7SUFPbkQsa0NBQWtCO0lBR2xCLCtDQUErQjtJQUcvQixnREFBZ0M7SUFHaEMsb0NBQW9CO0lBR3BCLHFDQUFxQixhQUFDLEVBQVU7SUFHaEMsd0NBQXdCLGFBQUMsR0FBVztJQUdwQyxvQ0FBb0IsYUFBQyxTQUFpQixFQUFFLE9BQWUsRUFBRSxLQUFhLEVBQUUsY0FBc0IsRUFBRSxhQUFxQixFQUFFLE1BQWU7SUFHdEksdUNBQXVCLGFBQUMsY0FBc0I7SUFHOUMsd0NBQXdCLGFBQUMsS0FBYTtJQUd0QyxpQ0FBaUI7SUFHakIsOEJBQWMsYUFBQyxTQUFpQixFQUFFLFdBQW1CLEVBQUUsT0FBZSxFQUFFLFNBQWlCO0lBR3pGLDJCQUFXLGFBQUMsUUFBdUIsRUFBRSxTQUFpQixFQUFFLE9BQWU7Ozs7Ozs7O0lBbE01RCxLQUFLO1FBSGpCLFVBQVUsQ0FBQztZQUNWLFVBQVUsRUFBRSxNQUFNO1NBQ25CLENBQUM7T0FDVyxLQUFLO2dCQW5FbEI7RUFtRTJCLGlCQUFpQjtTQUEvQixLQUFLIiwic291cmNlc0NvbnRlbnQiOlsiLyoqXG4gKiBUaGlzIGlzIGEgdGVtcGxhdGUgZm9yIG5ldyBwbHVnaW4gd3JhcHBlcnNcbiAqXG4gKiBUT0RPOlxuICogLSBBZGQvQ2hhbmdlIGluZm9ybWF0aW9uIGJlbG93XG4gKiAtIERvY3VtZW50IHVzYWdlIChpbXBvcnRpbmcsIGV4ZWN1dGluZyBtYWluIGZ1bmN0aW9uYWxpdHkpXG4gKiAtIFJlbW92ZSBhbnkgaW1wb3J0cyB0aGF0IHlvdSBhcmUgbm90IHVzaW5nXG4gKiAtIFJlbW92ZSBhbGwgdGhlIGNvbW1lbnRzIGluY2x1ZGVkIGluIHRoaXMgdGVtcGxhdGUsIEVYQ0VQVCB0aGUgQFBsdWdpbiB3cmFwcGVyIGRvY3MgYW5kIGFueSBvdGhlciBkb2NzIHlvdSBhZGRlZFxuICogLSBSZW1vdmUgdGhpcyBub3RlXG4gKlxuICovXG5pbXBvcnQgeyBJbmplY3RhYmxlIH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyBQbHVnaW4sIENvcmRvdmEsIENvcmRvdmFQcm9wZXJ0eSwgQ29yZG92YUluc3RhbmNlLCBJbnN0YW5jZVByb3BlcnR5LCBJb25pY05hdGl2ZVBsdWdpbiB9IGZyb20gJ0Bpb25pYy1uYXRpdmUvY29yZSc7XG5pbXBvcnQgeyBPYnNlcnZhYmxlIH0gZnJvbSAncnhqcyc7XG5cbmV4cG9ydCBpbnRlcmZhY2UgVGFnT3B0aW9ucyB7XG4gIHNlcXVlbmNlOiBudW1iZXI7XG4gIHRhZ3M/OiBBcnJheTxzdHJpbmc+O1xufVxuXG5leHBvcnQgaW50ZXJmYWNlIEFsaWFzT3B0aW9ucyB7XG4gIHNlcXVlbmNlOiBudW1iZXI7XG4gIGFsaWFzPzogc3RyaW5nO1xufVxuXG4vKipcbiAqIEBuYW1lIGpwdXNoXG4gKiBAZGVzY3JpcHRpb25cbiAqIFRoaXMgcGx1Z2luIGRvZXMgc29tZXRoaW5nXG4gKlxuICogQHVzYWdlXG4gKiBgYGB0eXBlc2NyaXB0XG4gKiBpbXBvcnQgeyBqcHVzaCB9IGZyb20gJ0Bpb25pYy1uYXRpdmUvanB1c2gnO1xuICpcbiAqXG4gKiBjb25zdHJ1Y3Rvcihwcml2YXRlIGpwdXNoOiBqcHVzaCkgeyB9XG4gKlxuICogLi4uXG4gKlxuICpcbiAqIHRoaXMuanB1c2guZnVuY3Rpb25OYW1lKCdIZWxsbycsIDEyMylcbiAqICAgLnRoZW4oKHJlczogYW55KSA9PiBjb25zb2xlLmxvZyhyZXMpKVxuICogICAuY2F0Y2goKGVycm9yOiBhbnkpID0+IGNvbnNvbGUuZXJyb3IoZXJyb3IpKTtcbiAqXG4gKiBgYGBcbiAqL1xuLy8gQFBsdWdpbih7XG4vLyAgIHBsdWdpbk5hbWU6ICdqcHVzaCcsXG4vLyAgIHBsdWdpbjogJycsIC8vIG5wbSBwYWNrYWdlIG5hbWUsIGV4YW1wbGU6IGNvcmRvdmEtcGx1Z2luLWNhbWVyYVxuLy8gICBwbHVnaW5SZWY6ICcnLCAvLyB0aGUgdmFyaWFibGUgcmVmZXJlbmNlIHRvIGNhbGwgdGhlIHBsdWdpbiwgZXhhbXBsZTogbmF2aWdhdG9yLmdlb2xvY2F0aW9uXG4vLyAgIHJlcG86ICcnLCAvLyB0aGUgZ2l0aHViIHJlcG9zaXRvcnkgVVJMIGZvciB0aGUgcGx1Z2luXG4vLyAgIGluc3RhbGw6ICcnLCAvLyBPUFRJT05BTCBpbnN0YWxsIGNvbW1hbmQsIGluIGNhc2UgdGhlIHBsdWdpbiByZXF1aXJlcyB2YXJpYWJsZXNcbi8vICAgaW5zdGFsbFZhcmlhYmxlczogW10sIC8vIE9QVElPTkFMIHRoZSBwbHVnaW4gcmVxdWlyZXMgdmFyaWFibGVzXG4vLyAgIHBsYXRmb3JtczogW10gLy8gQXJyYXkgb2YgcGxhdGZvcm1zIHN1cHBvcnRlZCwgZXhhbXBsZTogWydBbmRyb2lkJywgJ2lPUyddXG4vLyB9KVxuQFBsdWdpbih7XG4gIHBsdWdpbk5hbWU6ICdKUHVzaCcsXG4gIHBsdWdpbjogJ2pwdXNoLXBob25lZ2FwLXBsdWdpbicsXG4gIHBsdWdpblJlZjogJ3BsdWdpbnMualB1c2hQbHVnaW4nLFxuICByZXBvOiAnaHR0cHM6Ly9naXRodWIuY29tL2pwdXNoL2pwdXNoLXBob25lZ2FwLXBsdWdpbicsXG4gIGluc3RhbGw6ICdpb25pYyBjb3Jkb3ZhIHBsdWdpbiBhZGQganB1c2gtcGhvbmVnYXAtcGx1Z2luIC0tdmFyaWFibGUgQVBQX0tFWT15b3VyX2FwcF9rZXknLFxuICBpbnN0YWxsVmFyaWFibGVzOiBbJ0FQUF9LRVknXSxcbiAgcGxhdGZvcm1zOiBbJ0FuZHJvaWQnLCAnaU9TJ11cbn0pXG5ASW5qZWN0YWJsZSh7XG4gIHByb3ZpZGVkSW46ICdyb290J1xufSlcbmV4cG9ydCBjbGFzcyBKUHVzaCBleHRlbmRzIElvbmljTmF0aXZlUGx1Z2luIHtcblxuICAvKipcbiAgICogVGhpcyBmdW5jdGlvbiBkb2VzIHNvbWV0aGluZ1xuICAgKiBAcGFyYW0gYXJnMSB7c3RyaW5nfSBTb21lIHBhcmFtIHRvIGNvbmZpZ3VyZSBzb21ldGhpbmdcbiAgICogQHBhcmFtIGFyZzIge251bWJlcn0gQW5vdGhlciBwYXJhbSB0byBjb25maWd1cmUgc29tZXRoaW5nXG4gICAqIEByZXR1cm4ge1Byb21pc2U8YW55Pn0gUmV0dXJucyBhIHByb21pc2UgdGhhdCByZXNvbHZlcyB3aGVuIHNvbWV0aGluZyBoYXBwZW5zXG4gICAqL1xuICBAQ29yZG92YSgpXG4gIGZ1bmN0aW9uTmFtZShhcmcxOiBzdHJpbmcsIGFyZzI6IG51bWJlcik6IFByb21pc2U8YW55PiB7XG4gICAgcmV0dXJuOyAvLyBXZSBhZGQgcmV0dXJuOyBoZXJlIHRvIGF2b2lkIGFueSBJREUgLyBDb21waWxlciBlcnJvcnNcbiAgfVxuXG4gIEBDb3Jkb3ZhKHtcbiAgICBzeW5jOiB0cnVlLFxuICAgIHBsYXRmb3JtczogWydpT1MnLCAnQW5kcm9pZCddXG4gICB9KVxuICBpbml0KCk6IHZvaWQgeyAgfVxuXG4gIEBDb3Jkb3ZhKHtcbiAgICBzeW5jOiB0cnVlLFxuICAgIHBsYXRmb3JtczogWydpT1MnLCAnQW5kcm9pZCddXG4gICB9KVxuICBzZXREZWJ1Z01vZGUoZW5hYmxlOiBib29sZWFuKTogdm9pZCB7ICB9XG5cbiAgQENvcmRvdmEoKVxuICBnZXRSZWdpc3RyYXRpb25JRCgpOiBQcm9taXNlPGFueT4geyByZXR1cm47IH1cblxuICBAQ29yZG92YSgpXG4gIHN0b3BQdXNoKCk6IFByb21pc2U8YW55PiB7IHJldHVybjsgfVxuXG4gIEBDb3Jkb3ZhKClcbiAgcmVzdW1lUHVzaCgpOiBQcm9taXNlPGFueT4geyByZXR1cm47IH1cblxuICBAQ29yZG92YSgpXG4gIGlzUHVzaFN0b3BwZWQoKTogUHJvbWlzZTxhbnk+IHsgcmV0dXJuOyB9XG5cbiAgQENvcmRvdmEoKVxuICBzZXRUYWdzKHBhcmFtczogVGFnT3B0aW9ucyk6IFByb21pc2U8YW55PiB7IHJldHVybjsgfVxuXG4gIEBDb3Jkb3ZhKClcbiAgYWRkVGFncyhwYXJhbXM6IFRhZ09wdGlvbnMpOiBQcm9taXNlPGFueT4geyByZXR1cm47IH1cblxuICBAQ29yZG92YSgpXG4gIGRlbGV0ZVRhZ3MocGFyYW1zOiBUYWdPcHRpb25zKTogUHJvbWlzZTxhbnk+IHsgcmV0dXJuOyB9XG5cbiAgQENvcmRvdmEoKVxuICBjbGVhblRhZ3MocGFyYW1zOiBUYWdPcHRpb25zKTogUHJvbWlzZTxhbnk+IHsgcmV0dXJuOyB9XG5cbiAgQENvcmRvdmEoKVxuICBnZXRBbGxUYWdzKHBhcmFtczogVGFnT3B0aW9ucyk6IFByb21pc2U8YW55PiB7IHJldHVybjsgfVxuXG4gIC8qKlxuICAgKiBAcGFyYW0gcGFyYW1zIHsgc2VxdWVuY2U6IG51bWJlciwgdGFnOiBzdHJpbmcgfVxuICAgKi9cbiAgQENvcmRvdmEoKVxuICBjaGVja1RhZ0JpbmRTdGF0ZShwYXJhbXM6IG9iamVjdCk6IFByb21pc2U8YW55PiB7IHJldHVybjsgfVxuXG4gIEBDb3Jkb3ZhKClcbiAgc2V0QWxpYXMocGFyYW1zOiBBbGlhc09wdGlvbnMpOiBQcm9taXNlPGFueT4geyByZXR1cm47IH1cblxuICBAQ29yZG92YSgpXG4gIGRlbGV0ZUFsaWFzKHBhcmFtczogQWxpYXNPcHRpb25zKTogUHJvbWlzZTxhbnk+IHsgcmV0dXJuOyB9XG5cbiAgQENvcmRvdmEoKVxuICBnZXRBbGlhcyhwYXJhbXM6IEFsaWFzT3B0aW9ucyk6IFByb21pc2U8YW55PiB7IHJldHVybjsgfVxuXG4gIC8qKlxuICAgKiBEZXRlcm1pbmF0ZSB3aGV0aGVyIHRoZSBhcHBsaWNhdGlvbiBub3RpZmljYXRpb24gaGFzIGJlZW4gb3BlbmVkLlxuICAgKiBcbiAgICogaU9TOiAwOiBjbG9zZWQ7ID4xOiBvcGVuZWQuXG4gICAqICBVSVJlbW90ZU5vdGlmaWNhdGlvblR5cGVOb25lID0gMCxcbiAgICogIFVJUmVtb3RlTm90aWZpY2F0aW9uVHlwZUJhZGdlID0gMSA8PCAwLFxuICAgKiAgVUlSZW1vdGVOb3RpZmljYXRpb25UeXBlU291bmQgPSAxIDw8IDEsXG4gICAqICBVSVJlbW90ZU5vdGlmaWNhdGlvblR5cGVBbGVydCA9IDEgPDwgMixcbiAgICogIFVJUmVtb3RlTm90aWZpY2F0aW9uVHlwZU5ld3NzdGFuZENvbnRlbnRBdmFpbGFiaWxpdHkgPSAxIDw8IDNcbiAgICogXG4gICAqIEFuZHJvaWQ6IDA6IGNsb3NlZDsgMTogb3BlbmVkLlxuICAgKi9cbiAgQENvcmRvdmEoKVxuICBnZXRVc2VyTm90aWZpY2F0aW9uU2V0dGluZ3MoKTogUHJvbWlzZTxhbnk+IHsgcmV0dXJuOyB9XG5cbiAgQENvcmRvdmEoKVxuICBjbGVhckxvY2FsTm90aWZpY2F0aW9ucygpOiBQcm9taXNlPGFueT4geyByZXR1cm47IH1cblxuICAvLyBpT1MgQVBJIC0gc3RhcnRcblxuICBAQ29yZG92YSh7XG4gICAgc3luYzogdHJ1ZSxcbiAgICBwbGF0Zm9ybXM6IFsnaU9TJ11cbiAgIH0pXG4gIHNldEJhZGdlKGJhZGdlOiBudW1iZXIpOiB2b2lkIHsgIH1cblxuICBAQ29yZG92YSh7XG4gICAgc3luYzogdHJ1ZSxcbiAgICBwbGF0Zm9ybXM6IFsnaU9TJ11cbiAgIH0pXG4gIHJlc2V0QmFkZ2UoKTogdm9pZCB7ICB9XG5cbiAgQENvcmRvdmEoe1xuICAgIHN5bmM6IHRydWUsXG4gICAgcGxhdGZvcm1zOiBbJ2lPUyddXG4gICB9KVxuICBzZXRBcHBsaWNhdGlvbkljb25CYWRnZU51bWJlcihiYWRnZTogbnVtYmVyKTogdm9pZCB7ICB9XG5cbiAgQENvcmRvdmEoKVxuICBnZXRBcHBsaWNhdGlvbkljb25CYWRnZU51bWJlcigpOiBQcm9taXNlPGFueT4geyByZXR1cm47IH1cblxuICBAQ29yZG92YSh7XG4gICAgc3luYzogdHJ1ZSxcbiAgICBwbGF0Zm9ybXM6IFsnaU9TJ11cbiAgIH0pXG4gIGFkZExvY2FsTm90aWZpY2F0aW9uRm9ySU9TKGRlbGF5VGltZTogbnVtYmVyLCBjb250ZW50OiBzdHJpbmcsIGJhZGdlOiBudW1iZXIsIGlkZW50aWZpZXJLZXk6IHN0cmluZywgZXh0cmFzPzogb2JqZWN0KTogdm9pZCB7ICB9XG5cbiAgQENvcmRvdmEoe1xuICAgIHN5bmM6IHRydWUsXG4gICAgcGxhdGZvcm1zOiBbJ2lPUyddXG4gICB9KVxuICBkZWxldGVMb2NhbE5vdGlmaWNhdGlvbldpdGhJZGVudGlmaWVyS2V5SW5JT1MoaWRlbnRpZmllcktleTogc3RyaW5nKTogdm9pZCB7ICB9XG5cbiAgQENvcmRvdmEoe1xuICAgIHN5bmM6IHRydWUsXG4gICAgcGxhdGZvcm1zOiBbJ2lPUyddXG4gICB9KVxuICBhZGREaXNtaXNzQWN0aW9ucyhhY3Rpb25zOiBBcnJheTxvYmplY3Q+LCBjYXRlZ29yeUlkOiBzdHJpbmcpOiB2b2lkIHsgIH1cblxuICBAQ29yZG92YSh7XG4gICAgc3luYzogdHJ1ZSxcbiAgICBwbGF0Zm9ybXM6IFsnaU9TJ11cbiAgIH0pXG4gIGFkZE5vdGlmaWNhdGlvbkFjdGlvbnMoYWN0aW9uczogQXJyYXk8b2JqZWN0PiwgY2F0ZWdvcnlJZDogc3RyaW5nKTogdm9pZCB7ICB9XG5cbiAgQENvcmRvdmEoe1xuICAgIHN5bmM6IHRydWUsXG4gICAgcGxhdGZvcm1zOiBbJ2lPUyddXG4gICB9KVxuICBzZXRMb2NhdGlvbihsYXRpdHVkZTogbnVtYmVyLCBsb25naXR1ZGU6IG51bWJlcik6IHZvaWQgeyAgfVxuXG4gIEBDb3Jkb3ZhKHtcbiAgICBzeW5jOiB0cnVlLFxuICAgIHBsYXRmb3JtczogWydpT1MnXVxuICAgfSlcbiAgc3RhcnRMb2dQYWdlVmlldyhwYWdlTmFtZTogc3RyaW5nKTogdm9pZCB7IHJldHVybjsgfVxuXG4gIEBDb3Jkb3ZhKHtcbiAgICBzeW5jOiB0cnVlLFxuICAgIHBsYXRmb3JtczogWydpT1MnXVxuICAgfSlcbiAgc3RvcExvZ1BhZ2VWaWV3KHBhZ2VOYW1lOiBzdHJpbmcpOiB2b2lkIHsgcmV0dXJuOyB9XG5cbiAgQENvcmRvdmEoe1xuICAgIHN5bmM6IHRydWUsXG4gICAgcGxhdGZvcm1zOiBbJ2lPUyddXG4gICB9KVxuICBiZWdpbkxvZ1BhZ2VWaWV3KHBhZ2VOYW1lOiBzdHJpbmcsIGR1cmF0aW9uOiBudW1iZXIpOiB2b2lkIHsgcmV0dXJuOyB9XG5cbiAgLy8gaU9TIEFQSSAtIGVuZFxuXG4gIC8vIEFuZHJvaWQgQVBJIC0gc3RhcnRcblxuICBAQ29yZG92YSgpXG4gIGdldENvbm5lY3Rpb25TdGF0ZSgpOiBQcm9taXNlPGFueT4geyByZXR1cm47IH1cblxuICBAQ29yZG92YSgpXG4gIHNldEJhc2ljUHVzaE5vdGlmaWNhdGlvbkJ1aWxkZXIoKTogUHJvbWlzZTxhbnk+IHsgcmV0dXJuOyB9XG5cbiAgQENvcmRvdmEoKVxuICBzZXRDdXN0b21QdXNoTm90aWZpY2F0aW9uQnVpbGRlcigpOiBQcm9taXNlPGFueT4geyByZXR1cm47IH1cblxuICBAQ29yZG92YSgpXG4gIGNsZWFyQWxsTm90aWZpY2F0aW9uKCk6IFByb21pc2U8YW55PiB7IHJldHVybjsgfVxuXG4gIEBDb3Jkb3ZhKClcbiAgY2xlYXJOb3RpZmljYXRpb25CeUlkKGlkOiBudW1iZXIpOiBQcm9taXNlPGFueT4geyByZXR1cm47IH1cblxuICBAQ29yZG92YSgpXG4gIHNldExhdGVzdE5vdGlmaWNhdGlvbk51bShudW06IG51bWJlcik6IFByb21pc2U8YW55PiB7IHJldHVybjsgfVxuXG4gIEBDb3Jkb3ZhKClcbiAgYWRkTG9jYWxOb3RpZmljYXRpb24oYnVpbGRlcklkOiBudW1iZXIsIGNvbnRlbnQ6IHN0cmluZywgdGl0bGU6IHN0cmluZywgbm90aWZpY2F0aW9uSWQ6IG51bWJlciwgYnJvYWRjYXN0VGltZTogbnVtYmVyLCBleHRyYXM/OiBzdHJpbmcpOiBQcm9taXNlPGFueT4geyByZXR1cm47IH1cblxuICBAQ29yZG92YSgpXG4gIHJlbW92ZUxvY2FsTm90aWZpY2F0aW9uKG5vdGlmaWNhdGlvbklkOiBudW1iZXIpOiBQcm9taXNlPGFueT4geyByZXR1cm47IH1cblxuICBAQ29yZG92YSgpXG4gIHJlcG9ydE5vdGlmaWNhdGlvbk9wZW5lZChtc2dJZDogbnVtYmVyKTogUHJvbWlzZTxhbnk+IHsgcmV0dXJuOyB9XG5cbiAgQENvcmRvdmEoKVxuICByZXF1ZXN0UGVybWlzc2lvbigpOiBQcm9taXNlPGFueT4geyByZXR1cm47IH1cblxuICBAQ29yZG92YSgpXG4gIHNldFNpbGVuY2VUaW1lKHN0YXJ0SG91cjogbnVtYmVyLCBzdGFydE1pbnV0ZTogbnVtYmVyLCBlbmRIb3VyOiBudW1iZXIsIGVuZE1pbnV0ZTogbnVtYmVyKTogUHJvbWlzZTxhbnk+IHsgcmV0dXJuOyB9XG5cbiAgQENvcmRvdmEoKVxuICBzZXRQdXNoVGltZSh3ZWVrZGF5czogQXJyYXk8c3RyaW5nPiwgc3RhcnRIb3VyOiBudW1iZXIsIGVuZEhvdXI6IG51bWJlcik6IFByb21pc2U8YW55PiB7IHJldHVybjsgfVxuXG4gIC8vIEFuZHJvaWQgQVBJIC0gZW5kXG5cbn1cbiJdfQ==