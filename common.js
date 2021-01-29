htmx.config.defaultSwapStyle = 'outerHTML';

const delay = f => {
  var pendingDelay;
  return () => {
    if (pendingDelay) {
      clearTimeout(pendingDelay);
    }
    pendingDelay = setTimeout(f, 1000);
  };
};

const loadScript = (src, onload) => {
  // Create new script element
  const script = document.createElement('script');
  script.src = src;
  script.onload = onload;
  document.head.appendChild(script);
}

var cljsSrc;
const editors = [];

const loadCljsHtmx = (onload) => {
  loadScript(cljsSrc, () => {
    ctmx_static.eval.init(false);
    onload();
  });
};

var hasFocussed = false;
const editorFocus = () => {
  if (!hasFocussed) {
    hasFocussed = true;
    if (cljsSrc) {
      loadScript(cljsSrc, () => ctmx_static.eval.init(false));
    }
  }
}

const editorialize = id => {

  const editor = ace.edit(id);
  editor.setTheme("ace/theme/tomorrow_night_bright");
  editor.session.setMode("ace/mode/clojure");
  editor.setOption('showGutter', false);

  const sendSession = () => ctmx_static.eval.eval_endpoints(editor.getSession().getValue());
  const sendSessionDelay = delay(sendSession);
  editor.on('focus', editorFocus);
  editor.getSession().on('change', sendSessionDelay);

  return editor;

};

const edAppend = id => editors.push(editorialize(id));

const prod = () => location.href = location.href.replace('http://localhost:8000', 'https://ctmx.info').replace('dist/', '');
const dev = () => location.href = location.href.replace('https://ctmx.info', 'http://localhost:8000');

const setSrc = (src) => cljsSrc = src;
