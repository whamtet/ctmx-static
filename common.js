htmx.config.defaultSettleDelay = 0;
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

const editorialize = id => {

  const editor = ace.edit(id);
  editor.setTheme("ace/theme/tomorrow_night_bright");
  editor.session.setMode("ace/mode/clojure");
  editor.setOption('showGutter', false);

  const sendSession = () => ctmx_static.eval.eval_endpoints(editor.getSession().getValue());
  const sendSessionDelay = delay(sendSession);
  editor.getSession().on('change', sendSessionDelay);

  return editor;

};

var editors = [];
const edAppend = s => editors.push(editorialize(s));

const prod = () => location.href = location.href.replace('http://localhost:8000', 'https://ctmx.info');
const dev = () => location.href = location.href.replace('https://ctmx.info', 'http://localhost:8000');
