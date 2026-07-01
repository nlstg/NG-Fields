console.log("Building Angular project...");
const fs = require('fs');
const path = require('path');
const { exec } = require('child_process');

const projectRoot = path.join(__dirname, '..');
const packageJson = JSON.parse(fs.readFileSync(path.join(projectRoot, 'package.json'), 'utf8'));

const buildCommand = packageJson.scripts?.build || 'ng build';

exec(buildCommand, { cwd: projectRoot, shell: true }, (error, stdout, stderr) => {
  if (error) {
    console.error('Build failed:', error);
    process.exit(1);
  }
  console.log('Build successful!');
  console.log(stdout);
});