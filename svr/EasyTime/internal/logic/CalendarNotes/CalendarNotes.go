package calendarnotes

import (
	"EasyTime/internal/model"
	"context"
)

type (
	sCalendarNotes struct{}
)

// func init() {
// 	service.RegisterMiddleware
// }

func New() *sCalendarNotes {
	return &sCalendarNotes{}
}

func (s *sCalendarNotes) AddNotes(ctx context.Context, in model.CalendarNotesAddInput) {

}

func (s *sCalendarNotes) DelNotes(ctx context.Context, in model.CalendarNotesDelImput) {

}

func (s *sCalendarNotes) ModifyNotes(ctx context.Context, in model.CalendarNotesModifyInput) {

}
