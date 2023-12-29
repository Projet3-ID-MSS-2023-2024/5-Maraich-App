import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MaraicherComponent } from './maraicher.component';

describe('MaraicherComponent', () => {
  let component: MaraicherComponent;
  let fixture: ComponentFixture<MaraicherComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MaraicherComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MaraicherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
