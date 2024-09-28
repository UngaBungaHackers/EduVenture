const WebSocket = require('ws');
const sqlite3 = require('sqlite3').verbose() //verbose provides more detailed stack trace
const db = new sqlite3.Database('./data/projectData')
const wss = new WebSocket.Server({ port: 8080 });

//authenticate user and password based off sql database
function authenticate(user, password) {
    var authorized = false
    //check database users table for user
    db.all("SELECT userid, password FROM users", function(err, rows) {
        if (err){ console.log(err); return;}
        for (var i = 0; i < rows.length; i++) {
          if (rows[i].userid == user & rows[i].password == password){
            authorized = true
          } 
        }
        return authorized;
      })
}

wss.on('connection', function connection(ws) {
  console.log('Client connected');
  
  ws.on('message', function incoming(message) {
    console.log('received: %s', message);
    // Parse the JSON message
    const data = JSON.parse(message);
    
    //authenticate and notify user
    if (data.action === "validate") {
      let result = authenticate(data);
      if (result){
        ws.send(JSON.stringify({ status: "success",validated: true, message: "User validation successful!" }))
      }
      ws.send(JSON.stringify({ status: "failed",validated: false, message: "User validation unsuccessful!" }))
    }
  });

  ws.on('close', function close() {
    console.log('Client disconnected');
  });
});

console.log('WebSocket server running on ws://localhost:8080');
