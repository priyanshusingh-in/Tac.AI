![tacai-app-icon-animated](https://github.com/user-attachments/assets/7303f82e-dd06-4a42-9e90-5668caf259cf)# Tac.AI

![<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 108 108">
  <defs>
    <linearGradient id="bg-gradient" x1="0" y1="0" x2="1" y2="1">
      <stop offset="0%" style="stop-color:#2B1B4E"/>
      <stop offset="50%" style="stop-color:#3B1B3E"/>
      <stop offset="100%" style="stop-color:#6E1B2E"/>
    </linearGradient>
    
    <filter id="glow" x="-50%" y="-50%" width="200%" height="200%">
      <feGaussianBlur in="SourceGraphic" stdDeviation="1.5" result="blur"/>
      <feColorMatrix in="blur" type="matrix" values="
        1 0 0 0 0
        0 1 0 0 0
        0 1 0 0 0
        0 0 0 15 -6" result="glow"/>
      <feMerge>
        <feMergeNode in="glow"/>
        <feMergeNode in="SourceGraphic"/>
      </feMerge>
    </filter>

    <mask id="safe-area">
      <rect x="8" y="8" width="92" height="92" rx="20" fill="white"/>
    </mask>
  </defs>

  <!-- Background -->
  <rect width="108" height="108" fill="url(#bg-gradient)" mask="url(#safe-area)"/>

  <!-- Main game board -->
  <g transform="translate(24,24)" filter="url(#glow)">
    <!-- Game grid with looping animations -->
    <g stroke="#fff" stroke-width="2" opacity="0.8">
      <line x1="20" y1="0" x2="20" y2="60">
        <animate attributeName="y2" values="0;60;60;60;0" dur="3s" repeatCount="indefinite"/>
      </line>
      <line x1="40" y1="0" x2="40" y2="60">
        <animate attributeName="y2" values="0;60;60;60;0" dur="3s" begin="0.2s" repeatCount="indefinite"/>
      </line>
      <line x1="0" y1="20" x2="60" y2="20">
        <animate attributeName="x2" values="0;60;60;60;0" dur="3s" begin="0.4s" repeatCount="indefinite"/>
      </line>
      <line x1="0" y1="40" x2="60" y2="40">
        <animate attributeName="x2" values="0;60;60;60;0" dur="3s" begin="0.6s" repeatCount="indefinite"/>
      </line>
    </g>

    <!-- Game pieces -->
    <g>
      <!-- AI's X with looping animation -->
      <g transform="translate(5,5)" stroke="#4CA9FF" stroke-width="3" stroke-linecap="round">
        <line x1="2" y1="2" x2="10" y2="10">
          <animate attributeName="x2" values="2;10;10;10;2" dur="3s" begin="0.8s" repeatCount="indefinite"/>
          <animate attributeName="y2" values="2;10;10;10;2" dur="3s" begin="0.8s" repeatCount="indefinite"/>
        </line>
        <line x1="10" y1="2" x2="2" y2="10">
          <animate attributeName="x1" values="2;10;10;10;2" dur="3s" begin="0.8s" repeatCount="indefinite"/>
          <animate attributeName="y2" values="2;10;10;10;2" dur="3s" begin="0.8s" repeatCount="indefinite"/>
        </line>
      </g>

      <!-- Player's O -->
      <circle cx="50" cy="30" r="5" fill="none" stroke="#FF6B6B" stroke-width="3">
        <animate attributeName="r" values="0;5;5;5;0" dur="3s" begin="1s" repeatCount="indefinite"/>
      </circle>

      <!-- AI's X -->
      <g transform="translate(25,45)" stroke="#4CA9FF" stroke-width="3" stroke-linecap="round">
        <line x1="2" y1="2" x2="10" y2="10">
          <animate attributeName="x2" values="2;10;10;10;2" dur="3s" begin="1.2s" repeatCount="indefinite"/>
          <animate attributeName="y2" values="2;10;10;10;2" dur="3s" begin="1.2s" repeatCount="indefinite"/>
        </line>
        <line x1="10" y1="2" x2="2" y2="10">
          <animate attributeName="x1" values="2;10;10;10;2" dur="3s" begin="1.2s" repeatCount="indefinite"/>
          <animate attributeName="y2" values="2;10;10;10;2" dur="3s" begin="1.2s" repeatCount="indefinite"/>
        </line>
      </g>
    </g>
  </g>
</svg>
Uploading tacai-app-icon-animated.svg…]()


A modern Android implementation of Tic-tac-toe with an AI opponent, featuring a sleek dark UI theme and timed moves.

## Features

- Player vs AI gameplay
- 10-second timer for each move
- Score tracking system
- Beautiful dark theme UI with gradient background
- Responsive grid layout
- Reset game functionality
- Sound effects for moves and game events

## Technical Details

- Built for Android using Kotlin
- Minimum SDK version: API 26("Oreo"; Android 8.0)
- Build configuration language: Kotlin DSL (build.gradle.kts)

## Game Rules

- Classic 3x3 Tic-tac-toe grid
- Players alternate turns with X's and O's
- 10-second time limit per move
- Game ends when:
  - A player gets three in a row (horizontally, vertically, or diagonally)
  - The board is full (draw)
  - Time runs out for a player's move

## Installation

1. Clone the repository
```bash
git clone https://github.com/priyanshusingh-in/Tac.AI.git
```
2. Open the project in Android Studio
3. Build and run on your Android device or emulator

## Tech Stack

- Kotlin
- Android SDK
- Android Media Player for sound effects
- CountDownTimer for move timing
- Custom UI components
- Linear Layouts for responsive design

## Contributing

Feel free to fork the repository and submit pull requests for any improvements.

## Developer

Priyanshu Singh

## Screenshots
![3](https://github.com/user-attachments/assets/b7178a16-5cfa-4bf0-8c7e-cae28b0935d2)
![4](https://github.com/user-attachments/assets/587dea4f-1d24-4006-9d38-4eb561f4d764)
![5](https://github.com/user-attachments/assets/45c640ac-ad99-4679-9159-2a1d6a71fe91)

