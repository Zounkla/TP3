import { defineConfig, loadEnv } from '@rsbuild/core';
import { pluginReact } from '@rsbuild/plugin-react';

const { publicVars, rawPublicVars } = loadEnv({ prefixes: ['REACT_APP_'] });

export default defineConfig({
    plugins: [pluginReact()],
    source: {
        define: {
            ...publicVars,
            'process.env': JSON.stringify(rawPublicVars),
        },
    },
});