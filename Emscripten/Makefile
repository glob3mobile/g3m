# Builds the g3m Emscripten port (core + platform) as a static library, and the demo.
#
#   make MODE=release        (or MODE=debug)
#
# Run it inside the emscripten/emsdk image with build-in-container.sh, which pins the toolchain version.
# Outputs go to _build/$(MODE)/ (ignored by git).

ifeq ($(MODE),release)
  MODE_CXXFLAGS := -O2
  MODE_LDFLAGS  := -O2
else ifeq ($(MODE),debug)
  MODE_CXXFLAGS := -O0 -g
  MODE_LDFLAGS  := -O0 -g -sASSERTIONS=2
else
  $(error MODE must be release or debug, got "$(MODE)")
endif

# container path -> host path in DWARF and source maps, so DevTools finds the sources; set by build-in-container.sh
ifneq ($(FILE_PREFIX_MAP),)
  MODE_CXXFLAGS += -ffile-prefix-map=$(FILE_PREFIX_MAP)
endif

CXX := em++
AR  := emar

CORE_DIR     := ../cpp/G3M
PLATFORM_DIR := G3MEmscripten/G3MEmscripten
DEMO_DIR     := G3MEmscriptenDemo

BUILD_DIR := _build/$(MODE)
OBJ_DIR   := $(BUILD_DIR)/obj

CXXFLAGS := -std=c++17 -DC_CODE -Wall -MMD -MP \
            -I../cpp -I$(CORE_DIR) -IG3MEmscripten -I$(PLATFORM_DIR) \
            $(MODE_CXXFLAGS)

# browser floor: the 6.0.12 defaults, written down so a toolchain update can't move them silently
BROWSER_FLAGS := -sMIN_SAFARI_VERSION=150000 -sMIN_CHROME_VERSION=85 -sMIN_FIREFOX_VERSION=79

LDFLAGS := -lembind \
           -sALLOW_MEMORY_GROWTH=1 \
           -sALLOW_TABLE_GROWTH=1 \
           -sMAX_WEBGL_VERSION=2 \
           -sEXPORTED_RUNTIME_METHODS=ccall,cwrap \
           $(BROWSER_FLAGS) \
           $(MODE_LDFLAGS)

CORE_SOURCES     := $(shell find $(CORE_DIR) -name '*.cpp')
PLATFORM_SOURCES := $(shell find $(PLATFORM_DIR) -name '*.cpp')
DEMO_SOURCES     := $(DEMO_DIR)/main.cpp

# ../cpp/G3M/X.cpp -> _build/MODE/obj/cpp/G3M/X.o
object = $(OBJ_DIR)/$(patsubst ../%,%,$(1:.cpp=.o))

LIB_OBJECTS  := $(foreach s,$(CORE_SOURCES) $(PLATFORM_SOURCES),$(call object,$(s)))
DEMO_OBJECTS := $(foreach s,$(DEMO_SOURCES),$(call object,$(s)))

LIB        := $(BUILD_DIR)/libG3MEmscripten.a
DEMO_JS    := $(BUILD_DIR)/demo/G3MEmscriptenDemo.js
DEMO_FILES := $(patsubst $(DEMO_DIR)/html/%,$(BUILD_DIR)/demo/%,$(shell find $(DEMO_DIR)/html -type f))

.PHONY: all lib demo
all: demo
lib: $(LIB)
demo: $(DEMO_JS) $(DEMO_FILES)

$(LIB): $(LIB_OBJECTS)
	@mkdir -p $(@D)
	@echo "$(AR) rcs $@ ($(words $^) objects)"
	@$(AR) rcs $@ $^

$(DEMO_JS): $(DEMO_OBJECTS) $(LIB)
	@mkdir -p $(@D)
	$(CXX) $(DEMO_OBJECTS) $(LIB) $(LDFLAGS) -o $@

$(BUILD_DIR)/demo/%: $(DEMO_DIR)/html/%
	@mkdir -p $(@D)
	cp $< $@

$(OBJ_DIR)/cpp/%.o: ../cpp/%.cpp
	@mkdir -p $(@D)
	$(CXX) $(CXXFLAGS) -c $< -o $@

$(OBJ_DIR)/%.o: %.cpp
	@mkdir -p $(@D)
	$(CXX) $(CXXFLAGS) -c $< -o $@

-include $(LIB_OBJECTS:.o=.d) $(DEMO_OBJECTS:.o=.d)
