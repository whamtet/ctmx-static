var requestConfig;
var toSwap;

const mockXhr = {
  status: 200,
  getAllResponseHeaders: () => ''
};

htmx.defineExtension('static', {
  onEvent : function(name, evt) {
    if (name === 'htmx:beforeRequest') {
      const xhr = evt.detail.xhr;
      xhr.send = xhr.onload;
      evt.detail.xhr = mockXhr;
      requestConfig = evt.detail.requestConfig;
    }
    if (name === 'htmx:beforeSwap') {
      if (ctmx_static.rt.endpoint(requestConfig.path)) {
        const f = cljs.user[requestConfig.path];
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
    console.log('transform');
    return toSwap;
  }
});