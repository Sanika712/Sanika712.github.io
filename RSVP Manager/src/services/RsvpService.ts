// src/services/RsvpService.ts
import { Player, RsvpEntry } from '../interfaces/RsvpInterfaces';

export class RsvpService {
  private rsvpList: RsvpEntry[] = [];

  addOrUpdateRsvp(player: Player, status: "Yes" | "No" | "Maybe"): void {
    const existingEntry = this.rsvpList.find(entry => entry.player.id === player.id);
    if (existingEntry) {
      existingEntry.status = status; // Update the status if player already exists
    } else {
      this.rsvpList.push({ player, status }); // Add new player entry
    }
  }

  getConfirmedAttendees(): Player[] {
    return this.rsvpList.filter(entry => entry.status === "Yes").map(entry => entry.player);
  }

  countResponses(): { total: number; confirmed: number; declined: number; maybe: number } {
  const total = this.rsvpList.length;
  const confirmed = this.rsvpList.filter(entry => entry.status === "Yes").length;
  const declined = this.rsvpList.filter(entry => entry.status === "No").length;
  const maybe = this.rsvpList.filter(entry => entry.status === "Maybe").length;

  return { total, confirmed, declined, maybe };
  }
}
