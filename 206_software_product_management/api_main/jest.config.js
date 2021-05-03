module.exports = {
  rootDir: '.', // This should point to the rootDir set in your tsconfig.json
  globals: {
    'ts-jest': {
      tsconfig: './tsconfig.json',
    },
  },
  verbose: true,
  preset: 'ts-jest',
  testEnvironment: 'node',
  setupFiles: [
    'dotenv/config',
  ],
  modulePathIgnorePatterns: [
    '<rootDir>/dist/',
  ],
  moduleNameMapper: {
    '@interfaces(.*)': '<rootDir>/src/interfaces/$1',
    '@mongo/(.*)': '<rootDir>/src/mongo/$1',
    '@server/(.*)': '<rootDir>/src/server/$1',
  },
};
