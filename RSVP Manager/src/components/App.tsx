import React, { useState } from 'react';
import { RsvpService } from '../services/RsvpService';
import { Player } from '../interfaces/RsvpInterfaces';

const App: React.FC = () => {
  const [playerName, setPlayerName] = useState('');
  const [rsvpStatus, setRsvpStatus] = useState<'Yes' | 'No' | 'Maybe'>('Maybe');
  const [rsvpService] = useState(new RsvpService());
  const [attendees, setAttendees] = useState<Player[]>(rsvpService.getConfirmedAttendees());
  const [responseCount, setResponseCount] = useState(rsvpService.countResponses());

  const [showAttendees, setShowAttendees] = useState(false);
  const [showResponseCount, setShowResponseCount] = useState(false);

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();
    const newPlayer: Player = { id: Date.now().toString(), name: playerName };
    rsvpService.addOrUpdateRsvp(newPlayer, rsvpStatus);

    setAttendees(rsvpService.getConfirmedAttendees());
    setResponseCount(rsvpService.countResponses());
    setPlayerName('');
  };

  return (
    <div
      style={{
        backgroundImage: `url("/sports-background.jpg")`, // Make sure this image is in your `public` folder
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        minHeight: '100vh',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        padding: '20px',
        textAlign: 'center',
        color: 'white',
      }}
    >
      <h1 style={{ color: '#FFD700' }}>RSVP Manager</h1>

      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '10px', width: '300px' }}>
        <input
          type="text"
          placeholder="Enter player's name"
          value={playerName}
          onChange={(e) => setPlayerName(e.target.value)}
          required
        />
        <select
          value={rsvpStatus}
          onChange={(e) => setRsvpStatus(e.target.value as 'Yes' | 'No' | 'Maybe')}
        >
          <option value="Yes">Yes</option>
          <option value="No">No</option>
          <option value="Maybe">Maybe</option>
        </select>
        <button type="submit">Submit RSVP</button>

      </form>

      <div style={{ marginTop: '20px' }}>
        <button onClick={() => setShowAttendees(!showAttendees)} style={{ margin: '10px' }}>
          {showAttendees ? 'Hide Confirmed Attendees' : 'Get Confirmed Attendees'}
        </button>
        <button onClick={() => setShowResponseCount(!showResponseCount)} style={{ margin: '10px' }}>
          {showResponseCount ? 'Hide RSVP Counts' : 'Get RSVP Counts'}
        </button>
      </div>

      {showAttendees && (
        <div>
          <h2>Confirmed Attendees</h2>
          <ul style={{ listStyle: 'none', padding: 0 }}>
            {attendees.map((player) => (
              <li key={player.id}>{player.name}</li>
            ))}
          </ul>
        </div>
      )}

      {showResponseCount && (
        <div>
          <h3>Response Counts</h3>
          <p>Total: {responseCount.total}</p>
          <p>Confirmed: {responseCount.confirmed}</p>
          <p>Declined: {responseCount.declined}</p>
	  <p>Maybe: {responseCount.maybe}</p>
        </div>
      )}
    </div>
  );
};

export default App;
