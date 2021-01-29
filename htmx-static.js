var requestConfig;
var toSwap;

const mockXhr = {
  status: 200,
  getAllResponseHeaders: () => ''
};

const logRequest = () => console.log(
  requestConfig.verb.toUpperCase(),
  '/' + requestConfig.path + queryString(requestConfig.parameters)
);

htmx.defineExtension('static', {
  onEvent : function(name, evt) {
    if (name === 'htmx:beforeRequest') {
      const xhr = evt.detail.xhr;
      evt.detail.xhr = mockXhr;
      requestConfig = evt.detail.requestConfig;

      const logLoad = () => {
        logRequest();
        xhr.onload();
      };

      if (!window.ctmx_static) {
        loadCljsHtmx(logLoad);
        xhr.send = () => null;
      } else {
        xhr.send = logLoad;
      }
    }
    if (name === 'htmx:beforeSwap') {
      if (ctmx_static.rt.endpoint(requestConfig.path)) {
        const f = cljs.user[requestConfig.path.replaceAll('-', '_')];
        toSwap = ctmx_static.rt.wrap_response(requestConfig, f);
      } else {
        return false;
      }
      if (toSwap === undefined || toSwap === null) {
        return false;
      }
    }
  },
  transformResponse: function(text, xhr, elt) {
    return toSwap;
  }
});
